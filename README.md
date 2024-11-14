



## **`STEP 05`**
- 시나리오 선정 및 프로젝트 Milestone 제출 : [마일스톤](https://github.com/users/riley-hhp/projects/1/views/1)
- 시나리오 요구사항 별 분석 자료 제출
- 시퀀스 다이어그램 : [시퀀스 다이어그램](docs/SEQUENCE.md)


## **`STEP 06`**
- ERD 설계 자료 제출
- API 명세 및 Mock API 작성
  ### [ERD](docs/ERD.md)
  ### [API 명세](docs/API.md)
  ###  Mock API : `ConcertReservationController.java`
  
  ### 패키지 구조
  ```json
  src
   └── main
       └── java
           └── io.hhplus.concert
              └── app
                  ├── application
                  ├── domain
                  ├── infra
                  └── api
              └── config
                   
  ```
  
  ### 기술 스택
  - JDK17
    - Spring Boot
    - Spring Data JPA
    - H2


## **`STEP 07`**
### [API Swagger](docs/SWAGGER.png)


## **`STEP 08`**
비즈니스 Usecase 개발 및 통합 테스트 작성


## **`STEP 09`**
비즈니스 별 발생할 수 있는 에러 코드 정의 및 관리 체계 구축
프레임워크별 글로벌 에러 핸들러를 통해 예외 로깅 및 응답 처리 핸들러 구현
시스템 성격에 적합하게 Filter, Interceptor 를 활용해 기능의 관점을 분리하여 개선
모든 API 가 정상적으로 기능을 제공하도록 완성


## **`STEP 10`**
- 시나리오별 동시성 통합 테스트 작성
- [Chapter 2 회고록](docs%2FCH02.md) 작성


## **`STEP 11`**
- 나의 시나리오에서 발생할 수 있는 동시성 이슈에 대해 파악하고 가능한 동시성 제어 방식들을 도입해보고 각각의 장단점을 파악한 내용을 정리 제출
  - [동시성_보고서.md](docs%2F%EB%8F%99%EC%8B%9C%EC%84%B1_%EB%B3%B4%EA%B3%A0%EC%84%9C.md)


## **`STEP 12`**
- **DB Lock 을 활용한 동시성 제어 방식** 에서 해당 비즈니스 로직에서 적합하다고 판단하여 차용한 동시성 제어 방식을 구현하여 비즈니스 로직에 적용하고, 통합테스트 등으로 이를 검증하는 코드 작성 및 제출


## **`STEP 13_기본`**
- 조회가 오래 걸리는 쿼리에 대한 캐싱, 혹은 Redis 를 이용한 로직 이관을 통해 성능 개선할 수 있는 로직을 분석하고 이를 합리적인 이유와 함께 정리한 문서 제출


## **`STEP 14_심화`**
- **콘서트 예약 시나리오(대기열 인원수 제한 없음)**
  - 대기열 구현에 대한 설계를 진행하고, 설계한 내용과 부합하도록 적절하게 동작하는 대기열을 구현하여 제출

## **`STEP 15_기본`**

- 나의 시나리오에서 수행하는 쿼리들을 수집해보고, 필요하다고 판단되는 인덱스를 추가하고 쿼리의 성능개선 정도를 작성하여 제출
  - 자주 조회하는 쿼리, 복잡한 쿼리 파악
  - Index 추가 전후 Explain 비교 ( [DB_INDEX.md](docs%2FDB_INDEX.md) )