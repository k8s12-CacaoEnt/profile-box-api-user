package com.goorm.profileboxapiuser.api;

import com.goorm.profileboxcomm.entity.Profile;
import com.goorm.profileboxcomm.dto.profile.request.CreateProfileRequestDto;
import com.goorm.profileboxcomm.dto.profile.request.SelectProfileListRequestDto;
import com.goorm.profileboxcomm.dto.profile.response.SelectProfileResponseDto;
import com.goorm.profileboxapiuser.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ProfileApiController {
    private final ProfileService profileService;

    @GetMapping("/profile")
    public List<SelectProfileResponseDto> getProfiles(@ModelAttribute SelectProfileListRequestDto requestDto) {
        Page<Profile> profiles = profileService.getAllProfile(requestDto);
        List<SelectProfileResponseDto> result = profiles.stream()
                .map(o -> new SelectProfileResponseDto(o))
                .collect(toList());
        return result;
    }

    @GetMapping("/profile/{profileId}")
    public SelectProfileResponseDto getProfile(@PathVariable String profileId){
        Profile profile = profileService.getProfileByProfileId(profileId);
        return profile != null ? new SelectProfileResponseDto(profile) : null;
    }

    @PostMapping("/profile")
    public String addProfile(@RequestPart(value = "data") CreateProfileRequestDto profileDto,
                             @RequestPart(value = "images") List<MultipartFile> imageFiles,
                             @RequestPart(value = "videos") List<MultipartFile> videoFiles) {

        profileService.addProfile(profileDto, imageFiles, videoFiles);
        return "";
    }
}
