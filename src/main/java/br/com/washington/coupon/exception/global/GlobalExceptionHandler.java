package br.com.washington.coupon.exception.global;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseStatusExceptionHandler {
}
