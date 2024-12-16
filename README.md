# Practice QueryDSL

> [실전! Querydsl](https://www.inflearn.com/course/querydsl-%EC%8B%A4%EC%A0%84/dashboard) 강의를 실습하며 작성한 코드입니다. QueryDSL의 기본부터 중급 문법까지 다양한 기능을 익히고 활용해 보았습니다.

## 목차
- [개발 환경](#개발-환경)
- [프로젝트 특징](#프로젝트-특징)

## 개발 환경
- Spring Boot: 3.3.4
- Java: 17
- 빌드 도구: Gradle
- 데이터베이스: H2
- ORM 및 쿼리 도구: JPA, Spring Data JPA, QueryDSL, JPQL

## 프로젝트 특징
### 1. 기본 문법 실습
- 위치: src/test/java/com/example/querydlspractice/basic
- 주요 실습 내용:
  - Aggregation (집계 함수)
  - Case when then (조건문)
  - Join (조인)
  - Paging (페이징 처리)
  - Sort (정렬)
  - SubQuery (서브쿼리)
  - Constants (상수 처리)
  - SQL Function (SQL 함수 호출)

### 2. 중급 문법 실습
- 위치: src/test/java/com/example/querydlspractice/middle
- 주요 실습 내용:
  - Projection (필드 선택 및 매핑)
  - BooleanBuilder (동적 조건 생성)
  - BooleanExpression (복합 조건 처리)
  - Bulk Query (벌크 쿼리)

### 3. 샘플 코드 구성
- Entity
  - Member
  - Team
- Repository 구현 방식
  - 순수 JPA, JPQL, QueryDSL을 활용한 레포지토리
    - com.example.querydlspractice.member.repository.MemberJpaRepository
  - Spring Data JPA와 QueryDSL Custom Repository
    - com.example.querydlspractice.member.repository.MemberRepository
  - QuerydslRepositorySupport를 활용한 구현
    - com.example.querydlspractice.member.repository.querydslSupport