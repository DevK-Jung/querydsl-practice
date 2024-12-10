package com.example.querydlspractice.member.repository;

import com.example.querydlspractice.TestDataUtil;
import com.example.querydlspractice.dto.MemberSearchCondition;
import com.example.querydlspractice.dto.MemberTeamDto;
import com.example.querydlspractice.member.entity.Member;
import com.example.querydlspractice.member.entity.QMember;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void basicTest() {
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> all = memberRepository.findAll();
        assertThat(all).containsExactly(member);

        List<Member> result = memberRepository.findByUsername("member1");
        assertThat(result).containsExactly(member);

    }

    @Test
    public void searchTest() {
        TestDataUtil.setupTestData(em);

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<MemberTeamDto> results = memberRepository.search(condition);

        assertThat(results).extracting("username").containsExactly("member4");
    }

    @Test
    public void searchPage() {
        TestDataUtil.setupTestData(em);

        MemberSearchCondition condition = new MemberSearchCondition();
        PageRequest pageRequest = PageRequest.of(0, 3);

        Page<MemberTeamDto> page = memberRepository.searchPage(condition, pageRequest);

        assertThat(page.getSize()).isEqualTo(3);
        assertThat(page.getContent()).extracting("username").containsExactly("member1", "member2", "member3");
    }

    @Test
    public void querydslPredicateExecutorTest() {
        TestDataUtil.setupTestData(em);

        QMember member = QMember.member;
        Iterable<Member> result = memberRepository.findAll(member.age.between(20, 40).and(member.username.eq("member1")));
        for (Member findMember : result) {
            System.out.println("findMember = " + findMember);
        }
    }

}