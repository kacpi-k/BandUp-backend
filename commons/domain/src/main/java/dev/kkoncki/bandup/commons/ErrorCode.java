package dev.kkoncki.bandup.commons;

public enum ErrorCode {

    USER_NOT_FOUND("User not found"),
    USER_NOT_LOGGED_IN("User is not logged in"),
    USER_BLOCKED("User is blocked"),
    INVALID_CREDENTIALS("Wrong email or password"),
    WRONG_PASSWORD("Wrong password"),
    GENRE_NOT_FOUND("Genre not found"),
    INSTRUMENT_CATEGORY_NOT_FOUND("Instrument category not found"),
    INSTRUMENT_NOT_FOUND("Instrument not found"),
    USER_INSTRUMENT_NOT_FOUND("User instrument not found"),;

    ErrorCode(String message) {
        this.message = message;
    }

    final String message;
}
