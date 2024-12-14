package com.example.querydlspractice.basic;

import com.example.querydlspractice.utils.TestDataUtil;
import com.example.querydlspractice.member.entity.Member;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.querydlspractice.member.entity.QMember.member;
import static com.example.querydlspractice.team.entity.QTeam.team;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Join 문
 */
@Transactional
@SpringBootTest
public class JoinTest {
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;
    @BeforeEach
    public void before() { // 테스트 케이스 실행전 테스트 데이터 세팅
        queryFactory = new JPAQueryFactory(em);
        TestDataUtil.setupTestData(em);
    }

    @Test
    @DisplayName("inner join")
    public void join() {

        List<Member> result = queryFactory
                .select(member)
                .from(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }

    @Test
    @DisplayName("thetaJoin")
    public void thetaJoin() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * 회원과 팀을 조인하면서, 팀 이름이 teamA 인 팀만 조인, 회원은 모두 조회
     * JPQL: select m from Member m left join m.team t on t.name = "teamA"
     */
    @Test
    @DisplayName("키 값이 아닌 항목으로 조인")
    public void joinOnFiltering() {

        // 아래 두 쿼리는 동일
//        List<Tuple> result = queryFactory
//                .select(member, team)
//                .from(member)
//                .join(member.team)
//                .on(team.name.eq("teamA"))
//                .fetch();

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .join(member.team)
                .where(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

    }

    /**
     * 연관 관계 없는 엔티티 외부 조인
     * 회원의 이름이 팀 이름과 같은 대상 외부 조인
     */
    @Test
    @DisplayName("연관 관계 없는 엔티티 외부 조인")
    public void joinOnNoRelation() {

        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
//                .leftJoin(member.team, team) // 이렇게 하면 아이디가 자동 조인됨
                .leftJoin(team)
                .on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

    }
}
