package com.goorm.profileboxapiuser.repository;
import com.goorm.profileboxcomm.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    @Override
    Page<ProfileEntity> findAll(@Param("pageable") Pageable pageable);

    ProfileEntity findProfileByProfileId(@Param("profileId") Long profileId);
}
