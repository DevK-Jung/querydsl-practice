package com.example.querydlspractice.middle;

import com.example.querydlspractice.TestDataUtil;
import com.example.querydlspractice.member.entity.QMember;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.querydlspractice.member.entity.QMember.member;

@Transactional
@SpringBootTest
public class SqlFunctionTest {
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
    public void sqlFunctionReplace() {
        List<String> result = queryFactory
                .select(
                        Expressions.stringTemplate(
                                "function('replace', {0}, {1}, {2})",
                                member.username, "member", "M"
                        )
                )
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void sqlFunctionUpper() {
        List<String> result = queryFactory
                .select(
//                        Expressions.stringTemplate(
//                                "function('upper', {0})",
//                                member.username
//                        )
                        member.username.upper()
                )
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }
}
