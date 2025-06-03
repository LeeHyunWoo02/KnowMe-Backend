package HYU.FishShip.Feature.AI.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import HYU.FishShip.Feature.AI.Dto.AIAnalysisResultResponseDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import HYU.FishShip.Common.Config.RabbitMQConfig;
import HYU.FishShip.Core.Entity.AIAnalysis;
import HYU.FishShip.Core.Entity.Activity;
import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.AIAnalysisRepository;
import HYU.FishShip.Core.Repository.ActivityRepository;
import HYU.FishShip.Core.Repository.UserRepository;
import HYU.FishShip.Feature.AI.Dto.AIWorkRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIAnalysisService {

    private final AIAnalysisRepository aiAnalysisRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    /**
     * 포트폴리오 분석 요청
     */
    @Transactional
    public Pair<Long, Integer> requestPortfolioAnalysis(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        // AIAnalysis 엔티티 생성
        AIAnalysis analysis = AIAnalysis.builder()
                .analysis_type("포트폴리오")
                .created_at(LocalDateTime.now())
                .user(user)
                .build();

        AIAnalysis savedAnalysis = aiAnalysisRepository.save(analysis);

        // 사용자의 활동 데이터 수집
        List<Activity> activities = activityRepository.findByUserId(userId);
        int activityCount = activities.size();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("analysisId", savedAnalysis.getId());
        parameters.put("userId", userId);
        parameters.put("activities", activities.stream()
                .map(activity -> {
                    Map<String, Object> activityData = new HashMap<>();
                    activityData.put("title", activity.getTitle());
                    activityData.put("description", activity.getDescription());
                    activityData.put("content", activity.getContent());
                    activityData.put("tags", activity.getTags());
                    return activityData;
                })
                .toList());
        // 사용자의 교육
        parameters.put("educations", user.getEducations());

        // RabbitMQ로 분석 작업 전송
        submitAIWork("ANALYZE", savedAnalysis.getId(), userId, parameters);

        return Pair.of(savedAnalysis.getId(), activityCount);
    }

    /**
     * 채용공고 추천 요청
     */
    @Transactional
    public Pair<Long, Integer> requestJobRecommendation(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        // AIAnalysis 엔티티 생성
        AIAnalysis analysis = AIAnalysis.builder()
                .analysis_type("채용공고 추천")
                .created_at(LocalDateTime.now())
                .user(user)
                .build();

        AIAnalysis savedAnalysis = aiAnalysisRepository.save(analysis);

        // 사용자의 활동 데이터 수집
        List<Activity> activities = activityRepository.findByUserId(userId);
        int activityCount = activities.size();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("analysisId", savedAnalysis.getId());
        parameters.put("userId", userId);
        parameters.put("activities", activities.stream()
                .map(activity -> {
                    Map<String, Object> activityData = new HashMap<>();
                    activityData.put("title", activity.getTitle());
                    activityData.put("description", activity.getDescription());
                    activityData.put("content", activity.getContent());
                    activityData.put("tags", activity.getTags());
                    return activityData;
                })
                .toList());

        // 사용자의 교육, 프로젝트, 경력 정보도 추가
        parameters.put("educations", user.getEducations());
        parameters.put("projects", user.getProjects());
        parameters.put("experiences", user.getExperiences());

        // RabbitMQ로 추천 작업 전송
        submitAIWork("RECOMMEND_POST", savedAnalysis.getId(), userId, parameters);

        return Pair.of(savedAnalysis.getId(), activityCount);
    }

    /**
     * 대외활동 추천 요청
     */
    @Transactional
    public Pair<Long, Integer> requestExternalActivityRecommendation(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        // AIAnalysis 엔티티 생성
        AIAnalysis analysis = AIAnalysis.builder()
                .analysis_type("대외활동 추천")
                .created_at(LocalDateTime.now())
                .user(user)
                .build();

        AIAnalysis savedAnalysis = aiAnalysisRepository.save(analysis);

        // 사용자의 활동 데이터 수집
        List<Activity> activities = activityRepository.findByUserId(userId);
        int activityCount = activities.size();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("analysisId", savedAnalysis.getId());
        parameters.put("userId", userId);
        parameters.put("activities", activities.stream()
                .map(activity -> {
                    Map<String, Object> activityData = new HashMap<>();
                    activityData.put("title", activity.getTitle());
                    activityData.put("description", activity.getDescription());
                    activityData.put("content", activity.getContent());
                    activityData.put("tags", activity.getTags());
                    return activityData;
                })
                .toList());

        // 사용자의 교육, 프로젝트, 경력 정보도 추가
        parameters.put("educations", user.getEducations());
        parameters.put("projects", user.getProjects());
        parameters.put("experiences", user.getExperiences());

        // RabbitMQ로 추천 작업 전송
        submitAIWork("RECOMMEND_EXTERNAL", savedAnalysis.getId(), userId, parameters);

        return Pair.of(savedAnalysis.getId(), activityCount);
    }

    @Transactional
    public AIAnalysisResultResponseDto getAnalysis(Long analysisId) {
        AIAnalysis analysis = aiAnalysisRepository.findById(analysisId)
                .orElseThrow(() -> new IllegalArgumentException("해당 분석이 존재하지 않습니다."));

        return AIAnalysisResultResponseDto.builder()
                .resultSummary(analysis.getResult_summary())
                .build();
    }

    /**
     * AI 분석 결과 업데이트 (RabbitMQ 리스너에서 호출)
     */
    @Transactional
    public void updateAnalysisResult(Long analysisId, String resultSummary) {
        AIAnalysis analysis = aiAnalysisRepository.findById(analysisId)
                .orElseThrow(() -> new IllegalArgumentException("해당 분석이 존재하지 않습니다."));

        analysis.setCompleted_at(LocalDateTime.now());
        analysis.setResult_summary(resultSummary);
        aiAnalysisRepository.save(analysis);
    }

    private final RabbitTemplate rabbitTemplate;

    /**
     * AI 작업을 큐에 전송합니다.
     * 
     * @param taskType 작업 타입
     * @param userId 사용자 ID
     * @param parameters 작업 파라미터
     */
    public void submitAIWork(String taskType, Long userId, Long analysisId, Map<String, Object> parameters) {
        AIWorkRequest request = new AIWorkRequest(analysisId, taskType, userId, parameters, LocalDateTime.now());
        request.setAnalysisId(analysisId);
        
        try {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.AI_EXCHANGE,
                RabbitMQConfig.AI_WORK_ROUTING_KEY,
                request
            );
            
            log.info("AI work submitted - AnalysisId: {}, TaskType: {}, UserID: {}",
                    analysisId, taskType, userId);
        } catch (Exception e) {
            log.error("Failed to submit AI work - AnalysisId: {}, Error: {}", analysisId, e.getMessage());
            throw new RuntimeException("AI 작업 전송에 실패했습니다.", e);
        }
    }
}
