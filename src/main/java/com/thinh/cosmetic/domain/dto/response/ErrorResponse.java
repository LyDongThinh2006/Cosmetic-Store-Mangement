package com.thinh.cosmetic.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private int status;
    private String code;
    private String message;
    private String path;
    private List<FieldErrorDto> fieldErrors;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FieldErrorDto {
        private String field;
        private String message;
    }
}
