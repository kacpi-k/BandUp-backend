package dev.kkoncki.bandup.commons;

public enum ErrorCode {

    USER_NOT_FOUND("User not found"),
    USER_NOT_LOGGED_IN("User is not logged in"),
    USER_BLOCKED("User is blocked"),
    INVALID_CREDENTIALS("Wrong email or password"),
    WRONG_PASSWORD("Wrong password"),;

    ErrorCode(String message) {
        this.message = message;
    }

    final String message;
}
