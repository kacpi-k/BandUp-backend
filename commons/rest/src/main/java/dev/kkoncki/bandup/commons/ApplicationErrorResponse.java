package dev.kkoncki.bandup.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationErrorResponse {
    private Instant timestamp;
    private int status;
    private ErrorCode errorCode;
    private String message;
    private Map<String, String> errors;
}
