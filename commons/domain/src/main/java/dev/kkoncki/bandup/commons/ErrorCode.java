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
    USER_INSTRUMENT_ALREADY_EXISTS("User instrument already exists"),
    USER_ALREADY_HAS_INSTRUMENT("User already has this instrument"),
    USER_DOES_NOT_HAVE_INSTRUMENT("User does not have this instrument"),
    USER_INSTRUMENT_NOT_FOUND("User instrument not found"),
    BAND_MEMBER_NOT_FOUND("Band member not found"),
    BAND_NOT_FOUND("Band not found"),
    NOT_A_LEADER("User is not a leader of the band"),
    FAILED_TO_UPLOAD_FILE("Failed to upload file"),
    FILE_NOT_FOUND("File not found"),
    FAILED_TO_DOWNLOAD_FILE("Failed to download file"),
    FAILED_TO_DELETE_FILE("Failed to delete file"),
    FAILED_TO_GENERATE_FILE_URL("Failed to generate file URL"),
    BUCKET_NOT_FOUND("Bucket not found"),
    UNAUTHORIZED("Unauthorized attempt to send message"),
    INVALID_MEDIA_TYPE("Invalid media type"),
    POST_NOT_FOUND("Post not found"),
    COMMENT_NOT_FOUND("Comment not found"),
    POST_ALREADY_LIKED("Post already liked"),
    POST_NOT_LIKED("Post not liked"),;

    ErrorCode(String message) {
        this.message = message;
    }

    final String message;
}
