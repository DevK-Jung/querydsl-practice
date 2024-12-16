package com.example.querydlspractice.member.repository;

import com.example.querydlspractice.dto.MemberSearchCondition;
import com.example.querydlspractice.dto.MemberTeamDto;
import com.example.querydlspractice.dto.QMemberTeamDto;
import com.example.querydlspractice.member.entity.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.querydlspractice.member.entity.QMember.member;
import static com.example.querydlspractice.team.entity.QTeam.team;
import static org.springframework.util.StringUtils.hasText;

/**
 * EntityManager 를 활용한 순수 JPA 활용
 */
@Repository
public class MemberJpaRepository {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public MemberJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em); // Bean 으로 만들어서 사용해도 됨
    }

    // 저장
    public void save(Member member) {
        em.persist(member);
    }

    // id로 조회
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    // 전체 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // querydsl 을 활용한 조회
    public List<Member> findAll_Querydsl() {
        return queryFactory
                .selectFrom(member)
                .fetch();
    }

    // jpql을 활용하여 username으로 조회
    public List<Member> findByUsername(String username) {
        return em.createQuery("select m from Member m where m.username=:username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    // querydsl 을 활용하여 username 으로 조회
    public List<Member> findByUsername_Querydsl(String username) {
        return queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq(username))
                .fetch();
    }

    // querydsl에서 BooleanBuilder 를 활용한 검색
    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition) {

        BooleanBuilder builder = new BooleanBuilder();

        if (hasText(condition.getUsername())) {
            builder.and(member.username.eq(condition.getUsername()));
        }

        if (hasText(condition.getTeamName())) {
            builder.and(team.name.eq(condition.getTeamName()));
        }

        if (condition.getAgeGoe() != null) {
            builder.and(member.age.goe(condition.getAgeGoe()));
        }

        if (condition.getAgeLoe() != null) {
            builder.and(member.age.loe(condition.getAgeLoe()));
        }

        return queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                )).from(member)
                .leftJoin(member.team, team)
                .where(builder)
                .fetch();
    }

    // BooleanExpressions을 활용하여 조건문 조합
    public List<MemberTeamDto> searchByBuilderExpression(MemberSearchCondition condition) {

        return queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                )).from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    private BooleanExpression usernameEq(String username) {
        return hasText(username) ? member.username.eq(username) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

    private BooleanExpression ageGoe(Integer ageLoe) {
        return ageLoe != null ? member.age.goe(ageLoe) : null;
    }

}
