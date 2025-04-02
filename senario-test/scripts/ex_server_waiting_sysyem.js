import http from 'k6/http';
import {check, sleep} from 'k6';
import {Counter} from 'k6/metrics';
import {SharedArray} from 'k6/data';

// 유저 데이터 로드
const users = new SharedArray('users', function () {
    return JSON.parse(open('./ex_server_waiting_system_user_data.json')).userPayloads; // 1000명 유저 데이터
});

// 에러 카운터
const errorCounter = new Counter('custom_errors');

const BASE_URL = ''; // API 기본 URL (필요 시 수정)

// 테스트 옵션: 유저 수를 10
export let options = {
    stages: [
        {duration: '1m', target: 10},   // 10명
    ],
    thresholds: {
        http_req_duration: ['p(95)<1000'], // 95% 응답 시간이 1초 이하
        'custom_errors': ['count==0'],     // 에러 발생 시 테스트 실패
    },
};

// 유저 시나리오 (각 유저는 한 번만 실행)
export function userScenario() {
    const userIndex = __VU - 1; // 가상 유저 ID (1부터 시작하므로 -1)
    if (userIndex >= users.length) return; // 유저 데이터 초과 시 종료

    const user = users[userIndex]; // 고유 유저 선택
    const username = user.id;


    // 1. 어드민 로그인 (유저가 어드민으로 로그인)
    const loginRes = http.post(`${BASE_URL}/api/v1/users/login/admin`, JSON.stringify({
        id: user.id,
        password: user.password,
    }), {
        headers: {'Content-Type': 'application/json'},
        tags: {page_name: 'user_login'},
    });

    const loginSuccess = check(loginRes, {
        'login successful': (res) => res.status === 200 && res.json('accessToken') !== '',
    });

    if (!loginSuccess) {
        console.error(`[${username}] Login failed: ${loginRes.status} - ${loginRes.body}`);
        errorCounter.add(1, {error_api_name: 'login'});
        return;
    }

    // 올바른 경로에서 accessToken 가져오기
    const token = loginRes.json().loginSuccessPayload.accessToken;
    const headers = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
    };
    // 대기 등록 번호 조회
    const statusRes = http.get(`${BASE_URL}/api/v1/users/me/waiting-lists?reservationSettingId=1`, {
        headers: headers,
        tags: {name: 'waiting_status'},
    });


    const statusSuccess = check(statusRes, {
        'status check successful': (res) => res.status === 200,
    });

    // 대기 등록하면 대기 상태가 'WAITING'으로 변경되어 다시 대기 등록하지 않음
    if (!statusSuccess || statusRes.json().status !== 'WAITING') {
        // 3. 대기 등록
        const waitingRes = http.post(`${BASE_URL}/api/v1/waiting-lists`, JSON.stringify({
            isRemote: false,
            reservationSettingId: 1
        }), {
            headers: headers,
            tags: {name: 'waiting_register'},
        });

        const waitingSuccess = check(waitingRes, {
            'waiting register successful': (res) => res.status === 201,
        });

        if (!waitingSuccess) {
            console.error(`[${username}] Waiting register failed: ${waitingRes.status} - ${waitingRes.body}`);
            errorCounter.add(1, {error_api_name: 'waiting_register'});
            return;
        }

        const waitingId = waitingRes.json('id'); // 대기 ID 추출 (응답에 따라 수정 필요)

        // 3. 슬랙 알림 등록
        const slackRes = http.post(`${BASE_URL}/api/v1/auth/slack`, {
            headers: headers,
            tags: {name: 'slack_register'},
        });

        check(slackRes, {
            'slack register successful': (res) => res.status === 200,
        });
    }

    const waitingStatus = statusRes.json('status'); // status 필드 추출

    if (waitingStatus === 'COMPLETED') {
        console.info(`[${username}] Waiting status is COMPLETED. Terminating polling.`);
    }

    sleep(1); // 1초 대기
}

// 어드민 시나리오 (단일 어드민 실행)
export function adminScenario() {

    // // 1분 뒤에 실행하기
    // sleep(60);

    // 1. 어드민 로그인
    const loginRes = http.post(`${BASE_URL}/api/v1/users/login/admin`, JSON.stringify({
        id: "hwchoi22",
        password: "test1234",
    }), {
        headers: {'Content-Type': 'application/json'},
        tags: {page_name: 'admin_login'},
    });


    const loginSuccess = check(loginRes, {
        'admin login successful': (res) => res.status === 200 && res.json('accessToken') !== '',
    });

    if (!loginSuccess) {
        console.error(`[Admin] Login failed: ${loginRes.status} - ${loginRes.body}`);
        errorCounter.add(1, {error_api_name: 'admin_login'});
        return;
    }

    // 올바른 경로에서 accessToken 가져오기
    const token = loginRes.json().loginSuccessPayload.accessToken;
    const headers = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
    };

    // 2. 대기 현황 조회 및 상태 변경 (1초 주기)
    const waitingListRes = http.get(`${BASE_URL}/api/v1/waiting-lists`, {
        headers: headers,
        tags: {name: 'admin_waiting_list'},
    });


    const listSuccess = check(waitingListRes, {
        'waiting list successful': (res) => res.status === 200,
    });

    if (!listSuccess) {
        console.error(`[Admin] Waiting list failed: ${waitingListRes.status} - ${waitingListRes.body}`);
        errorCounter.add(1, {error_api_name: 'waiting_list'});
    }

    console.log(waitingListRes.json('waitingList'));
    const waitingList = waitingListRes.json('waitingList');
    const waitingItem = waitingList.find(item => item.status === 'WAITING');

    if (!waitingItem) {
        console.info('[Admin] No WAITING status found. Terminating.');
    } else {
        // 3. 상태 변경
        const statusRes = http.put(`${BASE_URL}/api/v1/waiting-lists/${waitingItem.id}/status`, JSON.stringify({
            status: 'COMPLETED',
        }), {
            headers: headers,
            tags: {name: 'status_update'},
        });


        check(statusRes, {
            'status update successful': (res) => res.status === 200,
        });
    }
    sleep(1); // 5초 대기
}

// 기본 실행 함수: 유저와 어드민 시나리오 분리
export default function () {
    if (__VU === 1) {
        adminScenario(); // 첫 번째 VU는 어드민 역할
    }
    else {
        userScenario(); // 나머지 VU는 유저 역할
    }
}