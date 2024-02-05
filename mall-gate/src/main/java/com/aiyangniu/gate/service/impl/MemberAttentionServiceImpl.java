package com.aiyangniu.gate.service.impl;

import com.aiyangniu.entity.model.pojo.pms.PmsBrand;
import com.aiyangniu.entity.model.pojo.ums.UmsMember;
import com.aiyangniu.gate.domain.MemberBrandAttention;
import com.aiyangniu.gate.mapper.PmsBrandMapper;
import com.aiyangniu.gate.repository.MemberAttentionRepository;
import com.aiyangniu.gate.service.MemberAttentionService;
import com.aiyangniu.gate.service.UmsMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

/**
 * 会员品牌关注管理实现类
 *
 * @author lzq
 * @date 2024/02/04
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MemberAttentionServiceImpl implements MemberAttentionService {

    @Value("${mongo.insert.sqlEnable}")
    private Boolean sqlEnable;

    private final PmsBrandMapper brandMapper;
    private final MemberAttentionRepository memberAttentionRepository;
    private final UmsMemberService memberService;

    @Override
    public int add(MemberBrandAttention memberBrandAttention) {
        int count = 0;
        if(memberBrandAttention.getBrandId() == null){
            return 0;
        }
        UmsMember member = memberService.getCurrentMember();
        memberBrandAttention.setMemberId(member.getId());
        memberBrandAttention.setMemberNickname(member.getNickname());
        memberBrandAttention.setMemberIcon(member.getIcon());
        memberBrandAttention.setCreateTime(new Date());
        MemberBrandAttention findAttention = memberAttentionRepository.findByMemberIdAndBrandId(memberBrandAttention.getMemberId(), memberBrandAttention.getBrandId());
        if (findAttention == null) {
            if(sqlEnable){
                PmsBrand brand = brandMapper.selectById(memberBrandAttention.getBrandId());
                if(ObjectUtils.isEmpty(brand)){
                    return 0;
                }else{
                    memberBrandAttention.setBrandCity(null);
                    memberBrandAttention.setBrandName(brand.getName());
                    memberBrandAttention.setBrandLogo(brand.getLogo());
                }
            }
            memberAttentionRepository.save(memberBrandAttention);
            count = 1;
        }
        return count;
    }

    @Override
    public int delete(Long brandId) {
        UmsMember member = memberService.getCurrentMember();
        return memberAttentionRepository.deleteByMemberIdAndBrandId(member.getId(), brandId);
    }

    @Override
    public Page<MemberBrandAttention> list(Integer pageNum, Integer pageSize) {
        UmsMember member = memberService.getCurrentMember();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return memberAttentionRepository.findByMemberId(member.getId(), pageable);
    }

    @Override
    public MemberBrandAttention detail(Long brandId) {
        UmsMember member = memberService.getCurrentMember();
        return memberAttentionRepository.findByMemberIdAndBrandId(member.getId(), brandId);
    }

    @Override
    public void clear() {
        UmsMember member = memberService.getCurrentMember();
        memberAttentionRepository.deleteAllByMemberId(member.getId());
    }
}
