package ru.evgeni.microservices.core.payment.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
public class HttpErrorInfo {
    private ZonedDateTime timestamp;
    private String path;
    private HttpStatus httpStatus;
    private String message;

    public HttpErrorInfo(HttpStatus httpStatus, String path, String message) {
        timestamp = ZonedDateTime.now();
        this.httpStatus = httpStatus;
        this.path = path;
        this.message = message;
    }
}
