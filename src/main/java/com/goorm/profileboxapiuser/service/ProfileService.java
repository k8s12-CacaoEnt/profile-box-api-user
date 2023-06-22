package com.goorm.profileboxapiuser.service;

import com.goorm.profileboxcomm.utils.FileHandler;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.entity.Profile;
import com.goorm.profileboxcomm.dto.image.request.CreateImageRequestDto;
import com.goorm.profileboxcomm.dto.profile.request.CreateProfileRequestDto;
import com.goorm.profileboxcomm.dto.profile.request.SelectProfileListRequestDto;
import com.goorm.profileboxcomm.dto.video.request.CreateVideoRequestDto;
import com.goorm.profileboxapiuser.repository.MemberRepository;
import com.goorm.profileboxapiuser.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final FileHandler fileHandler;

    public Page<Profile> getAllProfile(SelectProfileListRequestDto requestDto) {
        int offset = requestDto.getOffset();
        int limit = requestDto.getLimit();
        String sortKey = requestDto.getSortKey();
        return profileRepository.findAll(PageRequest.of(offset, limit, Sort.by(sortKey)));
    }

    public Profile getProfileByProfileId(String profileId) {
        return profileRepository.findProfileByProfileId(Long.parseLong(profileId));
    }

    @Transactional
    public void addProfile(CreateProfileRequestDto profileDto, List<MultipartFile> images, List<MultipartFile> videos) {
        Member member = memberRepository.findMemberByMemberId(Long.parseLong(profileDto.getMemberId()));
        // 유효성 체크 먼저

        // 이미지, 동영상 처리 후 profileDto 로 넣어줄까
        if(images != null & images.size() > 1){
            List<CreateImageRequestDto> imageDtoList = images.stream()
                    .map(o -> fileHandler.imageWrite(o))
                    .collect(toList());
            profileDto.setImages(imageDtoList);
        }

        if(videos != null & videos.size() > 1){
            List<CreateVideoRequestDto> videoDtoList = videos.stream()
                    .map(o -> fileHandler.videoWrite(o))
                    .collect(toList());
            profileDto.setVideos(videoDtoList);
        }

        // 프로필 데이터 생성
        Profile profile = Profile.createProfile(profileDto, member);
        profileRepository.save(profile);
        System.out.println("teset");
//        // 프로필 저장
//        Profile addProfile = profileRepository.save(profile);
    }


//
//    public Profile getProfileById(Long id) {
//        return profileRepository.findById(id).orElse(null);
//    }
//
//    public Profile saveProfile(Profile profile) {
//        return profileRepository.save(profile);
//    }

//    public void deleteProfile(Long id) {
//        profileRepository.deleteById(id);
//    }
}
