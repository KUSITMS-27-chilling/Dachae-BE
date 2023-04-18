package chilling.encore.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //Json 결과에서 Null은 빼고 나타남
public class ResponseDto<T> {
    private String message;
    private T data;

    public static <T> ResponseDto<T> create(String message) {
        return new ResponseDto(message, null);
    }
    public static <T> ResponseDto<T> create(String message, T dto) {
        return new ResponseDto(message, dto);
    }
}