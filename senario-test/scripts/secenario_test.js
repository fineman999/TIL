/**
 * 실행
 * k6 run --out influxdb=http://localhost:8086/k6 ./scripts/secenario_test.js
 */
import { URL } from 'https://jslib.k6.io/url/1.0.0/index.js';
import http from 'k6/http';
import { check } from 'k6';
import { SharedArray } from 'k6/data';
import { Counter } from 'k6/metrics';

const BASE_URL = 'http://localhost:3000';

// 에러 카운터 정의 (Grafana에서 추적 가능)
const errorCounter = new Counter('custom_errors');

// test.json 파일을 SharedArray로 로드
const users = new SharedArray('users', function () {
    return JSON.parse(open('./test.json')).userPayloads;
});

// message.json 파일을 SharedArray로 로드
const messages = new SharedArray('messages', function () {
    return JSON.parse(open('./message.json')).messages;
});

export let options = {
    stages: [
        { duration: '1.5m', target: 25 },  // 1.5분간 VUser 25로 서서히 증가 (25%)
        { duration: '5m', target: 25 },    // VUser 25 유지
        { duration: '1.5m', target: 50 },  // 1.5분간 VUser 50으로 증가 (50%)
        { duration: '5m', target: 50 },    // VUser 50 유지
        { duration: '1.5m', target: 100 }, // 1.5분간 VUser 100으로 증가 (100%)
        { duration: '10m', target: 100 },  // VUser 100 유지 (최대 부하)
        { duration: '3m', target: 0 },     // 3분간 VUser 0으로 감소
    ],
    thresholds: {
        http_req_duration: ['p(95)<750'], // 95%의 응답 시간이 750ms 이하인지 확인
        'custom_errors': ['count==0'], // 에러 발생 시 테스트 실패로 간주
    },
};

export default function () {
    // 랜덤한 유저 선택
    const user = users[Math.floor(Math.random() * users.length)];

    const loginRes = http.post(`${BASE_URL}/api/v2/users/login`, JSON.stringify({
        id: user.nickname,
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

    const accessToken = loginRes.json('accessToken');

    // 게임 점수 저장 API 호출
    const score = Math.floor(Math.random() * (500 - 10 + 1)) + 10;
    const gameStoreRes = http.post(`${BASE_URL}/api/v1/games/records`, JSON.stringify({ score }), {
        headers: { Authorization: `Bearer ${accessToken}`, 'Content-Type': 'application/json' },
        tags: { page_name: "game_store" },
    });

    if (!check(gameStoreRes, { 'game record stored': (res) => res.status === 200 })) {
        console.error(`Game store failed: ${gameStoreRes.status} - ${gameStoreRes.body}`);
        errorCounter.add(1,{error_api_name: 'game_store'});
    }

    // 인벤토리 자산 확인 API 호출
    const assetCheckRes = http.get(`${BASE_URL}/api/v1/inventory/view/query`, {
        headers: { Authorization: `Bearer ${accessToken}` },
        tags: { page_name: "asset_check" },
    });

    if (!check(assetCheckRes, { 'asset check successful': (res) => res.status === 200 })) {
        console.error(`Asset check failed: ${assetCheckRes.status} - ${assetCheckRes.body}`);
        errorCounter.add(1,{error_api_name: 'asset_check'});
    }

    // 인벤토리 조회 API 호출
    const getStorageRes = http.get(`${BASE_URL}/api/v1/inventory/query`, {
        headers: { Authorization: `Bearer ${accessToken}` },
        tags: { page_name: "get_storage" },
    });

    if (!check(getStorageRes, { 'get storage successful': (res) => res.status === 200 })) {
        console.error(`Get storage failed: ${getStorageRes.status} - ${getStorageRes.body}`);
        errorCounter.add(1,{error_api_name: 'get_storage'});
    }

    // 랜덤한 아티스트 선택
    const artists = ["eunkwang", "minhyuk", "hyunsik", "peniel"];
    const artistMemberName = artists[Math.floor(Math.random() * artists.length)];

    // 편지 작성 API 호출
    const message = messages[Math.floor(Math.random() * messages.length)];
    const postLetterRes = http.post(`${BASE_URL}/api/v1/post/create`, JSON.stringify({
        artistMemberName,
        message,
    }), {
        headers: { Authorization: `Bearer ${accessToken}`, 'Content-Type': 'application/json' },
        tags: { page_name: "post_letter" },
    });

    if (!check(postLetterRes, { 'post letter successful': (res) => res.status === 200 })) {
        console.error(`Post letter failed: ${postLetterRes.status} - ${postLetterRes.body}`);
        errorCounter.add(1,{error_api_name: 'post_letter'});
    }
}
