import http from 'k6/http';
import { check, sleep } from 'k6';

// 부하 테스트 옵션 설정
export let options = {
    vus: 500, // 동시 사용자 수 (가상 사용자 500명)
    duration: '30s', // 테스트 지속 시간 (30초)
};

const baseUrl = 'http://host.docker.internal:8080/api/waiting-queue'; // API 기본 URL

// 콘서트 ID (테스트용 고정 값)
const concertId = 1; // 실제 콘서트 ID로 변경 필요

export default function () {
    // 1. 대기열 토큰 발급 요청 (POST /issue-token)
    const issueTokenUrl = `${baseUrl}/issue-token?concertId=${concertId}`;
    const issueResponse = http.post(issueTokenUrl);

    // 토큰 발급 결과 검증
    check(issueResponse, {
        'issueToken: 응답 상태 200': (res) => res.status === 200,
        'issueToken: 응답에 토큰 존재': (res) => res.status === 200 && res.json().token !== undefined,
    });

    // 발급된 토큰 가져오기 (응답이 성공적일 때만)
    let issuedToken = null;
    if (issueResponse.status === 200) {
        issuedToken = issueResponse.json().token;
    }

    sleep(1); // 대기 시간 (1초)

    // 2. 대기열 토큰 조회 요청 (GET /token)
    if (issuedToken) {
        const getTokenUrl = `${baseUrl}/token`;
        const headers = { 'Authorization': `Bearer ${issuedToken}` }; // 헤더에 토큰 추가
        const getTokenResponse = http.get(getTokenUrl, { headers: headers });

        // 토큰 조회 결과 검증
        check(getTokenResponse, {
            'getToken: 응답 상태 200': (res) => res.status === 200,
            'getToken: 응답에 대기열 정보 존재': (res) => res.status === 200 && res.json().id !== undefined,
        });
    }

    sleep(1); // 각 요청 사이에 1초 대기
}