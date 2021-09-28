# config-store application

Spring Cloud Config 어플리케이션

서버의 중단 없이 설정내용을 변경/적용 가능함.

## 실행 프로파일

### native

classpath:/configs/*.yml 을 로드. (개발용)

### git

http://192.168.0.23/gndbiz/std16-config.git의 설정 파일들을 로드 (운영용)

## 기타사항.

- localhost:실행포트/config-name/profile 을 통해 설정내역을 정상적으로 가져오는지 확인 가능.
- spring actuator 적용으로 actuator 통한 어플리케이션 모니터링 가능.
- config store url:port/microservice-id/profile 요청을 통해 설정 파일 내용을 확인할 수 있음
- http basic 인증 추가 되었음. (gndbiz / gndbiz1101!)