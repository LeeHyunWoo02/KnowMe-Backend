package HYU.FishShip.Feature.AI.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRecommendationDto {
    private Long postId;
    private String category;
    private String title;
    private String company;
    private String location;
    private String employmentType;
    private String startDate;
    private String endDate;
    private String description;
    private String jobTitle;
    private Integer experience;
    private String education;
    private List<String> requirements;
    private List<String> benefits;
    private List<AttachmentDto> attachments;
    private String createdAt;
    private String updatedAt;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttachmentDto {
        private String fileName;
        private String url;
    }
}
