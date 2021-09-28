# eureka discovery server

Eureka는 마이크로 서비스 기반의 아키텍처의 핵심 원칙 중 하나인 Service Discovery의 역할을 수행합니다. 

MSA에서는 Service의 IP와 Port가 일정하지 않고 지속적을 변화합니다. 그렇기 때문에 Client에 Service의 정보를 수동으로 입력하는 것은 한계가 분명합니다. 

Service Discovery란 이런 MSA의 상황에 적합합니다.

## 실행 프로파일

없음

## 기타사항.

- localhost:실행포트 접속해보면 연결 된 클라이언트들을 확인할 수 있는 대시보드 존재
- spring actuator 적용으로 actuator 통한 어플리케이션 모니터링가능.
