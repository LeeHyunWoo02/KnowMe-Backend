package HYU.FishShip.Feature.AI.Service;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import HYU.FishShip.Common.Config.RabbitMQConfig;
import HYU.FishShip.Feature.AI.Dto.AIWorkResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AIResultListener {

    private final AIAnalysisService aiAnalysisService;

    /**
     * AI 작업 결과를 수신하여 처리합니다.
     * 
     * @param result AI 작업 결과
     */
    @RabbitListener(queues = RabbitMQConfig.AI_RESULT_QUEUE)
    public void handleAIResult(AIWorkResult result) {
        try {
            log.info("AI result received - TaskID: {}, TaskType: {}, Success: {}", 
                    result.getAnalysisId(), result.getTaskType(), result.isSuccess());

            if (result.isSuccess()) handleSuccessResult(result);
            else
                log.warn("AI task failed - TaskID: {}, Error: {}", 
                        result.getAnalysisId(), result.getErrorMessage());

        } catch (Exception e) {
            log.error("Error processing AI result - TaskID: {}, Error: {}", 
                    result.getAnalysisId(), e.getMessage(), e);
        }
    }

    /**
     * 성공한 AI 작업 결과를 처리합니다.
     * 
     * @param result 성공한 작업 결과
     */
    private void handleSuccessResult(AIWorkResult result) {
        log.info("Processing successful AI result - TaskID: {}, ProcessingTime: {}ms", 
                result.getAnalysisId(), result.getProcessingTimeMs());

        // 작업 타입에 따른 결과 처리
        switch (result.getTaskType()) {
            case "ANALYZE" -> handlePortfolioAnalysisResult(result);
            case "RECOMMEND_POST" -> handleJobRecommendationResult(result);
            case "RECOMMEND_EXTERNAL" -> handleExternalActivityRecommendationResult(result);
            default -> {
                log.warn("Unknown task type: {}", result.getTaskType());
            }
        }
    }
    /**
     * 포트폴리오 분석 결과를 처리합니다.
     */
    private void handlePortfolioAnalysisResult(AIWorkResult result) {
        log.info("Processing portfolio analysis result for task: {}", result.getAnalysisId());
        
        try {
            // 분석 ID 추출
            Map<String, Object> resultData = result.getResult();
            Long analysisId = Long.valueOf(resultData.get("analysisId").toString());
            String resultSummary = (String) resultData.get("resultSummary");
            
            // AIAnalysis 엔티티 업데이트
            aiAnalysisService.updateAnalysisResult(analysisId, resultSummary);
            
            log.info("Portfolio analysis completed for analysisId: {}", analysisId);
        } catch (Exception e) {
            log.error("Failed to process portfolio analysis result: {}", e.getMessage(), e);
        }
    }

    /**
     * 채용공고 추천 결과를 처리합니다.
     */
    private void handleJobRecommendationResult(AIWorkResult result) {
        log.info("Processing job recommendation result for task: {}", result.getAnalysisId());
        
        try {
            // 분석 ID 추출
            Map<String, Object> resultData = result.getResult();
            Long analysisId = Long.valueOf(resultData.get("analysisId").toString());
            String resultSummary = (String) resultData.get("resultSummary");
            
            // AIAnalysis 엔티티 업데이트
            aiAnalysisService.updateAnalysisResult(analysisId, resultSummary);
            
            log.info("Job recommendation completed for analysisId: {}", analysisId);
        } catch (Exception e) {
            log.error("Failed to process job recommendation result: {}", e.getMessage(), e);
        }
    }

    /**
     * 대외활동 추천 결과를 처리합니다.
     */
    private void handleExternalActivityRecommendationResult(AIWorkResult result) {
        log.info("Processing external activity recommendation result for task: {}", result.getAnalysisId());
        
        try {
            // 분석 ID 추출
            Map<String, Object> resultData = result.getResult();
            Long analysisId = Long.valueOf(resultData.get("analysisId").toString());
            String resultSummary = (String) resultData.get("resultSummary");
            
            // AIAnalysis 엔티티 업데이트
            aiAnalysisService.updateAnalysisResult(analysisId, resultSummary);
            
            log.info("External activity recommendation completed for analysisId: {}", analysisId);
        } catch (Exception e) {
            log.error("Failed to process external activity recommendation result: {}", e.getMessage(), e);
        }
    }
}
