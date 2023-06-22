package com.goorm.profileboxapiuser.repository;

import com.goorm.profileboxcomm.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Override
    Page<Member> findAll(@Param("pageable") Pageable pageable);

    Member findMemberByMemberId(@Param("memberId") Long memberId);
}
