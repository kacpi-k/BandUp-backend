package dev.kkoncki.bandup.commons;

public enum ErrorCode {

    USER_NOT_LOGGED_IN("User is not logged in");

    ErrorCode(String message) {
        this.message = message;
    }

    final String message;
}
