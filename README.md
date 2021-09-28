# std-16-cld-app

STD v1.6 Spring Cloud 기반 MSA 아키텍쳐 프로젝트

![Java_16](https://img.shields.io/badge/java-v16-red?logo=java)
![Spring_Boot](https://img.shields.io/badge/Spring_Boot-v2.5.2-green.svg?logo=spring)
![Spring_Cloud](https://img.shields.io/badge/Spring_Cloud-v2020.0.3-green.svg?logo=spring)

# 업데이트 내역

[RELEASE for mes-services](mes-services/RELEASE.md)

[RELEASE for mes-admin-web](mes-admin-web/RELEASE.md)

[RELEASE for mes-user-web](mes-user-web/RELEASE.md)

# 개발환경

## - back-end (mes-services)
* java 16 (15버전은 사용 중지)
* Spring boot 2.5.2
* Spring cloud 2020.0.3

## 각 서버 모듈 별 md 문서 링크

[spring cloud config store server documents](mes-services/config-store/config-store.md)

[eureka discovery server documents](mes-services/discovery/discovery-server.md)

[spring cloud gateway server documents](mes-services/gateway/gateway-server.md)

## 각 micro service 모듈 별 md 문서 링크

[module-auth docs](mes-services/modules/module-auth/module-auth.md)

[module-core docs](mes-services/modules/module-core/module-core.md)

[module-user docs](mes-services/modules/module-user/module-user.md)

[module-company docs](mes-services/modules/module-company/module-company.md)

## 실행순서

1. Eureka Discovery server 실행
    - Discovery Client로 지정된 서버 및 모듈들은 실행 시 eureka discovery server에 등록되고, 주기적으로 양쪽에서 서비스의 가동 여부가 체크됩니다.
    - 현재 프로젝트 설정상 서버모듈 및 api 서비스 모듈 모두 eureka discovery client로 지정되어 있으므로 client들이 등록될 수 있도록 eureka 서버를 가장 먼저 실행해주세요.
2. config store server 실행
    - micro service 모듈들은 config server를 통해 application 설정 정보를 제공 받습니다. 
3. gateway server 실행
    - 모든 api 요청은 gateway 서버를 통합니다.
4. auth-server 실행
    - microservice 자원에 접근하려면 auth server를 통해 oauth 인증이 선행 되어야 합니다.
5. 개발 담당하는 microservice 모듈 실행
   (각 microservice 모듈은 프로필을 실행 옵션에 추가하여 실행 local, dev, prod)


## 요청방법

~~아직 로그인 작업이 이루어 지지 않았으므로, 임시로 요청 및 응답 확인을 위해 Postman 과 같은 툴을 이용해주세요.~~

2021-08-24

springdoc openapi 작업이 완료되었습니다. gateway 서버 실행 주소/swagger-ui.html 을 통해 api 확인 및 실행 가능합니다.


## 게이트웨이 (Spring Eureka)
![gateway 접속 화면](D:\Dev\IDEA_WORK\std-16-cld-app\etc\gateway.png)

## actuator
모듈 core를 제외한 모든 어플리케이션은 actuator 의존성을 갖습니다.

actuator를 사용하면 현재 어플리케이션의 다양한 정보를 모니터링 할 수 있습니다.

[참고링크](https://www.popit.kr/spring-actuator-%EA%B8%B0%EC%B4%88-%EC%84%A4%EC%A0%95-intellij-%ED%99%9C%EC%9A%A9%ED%95%98%EA%B8%B0/)

![/actuator 요청 시 응답 화면](D:\Dev\IDEA_WORK\std-16-cld-app\etc\actuator.png)

## 추가 적용 필요 및 검토 사항.

- actuator 필요한 endpoint 검토 및 접근 설정
- Resilience4J (장애 발생 시 전체에 영향이 끼치지 않도록 문제가 있는 마이크로서비스로의 트래픽을 차단하여 전체서비스가 느려지거나 중단되는것을 미리 방지)
- Sleuth (분산된 마이크로서비스간의 트래픽을 추적하여 문제를 사전에 방지하거나 해결)

## 웹페이지 

[mes user web docs](mes-user-web/mes-user-web.md)

[mes admin web docs](mes-admin-web/mes-admin-web.md)


