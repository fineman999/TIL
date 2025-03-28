import { URL } from 'https://jslib.k6.io/url/1.0.0/index.js';
import http from 'k6/http';
import { check, group, sleep, fail } from 'k6';

export let options = {
    stages: [ // 테스트 단계, 아래에 설정된대로 테스트가 진행된다.
        { duration: '3m', target: 25 }, //먼저 3분 동안 VUser 1에서 25까지 서서히 올린다.
        { duration: '10m',target: 25 }, //Vuser 25에서 10분간 유지한다.
        { duration: '3m', target: 125 }, //다시 3분간 25에서 125까지 서서히 올린다.
        { duration: '30m',target: 125 }, //30분간 유지
        { duration: '3m', target: 0 },  //3분 동안 Vuser 0으로 내려온다.
    ],

    thresholds: { // 부하 테스트가 언제 성공했다고 할 수 있는지
        http_req_duration: ['p(95)<138'], // 전체 요청의 95%가 138ms 안에 들어오면 성공
    },
};

const BASE_URL = 'https://www.subway-sgo8308.o-r.kr';
const STATION_COUNT = 16;

function getRandomStationId(){
    return Math.floor(Math.random() * STATION_COUNT) + 1;
}

function getPath(){
    let pathRes = http.get(`${BASE_URL}/path`, {
        tags: {
            page_name: "get_path", // tag에 따라 결과를 볼 수 있게 tag이름을 get_path로 지정
        },
    }); // get요청을 진행하고 결과를 리턴

    check(pathRes, { // 결과를 체크
        'success to get path': (res) => res.status === 200,//응답 상태코드가 200이면 성공
    });
}

function getPathResults(){
    var url = new URL(`${BASE_URL}/paths`);
    var sourceId = getRandomStationId();
    var targetId = getRandomStationId();
    url.searchParams.append('source', sourceId);
    url.searchParams.append('target', targetId);
    //최종 url 형태 : https://subway-sgo8308.o-r.kr/paths?source=1&target=10

    let pathResultRes = http.get(url.toString(), {
        tags: {
            page_name: "get_path_result",
        },
    });

    check(pathResultRes, {
        'success to get path results': (res) => "stations" in res
    });
}

export default function ()  {//이 default 함수만 테스트동안 계속 실행, 나머지는 1번만
    getPath(); // 경로 검색 페이지를 Get요청하고
    getPathResults(); // 경로 검색 결과 페이지를 Get요청한다.
};