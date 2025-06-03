package HYU.FishShip.Feature.AI.Dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class PortfolioAnalysisDto {
    private Long userId;
    private String analysisResult;
    private String recommendations;
    private String strengths;
    private String areasForImprovement;
    
    /**
     * JSON 결과에서 PortfolioAnalysisDto를 생성하는 정적 메소드
     */
    public static PortfolioAnalysisDto fromJson(String jsonResult) {
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            // JSON 문자열이 null이거나 비어있는 경우 기본값 반환
            if (jsonResult == null || jsonResult.trim().isEmpty()) {
                log.warn("JSON result is null or empty for portfolio analysis");
                return PortfolioAnalysisDto.builder()
                        .analysisResult("분석 결과를 가져올 수 없습니다.")
                        .build();
            }
            
            // JSON 객체로 파싱
            JsonNode rootNode = objectMapper.readTree(jsonResult);
            
            // JSON 구조에 따라 필드 매핑
            PortfolioAnalysisDto.PortfolioAnalysisDtoBuilder builder = PortfolioAnalysisDto.builder();
            
            // 다양한 JSON 구조에 대응
            if (rootNode.has("analysisResult")) {
                builder.analysisResult(rootNode.get("analysisResult").asText());
            } else if (rootNode.has("analysis")) {
                builder.analysisResult(rootNode.get("analysis").asText());
            } else if (rootNode.has("result")) {
                builder.analysisResult(rootNode.get("result").asText());
            } else {
                // 전체 JSON을 문자열로 저장
                builder.analysisResult(jsonResult);
            }
            
            if (rootNode.has("recommendations")) {
                builder.recommendations(rootNode.get("recommendations").asText());
            }
            
            if (rootNode.has("strengths")) {
                builder.strengths(rootNode.get("strengths").asText());
            }
            
            if (rootNode.has("areasForImprovement")) {
                builder.areasForImprovement(rootNode.get("areasForImprovement").asText());
            } else if (rootNode.has("improvements")) {
                builder.areasForImprovement(rootNode.get("improvements").asText());
            }
            
            if (rootNode.has("userId")) {
                builder.userId(rootNode.get("userId").asLong());
            }
            
            return builder.build();
            
        } catch (JsonProcessingException e) {
            log.error("Failed to parse portfolio analysis JSON: {}", jsonResult, e);
            return PortfolioAnalysisDto.builder()
                    .analysisResult("분석 결과 파싱 중 오류가 발생했습니다: " + jsonResult)
                    .build();
        }
    }
}
