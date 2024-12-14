package com.example.querydlspractice.utils;

import com.example.querydlspractice.member.entity.Member;
import com.example.querydlspractice.team.entity.Team;
import jakarta.persistence.EntityManager;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestDataUtil {
    public void setupTestData(EntityManager em) {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }
}
