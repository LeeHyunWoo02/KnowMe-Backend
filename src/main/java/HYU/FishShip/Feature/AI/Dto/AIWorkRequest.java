package HYU.FishShip.Feature.AI.Dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIWorkRequest {
    
    private Long analysisId;           // 작업 고유 ID
    private String taskType;         // 작업 타입 ("ANALYZE", "RECOMEND_POST", "RECOMMEND_EXTERNAL")
    private Long userId;           // 요청한 사용자 ID
    private Map<String, Object> parameters;  // 작업에 필요한 파라미터들
    private LocalDateTime requestedAt;  // 요청 시간
}
