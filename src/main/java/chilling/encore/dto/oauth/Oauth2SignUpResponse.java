package chilling.encore.dto.oauth;

import lombok.*;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class Oauth2SignUpResponse {
    private String id;
    private String email;
    private String nickname;
    private String gender;
}
