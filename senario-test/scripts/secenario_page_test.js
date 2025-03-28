/**
 * 실행
 * k6 run --out influxdb=http://localhost:8086/k6 ./scripts/scenario_test.js
 */
import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

// 테스트할 홈페이지 URL
const BASE_URL = 'http://localhost:3000';

// 에러 카운터 정의 (Grafana에서 추적 가능)
const errorCounter = new Counter('custom_errors');

export let options = {
    stages: [
        { duration: '1.5m', target: 25 },  // 1.5분간 VUser 25로 서서히 증가 (25%)
    ],
    thresholds: {
        http_req_duration: ['p(95)<750'], // 95%의 응답 시간이 750ms 이하인지 확인
        'custom_errors': ['count==0'],    // 에러 발생 시 테스트 실패로 간주
    },
};

export default function () {
    // 홈페이지에 GET 요청 보내기
    const res = http.get(BASE_URL, {
        headers: { 'Content-Type': 'text/html' }, // 홈페이지 요청이므로 적절한 헤더
        tags: { page_name: 'homepage' },          // 모니터링용 태그
    });

    // 응답 체크
    const pageLoadSuccess = check(res, {
        'page load successful': (r) => r.status === 200, // 200 OK인지 확인
    });

    // 실패 시 에러 로그 출력 및 카운터 증가
    if (!pageLoadSuccess) {
        console.error(`Page load failed: ${res.status} - ${res.body}`);
        errorCounter.add(1, { error_api_name: 'homepage' });
    }
}