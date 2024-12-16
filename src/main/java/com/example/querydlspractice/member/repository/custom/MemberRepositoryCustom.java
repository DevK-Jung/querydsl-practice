package com.example.querydlspractice.member.repository.custom;

import com.example.querydlspractice.dto.MemberSearchCondition;
import com.example.querydlspractice.dto.MemberTeamDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberTeamDto> search(MemberSearchCondition condition);
    Page<MemberTeamDto> searchPage(MemberSearchCondition condition, Pageable pageable);
    Page<MemberTeamDto> searchPageUpgradeCountQuery(MemberSearchCondition condition, Pageable pageable);
}
