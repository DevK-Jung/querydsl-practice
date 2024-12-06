package com.example.querydlspractice.member.controller;

import com.example.querydlspractice.dto.MemberSearchCondition;
import com.example.querydlspractice.dto.MemberTeamDto;
import com.example.querydlspractice.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberJpaRepository memberJpaRepository;

    @GetMapping
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return memberJpaRepository.searchByBuilder(condition);
    }
}
