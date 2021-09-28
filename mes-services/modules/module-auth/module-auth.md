# Authentication Microservice Application

인증 처리 및 토큰 발급(JWT)

## 실행 프로필 
- local
  localhost 상에서 개발 환경을 구성 및 어플리케이션 실행하는 경우
- dev
  개발 서버에 환경 구성 및 어플리케이션 실행하는 경우
- prod
  운영 서버에 환경 구성 및 어플리케이션 실행하는 경우


local 프로필로 실행하는 경우 로컬 mysql로 접속하며, 로컬 mysql의 사용자 테이블 및 oauth_client_details 테이블에

테스트를 위한 클라이언트와 user를 삽입합니다. (아래 참고) - 이미 생성되어 있다면 건너뜀 -

## 사용자 계정 / 비밀번호

- user01 / user01

## oAuth client 계정 / 비밀번호

- gndbiz / gndbiz1101

# oauth endpoints 설명

## /oauth/token (Spring Oauth 기본)

## /oauth/check_token (Spring Oauth 기본)

## /oauth/login

## /oauth/logout