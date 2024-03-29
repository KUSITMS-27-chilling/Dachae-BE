package chilling.encore.domain.oauth.dto;

import lombok.*;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class Oauth2SignUpResponse {
    private String id;
    private String email;
    private String gender;
    private String provider;
}
