package com.example.querydlspractice.basic;

import com.example.querydlspractice.utils.TestDataUtil;
import com.example.querydlspractice.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.querydlspractice.member.entity.QMember.member;

/**
 * 정렬
 */
@Transactional
@SpringBootTest
public class SortTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    // 테스트 케이스 실행전 테스트 데이터 세팅
    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        TestDataUtil.setupTestData(em);
    }


    /**
     * <h1>정렬 순서</h1>
     * <ul>
     *     <li>회원 나이 내림차순</li>
     *     <li>회원 이름 내림차순 - 단 이름이 없으면(null) 마지막에 출력</li>
     * </ul>
     */
    @Test
    @DisplayName("정렬 테스트")
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> results = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsFirst())
                .fetch();

        Member memberNull = results.get(0); // 이름이 null
        Member member5 = results.get(1);
        Member member6 = results.get(2);

        Assertions.assertThat(memberNull.getUsername()).isNull();
        Assertions.assertThat(member5.getUsername()).isEqualTo("member5");
        Assertions.assertThat(member6.getUsername()).isEqualTo("member6");
    }
}
