# 업데이트내역

## 2021-08-30

- api-doc path 수정

client 측에서 요청 시 http://{gateway-host}:{gateway-port}/api/{microservice-path}/{path} 형식으로 변경.

- 각 microservice에서는 swagger-ui 진입이 되지 않도록 변경
  http://{gateway-host}:{gateway-port}/swagger-ui.html 로만 swagger-ui 제공 되도록 변경

- prod 프로필(운영) 에서는 openapi 적용 제외 설정 추가

## 2021-08-24
- springdoc-openapi가 적용되었습니다. local 프로파일로 실행 기준 http:/localhost:9002/swagger-ui.html을 확인하세요.
![openapi 명세 페이지 접속 예시](D:\Dev\IDEA_WORK\std-16-cld-app\etc\swagger-ui.png "swagger-ui.html")
Authorization 버튼클 클릭하면 로그인 계정 및 Oauth Client 정보를 입력하여 인증토큰을 가져온 후 API를 테스트 할 수 있습니다.

- MapStruct 라이브러리를 적용하여, Entity <-> Form, DTO 변환 작업을 간단히 적용할 수 있도록 하였습니다.
해당 라이브러리의 자세한 설명은 링크로 대체합니다. https://huisam.tistory.com/entry/mapStruct

<pre>
    대부분의 API는 CRUD를 공통적으로 가지고 있고, 
    현재 우리는 아주 작은 서비스로 모든것을 쪼개는 방식으로 개발을 하기로 협의 하였습니다.
    해서 Service단과 Controller단에서 행해질 공통적인 CRUD항목들을 추상화 하여 적용하였습니다.
    이 과정에서 현재 구조상 각 API Microservice 모듈에서 DTO를 가지고 있고, Core 모듈에서 Entity를 가져와 쓰고 있는데
    Service를 Core모듈에 두면, Service의 리턴을 DTO타입으로 하기가 애매합니다... 그렇다고 DTO를 Core에 두는것도
    분리라는 관점에서 맞지 않는 것 같습니다. 해서 Entity와 Repository만을 Core에 남기고 Service 레이어는 각 모듈에서
    Core에 있는 기본 서비스 추상클래스를 상속받아 구현 시키는 방식으로 작성하였으니 의견 부탁드립니다.
</pre>