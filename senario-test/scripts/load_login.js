import { URL } from 'https://jslib.k6.io/url/1.0.0/index.js';
import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';

const BASE_URL = 'https://dev-go.witch-market.com';

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
        { duration: '10s', target: 1 },
    ],
    thresholds: {
        http_req_duration: ['p(95)<512'],
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

    check(loginRes, {
        'login successful': (res) => res.status === 200 && res.json('accessToken') !== '',
    });

    const accessToken = loginRes.json('accessToken');

    if (!accessToken) {
        console.error('Failed to get access token');
        return;
    }

    // 게임 점수 저장 API 호출
    const score = Math.floor(Math.random() * (500 - 10 + 1)) + 10;
    const gameStoreRes = http.post(`${BASE_URL}/api/v1/games/records`, JSON.stringify({ score }), {
        headers: { Authorization: `Bearer ${accessToken}`, 'Content-Type': 'application/json' },
        tags: { page_name: "game_store" },
    });

    check(gameStoreRes, {
        'game record stored': (res) => res.status === 200,
    });

    // 인벤토리 자산 확인 API 호출
    const assetCheckRes = http.get(`${BASE_URL}/api/v1/inventory/view/query`, {
        headers: { Authorization: `Bearer ${accessToken}` },
        tags: { page_name: "asset_check" },
    });

    check(assetCheckRes, {
        'asset check successful': (res) => res.status === 200,
    });

    // 인벤토리 조회 API 호출
    const getStorageRes = http.get(`${BASE_URL}/api/v1/inventory/query`, {
        headers: { Authorization: `Bearer ${accessToken}` },
        tags: { page_name: "get_storage" },
    });

    check(getStorageRes, {
        'get storage successful': (res) => res.status === 200,
    });

    // 랜덤한 아티스트 선택
    const artists = ["eunkwang", "minhyuk", "hyunsik", "peniel"];
    const artistMemberName = artists[Math.floor(Math.random() * artists.length)];

    // 편지 작성 API 호출
    // 랜덤한 메시지 선택
    const message = messages[Math.floor(Math.random() * messages.length)];
    const postLetterRes = http.post(`${BASE_URL}/api/v1/post/create`, JSON.stringify({
        artistMemberName,
        message,
    }), {
        headers: { Authorization: `Bearer ${accessToken}`, 'Content-Type': 'application/json' },
        tags: { page_name: "post_letter" },
    });

    check(postLetterRes, {
        'post letter successful': (res) => res.status === 200,
    });

    sleep(1);
}