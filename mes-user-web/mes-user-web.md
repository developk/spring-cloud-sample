# mes-user-web

도입기업에 공급 될 MES 웹 어플리케이션

![Vue](https://img.shields.io/badge/vue-v2.6.11-green?logo=vue.js)

## template 원본 문서 

[템플릿 원본 문서](./origin.md)

## 프로젝트 셋팅 및 어플리케이션 실행

*nodejs가 설치되어 있어야 합니다.*

```
cd mes-user-web // mes-user-web 경로로 이동

npm install // package.json에 기술 된 의존 라이브러리 설치

npm run serve // live reload 상태의 개발용 서버 실횅.

npm run build // production 용 build
```

## oAuth Token 정보 요청 파라미터
```
    host:
    http://localhost:9002/oauth/token
    
    header:
    "Authorization: Basic Z25kYml6OmduZGJpejExMDE="
    
    form-data:
    - grant_type = 'password'
    - username = 'user01'
    - password = 'user01'
    
    
    curl --location --request POST 'http://localhost:9002/oauth/token' \
            --header 'Authorization: Basic Z25kYml6OmduZGJpejExMDE=' \
            --form 'grant_type="password"' \
            --form 'username="user01"' \
            --form 'password="user01"'
```


## 응답 예시
```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzAzNDcxMjUsInVzZXJfbmFtZSI6InVzZXIwMSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJmNjdmMzAyNy1jYjZhLTQ2NTMtOTM5NC1hMmIyZWVlM2ZkZjkiLCJjbGllbnRfaWQiOiJnbmRiaXoiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.OQvhpD0fz9j7N6BJwxTs3mDe9ABCEsISOLIxi-vf2Ac",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ1c2VyMDEiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoiZjY3ZjMwMjctY2I2YS00NjUzLTkzOTQtYTJiMmVlZTNmZGY5IiwiZXhwIjoxNjMyOTAzMTI1LCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiNDU0NDgyNDctNzAyZi00ZWMwLWI4MjAtYmZiYjc5YzQ4MzAzIiwiY2xpZW50X2lkIjoiZ25kYml6In0.3hwO8bDcgo7E7sVEmWKwLW1TuqYuUyu-bVfAXpDtmag",
    "expires_in": 36000,
    "scope": "read write",
    "jti": "f67f3027-cb6a-4653-9394-a2b2eee3fdf9"
}
```