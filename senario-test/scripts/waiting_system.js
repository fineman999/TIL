import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

// test.json 파일을 SharedArray로 로드
const users = new SharedArray('users', function () {
    return JSON.parse(open('./waiting_login.json')).userPayloads;
});

// Redis 대신 k6의 SharedArray와 Counter로 유니크 사용자 생성
import { SharedArray } from 'k6/data';
const JWT_SECRET = "thisisjusttestaccesssecretsodontworry";
let userCounter = new Counter('user_counter');

// 에러 카운터 정의
const errorCounter = new Counter('custom_errors');

const BASE_URL = 'http://localhost:8080';
const PERFORMANCE_ID = 2;

export let options = {
    stages: [
        { duration: '1m', target: 10 },   // 1분간 VUser 10으로 증가
        { duration: '3m', target: 10 },   // VUser 10 유지
        { duration: '1m', target: 20 },   // 1분간 VUser 20으로 증가
        // { duration: '3m', target: 20 },   // VUser 20 유지
        // { duration: '1m', target: 0 },    // 1분간 VUser 0으로 감소
    ],
    thresholds: {
        http_req_duration: ['p(95)<1000'], // 95% 응답 시간이 1초 이하
        'custom_errors': ['count==0'],     // 에러 발생 시 테스트 실패
    },
};

export default function () {

    // 랜덤한 유저 선택
    const user = users[Math.floor(Math.random() * users.length)];

    const username = user.email;
    const loginRes = http.post(`${BASE_URL}/api/v1/login`, JSON.stringify({
        email: user.email,
        password: user.password,
    }), {
        headers: { 'Content-Type': 'application/json' },
        tags: { page_name: "get_login" },
    });

    const loginSuccess = check(loginRes, {
        'login successful': (res) => res.status === 200 && res.json('accessToken') !== '',
    });

    if (!loginSuccess) {
        console.error(`Login failed: ${loginRes.status} - ${loginRes.body}`);
        errorCounter.add(1,{error_api_name: 'login'});
        return;
    }

    const token = loginRes.json('accessToken');

    // 공통 헤더 설정
    const headers = {
        'Authorization': `Bearer ${token}`,
        'performanceId': `${PERFORMANCE_ID}`,
        'Content-Type': 'application/json',
    };

    // 1. 예매 사이트 입장
    const seatRes = http.get(`${BASE_URL}/api/v1/performances/${PERFORMANCE_ID}/seats`, {
        headers: headers,
        tags: { name: '1. 예매 사이트 입장' },
    });

    const seatCheck = check(seatRes, {
        'seat request successful': (r) => r.status === 200 || r.status === 307,
    });

    if (!seatCheck) {
        console.error(`[${username}] Seat request failed: ${seatRes.status} - ${seatRes.body}`);
        errorCounter.add(1, { error_api_name: 'seat_request' });
        return;
    }

    if (seatRes.status === 200) {
        console.info(`[${username}] 사용자 대기열 잔류 중. 시나리오 종료.`);
        return;
    } else if (seatRes.status === 307) {
        console.info(`[${username}] 대기열 입장. 남은 순번 폴링 단계로 이동.`);
    }

    // 2. 대기열 순번 조회 (폴링)
    let remainingCount = null;
    while (true) {
        const waitRes = http.get(`${BASE_URL}/performances/${PERFORMANCE_ID}/wait`, {
            headers: headers,
            tags: { name: '2. 남은 대기 순번 조회' },
        });

        const waitCheck = check(waitRes, {
            'wait request successful': (r) => r.status === 200,
        });

        if (!waitCheck) {
            console.error(`[${username}] Wait request failed: ${waitRes.status} - ${waitRes.body}`);
            errorCounter.add(1, { error_api_name: 'wait_request' });
            break;
        }

        remainingCount = waitRes.json('remainingCount');
        if (remainingCount <= 0) {
            console.info(`[${username}] 대기 종료.`);
            break;
        }

        console.info(`[${username}] 남은 순번=${remainingCount}`);
        sleep(1); // 1초 대기 후 재시도
    }
}