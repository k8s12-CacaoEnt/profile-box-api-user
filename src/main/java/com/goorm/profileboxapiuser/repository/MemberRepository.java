package com.goorm.profileboxapiuser.repository;

import com.goorm.profileboxcomm.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    @Override
    Page<MemberEntity> findAll(@Param("pageable") Pageable pageable);

    MemberEntity findMemberByMemberId(@Param("memberId") Long memberId);
}
