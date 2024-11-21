package com.example.querydlspractice.paging;

import com.example.querydlspractice.TestDataUtil;
import com.example.querydlspractice.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.querydlspractice.member.entity.QMember.*;

@Transactional
@SpringBootTest
public class QueryDslPagingTest {
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;
    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        TestDataUtil.setupTestData(em);
    }

    @Test
    public void paging() {

        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        Assertions.assertThat(result.size())
                .isEqualTo(2);
    }
}
