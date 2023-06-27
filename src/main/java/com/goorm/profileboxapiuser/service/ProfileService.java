package com.goorm.profileboxapiuser.service;

import com.goorm.profileboxcomm.dto.filmo.request.CreateFilmoRequestDto;
import com.goorm.profileboxcomm.dto.image.request.CreateImageRequestDto;
import com.goorm.profileboxcomm.dto.link.request.CreateLinkRequestDto;
import com.goorm.profileboxcomm.dto.profile.request.CreateProfileRequestDto;
import com.goorm.profileboxcomm.dto.profile.request.SelectProfileListRequestDto;
import com.goorm.profileboxcomm.dto.video.request.CreateVideoRequestDto;
import com.goorm.profileboxcomm.entity.*;
import com.goorm.profileboxcomm.exception.ExceptionEnum;
import com.goorm.profileboxcomm.repository.*;
import com.goorm.profileboxcomm.exception.ApiException;
import com.goorm.profileboxcomm.utils.FileHandler;
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
    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;
    private final FilmoRepository filmoRepository;
    private final LinkRepository linkRepository;

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
    public Long addProfile(CreateProfileRequestDto profileDto, List<MultipartFile> images, List<MultipartFile> videos) {
        Member member = memberRepository.findMemberByMemberId(profileDto.getMemberId());

        if (member == null) {
            throw new ApiException(ExceptionEnum.MEMBER_NOT_FOUND);
        }

        Profile profile = Profile.createProfile(profileDto, member);
        profileRepository.save(profile);

        if (images != null & images.size() > 1) {
            if (profileDto.getDefaultImageIdx() < 0 || profileDto.getDefaultImageIdx() > images.size()-1) {
                profileDto.setDefaultImageIdx(0);
            }

            List<CreateImageRequestDto> imageDtoList = images.stream()
                    .map(o -> fileHandler.imageWrite(o))
                    .collect(toList());
            for (int idx = 0; idx < imageDtoList.size(); idx++) {
                CreateImageRequestDto dto = imageDtoList.get(idx);
                Image image = Image.createImage(dto, profile);
                imageRepository.save(image);
                if(idx == profileDto.getDefaultImageIdx()){
                    profile.setDefaultImageId(image.getImgageId());
                }
            }
        }

        if (videos != null & videos.size() > 1) {
            List<CreateVideoRequestDto> videoDtoList = videos.stream()
                    .map(o -> fileHandler.videoWrite(o))
                    .collect(toList());
            for (CreateVideoRequestDto dto : videoDtoList) {
                Video video = Video.createVideo(dto, profile);
                videoRepository.save(video);
            }
        }

        if (profileDto.getFilmos() != null & profileDto.getFilmos().size() > 0) {
            for (CreateFilmoRequestDto dto : profileDto.getFilmos()) {
                Filmo filmo = Filmo.createFilmo(dto, profile);
                filmoRepository.save(filmo);
            }
        }

        if (profileDto.getLinks() != null & profileDto.getLinks().size() > 0) {
            for (CreateLinkRequestDto dto : profileDto.getLinks()) {
                Link link = Link.createLink(dto, profile);
                linkRepository.save(link);
            }
        }
        return profile.getProfileId();
    }
}
