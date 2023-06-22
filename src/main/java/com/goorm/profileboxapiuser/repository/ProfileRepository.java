package com.goorm.profileboxapiuser.repository;
import com.goorm.profileboxcomm.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Override
    Page<Profile> findAll(@Param("pageable") Pageable pageable);

    Profile findProfileByProfileId(@Param("profileId") Long profileId);
}
