package com.aiyangniu.gate.service.impl;

import com.aiyangniu.entity.model.pojo.ums.UmsMember;
import com.aiyangniu.gate.domain.MemberReadHistory;
import com.aiyangniu.gate.repository.MemberReadHistoryRepository;
import com.aiyangniu.gate.service.MemberReadHistoryService;
import com.aiyangniu.gate.service.UmsMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 会员浏览记录管理实现类
 *
 * @author lzq
 * @date 2024/02/05
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MemberReadHistoryServiceImpl implements MemberReadHistoryService {

    private final UmsMemberService umsMemberService;
    private final MemberReadHistoryRepository memberReadHistoryRepository;

    @Override
    public int create(MemberReadHistory memberReadHistory) {
        UmsMember member = umsMemberService.getCurrentMember();
        memberReadHistory.setMemberId(member.getId());
        memberReadHistory.setMemberNickname(member.getNickname());
        memberReadHistory.setMemberIcon(member.getIcon());
        memberReadHistory.setId(null);
        memberReadHistory.setCreateTime(new Date());
        memberReadHistoryRepository.save(memberReadHistory);
        return 1;
    }

    @Override
    public int delete(List<String> ids) {
        List<MemberReadHistory> deleteList = new ArrayList<>();
        ids.forEach(e -> {
            MemberReadHistory memberReadHistory = new MemberReadHistory();
            memberReadHistory.setId(e);
            deleteList.add(memberReadHistory);
        });
        memberReadHistoryRepository.deleteAll(deleteList);
        return ids.size();
    }

    @Override
    public Page<MemberReadHistory> list(Integer pageNum, Integer pageSize) {
        UmsMember member = umsMemberService.getCurrentMember();
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        return memberReadHistoryRepository.findByMemberIdOrderByCreateTimeDesc(member.getId(), pageable);
    }

    @Override
    public void clear() {
        UmsMember member = umsMemberService.getCurrentMember();
        memberReadHistoryRepository.deleteAllByMemberId(member.getId());
    }
}
