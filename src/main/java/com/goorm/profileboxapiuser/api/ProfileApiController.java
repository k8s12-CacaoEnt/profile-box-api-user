package com.goorm.profileboxapiuser.api;

import com.goorm.profileboxapiuser.service.ProfileService;
import com.goorm.profileboxcomm.dto.profile.request.CreateProfileRequestDto;
import com.goorm.profileboxcomm.dto.profile.request.SelectProfileListRequestDto;
import com.goorm.profileboxcomm.dto.profile.response.SelectProfileResponseDto;
import com.goorm.profileboxcomm.entity.Profile;
import com.goorm.profileboxcomm.exception.ApiException;
import com.goorm.profileboxcomm.exception.ExceptionEnum;
import com.goorm.profileboxcomm.response.ApiResult;
import com.goorm.profileboxcomm.response.ApiResultType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
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
    public ApiResult getProfiles(@ModelAttribute SelectProfileListRequestDto requestDto) {
        try{
            Page<Profile> profiles = profileService.getAllProfile(requestDto);
            List<SelectProfileResponseDto> result = profiles.stream()
                    .map(o -> new SelectProfileResponseDto(o))
                    .collect(toList());
            return ApiResult.getResult(ApiResultType.SUCCESS, "프로필 리스트 조회", result);
        }catch (Exception e){
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }
    }

    @GetMapping("/profile/{profileId}")
    public ApiResult getProfile(@PathVariable Long profileId){
        try{
            Profile profile = profileService.getProfileByProfileId(profileId);
            return ApiResult.getResult(ApiResultType.SUCCESS, "프로필 조회", profile);
        }catch (Exception e){
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }
    }

    @PostMapping("/profile")
    public ApiResult addProfile(@Valid @RequestPart(value = "data") CreateProfileRequestDto profileDto,
                                @Valid @Size(min = 1, max = 5, message = "이미지는 최소1장/최대5장 첨부 가능합니다.")
                                @RequestPart(value = "images") List<@Valid MultipartFile> imageFiles,
                                @Size(max = 2, message = "동영상은 최대2개 첨부 가능합니다.")
                                @RequestPart(value = "videos") List<MultipartFile> videoFiles) {
        try{
            if (imageFiles.isEmpty()) {
                throw new ApiException(ExceptionEnum.INVALID_REQUEST);
            }
            Profile profile = profileService.addProfile(profileDto, imageFiles, videoFiles);
            return ApiResult.getResult(ApiResultType.SUCCESS, "프로필 등록", profile);
        }catch (Exception e){
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }
    }


    @PatchMapping("/profile/{profileId}")
    public ApiResult updateProfile(@PathVariable Long profileId, @Valid @RequestPart(value = "data") CreateProfileRequestDto profileDto){
        Profile profile = profileService.updateProfile(profileId, profileDto);
        return ApiResult.getResult(ApiResultType.SUCCESS, "프로필 수정", profile);
    }


    @GetMapping("/profile/{profileId}")
    public ApiResult deleteProfile(@PathVariable Long profileId){
        try{
            profileService.deleteProfile(profileId);
            return ApiResult.getResult(ApiResultType.SUCCESS, "프로필 삭제", null);
        }catch (Exception e){
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }
    }
}
