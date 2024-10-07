package com.example.afaloan.exceptions

enum class ErrorCode {

    // common errors
    INVALID_REQUEST,
    SERVICE_UNAVAILABLE,

    // jwt errors
    TOKEN_EXPIRED,
    TOKEN_DOES_NOT_EXIST,
    TOKEN_INCORRECT_FORMAT,

    // file errors
    WRONG_FILE,
    DOCUMENT_NOT_YOURS,
    UNAVAILABLE_CONTENT_TYPE,
    FILE_SERVICE_UNAVAILABLE,

    // user errors
    FORBIDDEN,
    USER_NOT_FOUND,
    USER_ALREADY_EXIST,
    USER_PASSWORD_INCORRECT,
    ROLE_NOT_FOUND,

    // profile errors
    PROFILE_NOT_FOUND,

    // boiling point errors
    BOILING_POINT_NOT_FOUND,

    // microloan errors
    MICROLOAN_NOT_FOUND,

    // bid errors
    BID_NOT_FOUND,

    // process errors
    PROCESS_NOT_FOUND
}