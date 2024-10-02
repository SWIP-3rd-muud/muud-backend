package com.muud.playlist.exception;

import com.muud.global.exception.support.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PlayListErrorCode implements ErrorCode {

    PLAY_LIST_NOT_FOUND("존재하지 않는 플레이리스트 입니다.", HttpStatus.NOT_FOUND),
    INVALID_REQUEST("잘못된 API 요청입니다.", HttpStatus.BAD_REQUEST),
    YOUTUBE_REQUEST_ERROR("YouTube 데이터 요청에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    PLAYLIST_DEFAULT("PlayList 요청 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    PlayListErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String defaultMessage() {
        return message;
    }

    @Override
    public HttpStatus defaultHttpStatus() {
        return status;
    }

    @Override
    public PlayListException defaultException() {
        return new PlayListException(this);
    }

    @Override
    public PlayListException defaultException(Throwable cause) {
        return new PlayListException(this, cause);
    }

}
