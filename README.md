# Practice QueryDSL

> [실전! Querydsl](https://www.inflearn.com/course/querydsl-%EC%8B%A4%EC%A0%84/dashboard) 강의를 보고 실습하며 작성한 코드입니다.

## 목차
- [개발 환경](#개발-환경)
- [프로젝트 특징](#프로젝트-특징)

## 개발 환경
- Spring Boot 3.3.4
- Java 17
- Gradle
- h2 DB
- JPQL
- QueryDSL
- JPA
- Spring Data JPA

## 프로젝트 특징
- 기본 문법 - src/test/java/com/example/querydlspractice/basic
  - Aggregation
  - Case when then
  - Join
  - Paging
  - Sort
  - SubQuery
  - Constants
  - Sql Function
- 중급 문법 - src/test/java/com/example/querydlspractice/middle
  - Projection
  - BooleanBuilder
  - BooleanExpression
  - Bulk query
- Sample Code
  - Entity (Member, Team)
  - Repository
    - 순수 JPA, JPQL, Querydsl 을 활용한 Repository
      - com.example.querydlspractice.member.repository.MemberJpaRepository
    - Spring Data JPA, Querydsl, CustomRepository
      - com.example.querydlspractice.member.repository.MemberRepository
    - QuerydslRepositorySupport 활용
      - com.example.querydlspractice.member.repository.querydslSupport