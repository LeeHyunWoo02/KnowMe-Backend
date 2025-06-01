package HYU.FishShip.Feature.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class FindUserIdResponseDTO {

    @Schema(description = "응답 상태", example = "success")
    private HttpStatus status;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private String message;

    private String loginId;

    public FindUserIdResponseDTO(String loginId,HttpStatus status, String message) {
        this.loginId = loginId;
        this.status = status;
        this.message = message;
    }

}
