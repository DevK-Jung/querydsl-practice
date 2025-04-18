package com.example.querydlspractice.middle.projection;

import com.example.querydlspractice.dto.MemberDto;
import com.example.querydlspractice.dto.UserDto;
import com.example.querydlspractice.member.entity.QMember;
import com.example.querydlspractice.utils.TestDataUtil;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
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
 * DTO Projection
 */
@Transactional
@SpringBootTest
public class DtoProjectionTest {
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() { // 테스트 케이스 실행전 테스트 데이터 세팅
        queryFactory = new JPAQueryFactory(em);
        TestDataUtil.setupTestData(em);
    }

    @Test
    @DisplayName("JPQL 을 사용한 projection")
    public void findDtoByJPQL() { // 생성자 주입으로 생성 가능
        List<MemberDto> resultList = em.createQuery(
                        "select new com.example.querydlspractice.dto.MemberDto(m.username, m.age)" +
                                " from Member m",
                        MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : resultList) {
            System.out.println(memberDto);
        }
    }

    @Test
    @DisplayName("setter를 통한 주입")
    public void findDtoBySetter() {
        List<MemberDto> result = queryFactory
                .select(
                        Projections.bean(MemberDto.class,
                                member.username,
                                member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    @DisplayName("field 를 통한 주입") // getter setter 없어도 됨
    public void findDtoByField() {
        List<MemberDto> result = queryFactory
                .select(
                        Projections.fields(MemberDto.class,
                                member.username,
                                member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    @DisplayName("생성자 주입")
    public void findDtoByConstruct() {
        List<MemberDto> result = queryFactory
                .select(
                        Projections.constructor(MemberDto.class,
                                member.username,
                                member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    @DisplayName("field 및 서브쿼리 alias")
    public void findUserDto() {

        QMember memberSub = new QMember("memberSub");
        List<UserDto> userDtos = queryFactory
                .select(
                        Projections.fields(UserDto.class,
                                member.username.as("name"),
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(memberSub.age.max())
                                                .from(memberSub)
                                        , "age"))
                )
                .from(member)
                .fetch();

        for (UserDto userDto : userDtos) {
            System.out.println("userDto = " + userDto);
        }
    }

    @Test
    public void findUserDtoByConstruct() {

        QMember memberSub = new QMember("memberSub");
        List<UserDto> userDtos = queryFactory
                .select(
                        Projections.constructor(UserDto.class,
                                member.username,
                                JPAExpressions
                                        .select(memberSub.age.max())
                                        .from(memberSub)
                        ))
                .from(member)
                .fetch();

        for (UserDto userDto : userDtos) {
            System.out.println("userDto = " + userDto);
        }
    }
}
