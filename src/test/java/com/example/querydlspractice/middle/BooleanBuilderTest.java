package com.example.querydlspractice.middle;

import com.example.querydlspractice.TestDataUtil;
import com.example.querydlspractice.member.entity.Member;
import com.example.querydlspractice.member.entity.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.querydlspractice.member.entity.QMember.member;

@Transactional
@SpringBootTest
public class BooleanBuilderTest {
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    // 테스트 케이스 실행전 테스트 데이터 세팅
    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        TestDataUtil.setupTestData(em);
    }

    @Test
    public void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> memberList = searchMember(usernameParam, ageParam);

        Assertions.assertThat(memberList.size())
                .isEqualTo(1);
    }

    private List<Member> searchMember(String usernameParam, Integer ageParam) {

        BooleanBuilder builder = new BooleanBuilder();

        if (usernameParam != null) {
            builder.and(member.username.eq(usernameParam));
        }
        if (ageParam != null) {
            builder.and(member.age.eq(ageParam));
        }

        return queryFactory
                .select(member)
                .from(member)
                .where(builder)
                .fetch();
    }
}
