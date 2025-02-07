package dev.kkoncki.bandup.commons;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ErrorCode code;
    private String description;

    public ApplicationException(ErrorCode code, String description) {
        super(code.message);
        this.code = code;
        this.description = description;
    }

    public ApplicationException(ErrorCode code) {
        super(code.message);
        this.code = code;
    }

    public ErrorCode getErrorCode() {
        return code;
    }
}
