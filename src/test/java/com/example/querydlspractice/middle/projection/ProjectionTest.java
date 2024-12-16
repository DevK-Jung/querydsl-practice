package com.example.querydlspractice.middle.projection;

import com.example.querydlspractice.utils.TestDataUtil;
import com.querydsl.core.Tuple;
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
public class ProjectionTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() { // 테스트 케이스 실행전 테스트 데이터 세팅
        queryFactory = new JPAQueryFactory(em);
        TestDataUtil.setupTestData(em);
    }

    @Test
    public void simpleProjection() {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void tupleProjection() {
        List<Tuple> tuple = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple1 : tuple) {
            String username = tuple1.get(member.username);
            Integer age = tuple1.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }

}
