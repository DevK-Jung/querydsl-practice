package com.example.querydlspractice.middle;

import com.example.querydlspractice.utils.TestDataUtil;
import com.querydsl.core.types.dsl.Expressions;
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

/**
 * SQL Function
 */
@Transactional
@SpringBootTest
public class SqlFunctionTest {
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() { // 테스트 케이스 실행전 테스트 데이터 세팅
        queryFactory = new JPAQueryFactory(em);
        TestDataUtil.setupTestData(em);
    }

    @Test
    @DisplayName("replace")
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
    @DisplayName("upper")
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
