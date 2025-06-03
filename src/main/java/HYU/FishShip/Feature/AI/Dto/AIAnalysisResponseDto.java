package HYU.FishShip.Feature.AI.Dto;

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
public class AIAnalysisResponseDto {
    private Long analysisId; // 분석 ID
    private int activitiesCount; // 분석된 활동 수
    private String message;
}
