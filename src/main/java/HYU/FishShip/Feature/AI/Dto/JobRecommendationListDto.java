package HYU.FishShip.Feature.AI.Dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class JobRecommendationListDto {
    private List<JobRecommendationDto> recommendations;
    
    /**
     * JSON 결과에서 JobRecommendationListDto를 생성하는 정적 메소드
     */
    public static JobRecommendationListDto fromJson(String jsonResult) {
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            // JSON 문자열이 null이거나 비어있는 경우 빈 리스트 반환
            if (jsonResult == null || jsonResult.trim().isEmpty()) {
                log.warn("JSON result is null or empty for job recommendations");
                return JobRecommendationListDto.builder()
                        .recommendations(Collections.emptyList())
                        .build();
            }
            
            // JSON이 배열 형태인지 객체 형태인지 확인
            if (jsonResult.trim().startsWith("[")) {
                // 직접 배열 형태인 경우
                List<JobRecommendationDto> recommendations = objectMapper.readValue(
                    jsonResult, new TypeReference<List<JobRecommendationDto>>() {}
                );
                return JobRecommendationListDto.builder()
                        .recommendations(recommendations)
                        .build();
            } else {
                // 객체 형태에서 recommendations 필드를 추출하는 경우
                JobRecommendationListDto result = objectMapper.readValue(jsonResult, JobRecommendationListDto.class);
                return result;
            }
            
        } catch (JsonProcessingException e) {
            log.error("Failed to parse job recommendation JSON: {}", jsonResult, e);
            return JobRecommendationListDto.builder()
                    .recommendations(Collections.emptyList())
                    .build();
        }
    }
}
