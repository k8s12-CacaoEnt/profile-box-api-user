package com.goorm.profileboxcomm;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfileApiTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProfileService profileService;
//
//    @Test
//    public void testGetProfiles() throws Exception {
//        // ProfileService의 getAllProfile() 메서드를 호출할 때 반환할 모의 데이터
//        Page<Profile> mockProfiles = new PageImpl<>(Collections.singletonList(new Profile(1L, "John")));
//        when(profileService.getAllProfile(any(SelectProfileListRequestDto.class))).thenReturn(mockProfiles);
//
//        mockMvc.perform(get("/v1/profile")
//                        .param("offset", "0")
//                        .param("limit", "10")
//                        .param("sortKey", "name"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.resultType").value("SUCCESS"))
//                .andExpect(jsonPath("$.message").value("프로필 리스트 조회"))
//                .andExpect(jsonPath("$.data.length()").value(1))
//                .andExpect(jsonPath("$.data[0].id").value(1))
//                .andExpect(jsonPath("$.data[0].name").value("John"));
//
//        verify(profileService, times(1)).getAllProfile(any(SelectProfileListRequestDto.class));
//    }
//
//    @Test
//    public void testGetProfile() throws Exception {
//        // ProfileService의 getProfileByProfileId() 메서드를 호출할 때 반환할 모의 데이터
//        Profile mockProfile = new Profile(1L, "John");
//        when(profileService.getProfileByProfileId(1L)).thenReturn(mockProfile);
//
//        mockMvc.perform(get("/v1/profile/{profileId}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.resultType").value("SUCCESS"))
//                .andExpect(jsonPath("$.message").value("프로필 조회"))
//                .andExpect(jsonPath("$.data.id").value(1))
//                .andExpect(jsonPath("$.data.name").value("John"));
//
//        verify(profileService, times(1)).getProfileByProfileId(1L);
//    }
//
//    @Test
//    public void testAddProfile() throws Exception {
//        // ProfileService의 addProfile() 메서드를 호출할 때 반환할 모의 데이터
//        Profile mockProfile = new Profile(1L, "John");
//        when(profileService.addProfile(any(CreateProfileRequestDto.class), anyList(), anyList())).thenReturn(mockProfile);
//
//        MockMultipartFile dataFile = new MockMultipartFile("data", "", "application/json", "{\"name\":\"John\"}".getBytes());
//        MockMultipartFile imageFile = new MockMultipartFile("images", "image.jpg", "image/jpeg", "image".getBytes());
//
//        mockMvc.perform(multipart("/v1/profile")
//                        .file(dataFile)
//                        .file(imageFile))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.resultType").value("SUCCESS"))
//                .andExpect(jsonPath("$.message").value("프로필 등록"))
//                .andExpect(jsonPath("$.data.id").value(1))
//                .andExpect(jsonPath("$.data.name").value("John"));
//
//        verify(profileService, times(1)).addProfile(any(CreateProfileRequestDto.class), anyList(), anyList());
//    }
//
//    @Test
//    public void testUpdateProfile() throws Exception {
//        // ProfileService의 updateProfile() 메서드를 호출할 때 반환할 모의 데이터
//        Profile mockProfile = new Profile(1L, "John");
//        when(profileService.updateProfile(1L, any(CreateProfileRequestDto.class))).thenReturn(mockProfile);
//
//        MockMultipartFile dataFile = new MockMultipartFile("data", "", "application/json", "{\"name\":\"John\"}".getBytes());
//
//        mockMvc.perform(patch("/v1/profile/{profileId}", 1L)
//                        .file(dataFile))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.resultType").value("SUCCESS"))
//                .andExpect(jsonPath("$.message").value("프로필 수정"))
//                .andExpect(jsonPath("$.data.id").value(1))
//                .andExpect(jsonPath("$.data.name").value("John"));
//
//        verify(profileService, times(1)).updateProfile(1L, any(CreateProfileRequestDto.class));
//    }
//
//    @Test
//    public void testDeleteProfile() throws Exception {
//        mockMvc.perform(get("/v1/profile/{profileId}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.resultType").value("SUCCESS"))
//                .andExpect(jsonPath("$.message").value("프로필 삭제"))
//                .andExpect(jsonPath("$.data").isEmpty());
//
//        verify(profileService, times(1)).deleteProfile(1L);
//    }
}
