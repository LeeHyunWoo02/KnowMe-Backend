package HYU.FishShip.Feature.User.Controller;

import HYU.FishShip.Feature.User.Dto.LoginResponseDTO;
import HYU.FishShip.Feature.User.Dto.LogoutResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class LoginController {



    /**
     * 실제 로그인 로직은 LoginFilter를 통해 구현됨.
     * 프론트 개발자를 위한 api 명세서를 위한 코드
     * */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공 시 JWT 토큰 반환"),
            @ApiResponse(responseCode = "401", description = "로그인 실패 (잘못된 자격 증명)"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (필수 파라미터 누락)")
    })
    @Operation(summary = "로그인 로직 (Swagger 명세 전용)", description = "userId와 password를 포함하여 POST /login으로 요청을 보내면 기본 로그인 로직이 실행됩니다. " +
            "이 엔드포인트는 실제 요청을 처리하지 않고 문서화를 위해 제공됩니다"+"프론트엔드 팀은 JWT 토큰을 localStorage나 sessionStorage에 저장한 뒤," +
            " 인증이 필요한 API 요청 시 Authorization: Bearer <JWT 토큰> 형식으로 요청 헤더에 포함해 보내야 합니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginProc(
            @Parameter(description = "사용자 ID", required = true) @RequestParam(name = "userId") @NotBlank String userId,
            @Parameter(description = "사용자 비밀번호", required = true) @RequestParam(name = "password") @NotBlank String password) {
        LoginResponseDTO response = new LoginResponseDTO(HttpStatus.OK,"로그인 성공",null);

        return ResponseEntity.ok(response);
    }

    /**
     * oAuth2 소셜 로그인 설명
     * 실제로는 Filter 단에서 작동 됨.
     * 프론트 개발자를 위한 api 명세서를 위한 코드
     * */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "소셜 로그인 페이지로 리디렉션"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @Operation(summary = "OAuth2 소셜 로그인 (Swagger 명세 전용)",
            description = "앤드포인트 -> /oauth2/authorization/{provider}" +
                    "사용자가 /oauth2/authorization/{provider}로 리디렉션하면 해당 소셜 로그인 페이지로 이동합니다. 로그인 성공 시 서버는 JWT 토큰을 쿠키에 저장하고, " +
                    "클라이언트는 이후 요청에서 해당 토큰을 활용하여 인증할 수 있습니다. 실제 요청 처리가 아닌 명세용 설명입니다." +
                    "클라이언트가 /oauth2/authorization/naver로 접근하면 Spring Security의 OAuth2 설정에 따라 자동으로 네이버 로그인 로직이 실행됩니다. " +
                    "그리고 로그인에 성공하면 커스텀 성공 핸들러(CustomOAuth2SuccessHandler)가 작동하여 JWT 토큰을 발급하고, 이를 클라이언트에게 쿠키로 전달" +
                    "쿠키의 만료 시간은 1시간"
    )
    @GetMapping("/oauth2/authorization/{provider}")
    public ResponseEntity<LoginResponseDTO> oauth2Login(
            @Parameter(description = "소셜 로그인 제공자 (예: google, facebook, github)", required = true)
            @PathVariable String provider) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(new LoginResponseDTO(HttpStatus.FOUND, "소셜 로그인 페이지로 리디렉션", null));
    }



    /**
     * 로그아웃
     * 실제 관련 로직은 filter 단에서 움직임.
     * 프론트 개발자를 위한 api 명세서를 위한 코드
     * */

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @Operation(summary = "로그아웃 로직 (Swagger 명세 전용)",
            description = "POST /logout 요청을 보내면 클라이언트 측에서 JWT 토큰을 삭제해 로그아웃을 처리합니다. 이 엔드포인트는 실제 서버에서 처리하지 않고, 문서화를 위해 제공됩니다."+
                    "로그아웃 요청 시 클라이언트 측에서 저장된 JWT 토큰을 삭제해야 합니다. " +
                    "서버는 세션을 사용하지 않으므로 JWT 기반에서는 토큰 자체를 삭제하는 것이 곧 로그아웃을 의미")
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDTO> logoutProc(){
        LogoutResponseDTO response = new LogoutResponseDTO(HttpStatus.OK, "로그아웃 성공", null);

        return ResponseEntity.ok(response);
    }




}
