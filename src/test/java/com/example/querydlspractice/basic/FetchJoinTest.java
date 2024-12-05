package com.example.querydlspractice.basic;

import com.example.querydlspractice.TestDataUtil;
import com.example.querydlspractice.member.entity.Member;
import com.example.querydlspractice.member.entity.QMember;
import com.example.querydlspractice.team.entity.QTeam;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.example.querydlspractice.member.entity.QMember.member;
import static com.example.querydlspractice.team.entity.QTeam.team;

@Transactional
@SpringBootTest
public class FetchJoinTest {
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        TestDataUtil.setupTestData(em);
    }

    @PersistenceUnit
    EntityManagerFactory emf; // EntityManager를 만드는 애

    // 패치 조인이 없을때
    @Test
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

    // 패치 조인 사용
    @Test
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
