package com.example.querydlspractice.basic;

import com.example.querydlspractice.utils.TestDataUtil;
import com.example.querydlspractice.member.entity.Member;
import com.example.querydlspractice.member.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.example.querydlspractice.member.entity.QMember.member;
import static com.example.querydlspractice.team.entity.QTeam.team;

/**
 * Fetch Join 사용
 */
@Transactional
@SpringBootTest
public class FetchJoinTest {
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @PersistenceUnit
    EntityManagerFactory emf; // EntityManager 를 만드는 객체, 해당 객체로 lazy Load 된 항목이 현재 load 됐는지 확인 가능

    @BeforeEach
    public void before() { // 테스트 케이스 실행전 테스트 데이터 세팅
        queryFactory = new JPAQueryFactory(em);
        TestDataUtil.setupTestData(em);
    }
    @Test
    @DisplayName("패치 조인이 없을때")
    public void fetchJoinNo() {
        // 패치 조인을 사용할때는 영속성 컨텍스트에있는걸 DB에 날려줘야 제대로 된 결과를 볼 수 있다.
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.username.eq("member1"))
                .fetchOne();

        // Team 이 Lazy Loading임 loading 이 된 상태인지 확인
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());

        Assertions.assertThat(loaded).as("페치 조인 미적용").isFalse();
    }

    @Test
    @DisplayName("패치 조인 사용")
    public void fetchJoinUse() {
        // 패치 조인을 사용할때는 영속성 컨텍스트에있는걸 DB에 날려줘야 제대로 된 결과를 볼 수 있다.
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .join(member.team, team).fetchJoin()
                .where(QMember.member.username.eq("member1"))
                .fetchOne();

        // Team 이 Lazy Loading임 loading 이 된 상태인지 확인
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());

        Assertions.assertThat(loaded).as("페치 조인 적용").isTrue();
    }
}
