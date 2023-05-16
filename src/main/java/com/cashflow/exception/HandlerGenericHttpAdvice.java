package com.cashflow.exception;

import com.cashflow.exception.handler.HandlerExceptionNotFound;
import com.cashflow.exception.handler.HandlerValidationException;
import com.cashflow.exception.response.HandlerGenericHttpResponse;
import com.cashflow.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class HandlerGenericHttpAdvice {

    private final String MESSAGE_RESOURCE_NOT_FOUND = "URL não encontrada para a requisição";
    private final String ERROR_UNKNOW = "Erro desconhecido";
    private final int CODE_404 = 404;
    private final int CODE_400 = 400;
    private final int CODE_500 = 500;
    private final FormatUtil formatUtil;

    @ExceptionHandler({HandlerExceptionNotFound.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody HandlerGenericHttpResponse handlerResourceNotFound(final HandlerExceptionNotFound exception,
                                                                            final HttpServletRequest request) {
        loggerHandler(exception.getMessage(), request.getRequestURI(), CODE_404, request.getMethod(), exception);
        return exceptionResponseHandler(request.getRequestURI(),exception.getMessage(),CODE_404,request.getMethod());
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody
    HandlerGenericHttpResponse handleNoHandlerFound(final NoHandlerFoundException exception,
                                                    final HttpServletRequest request) {
        loggerHandler(exception.getMessage(), request.getRequestURI(), CODE_404, request.getMethod(), exception);
        return exceptionResponseHandler(request.getRequestURI(),MESSAGE_RESOURCE_NOT_FOUND,CODE_404,request.getMethod());
    }

    @ExceptionHandler({HandlerValidationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody HandlerGenericHttpResponse handlerResourceValidation (final HandlerValidationException exception,
                                                                               final HttpServletRequest request) {
        loggerHandler(exception.getMessage(), request.getRequestURI(), CODE_400, request.getMethod(), exception);
        return exceptionResponseHandler(request.getRequestURI(),exception.getMessage(),CODE_400,request.getMethod());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody HandlerGenericHttpResponse handlerException (final Exception exception,
                                                                      final HttpServletRequest request) {
        String messageHandle = exception == null ? ERROR_UNKNOW : exception.getMessage();
        loggerHandler(messageHandle, request.getRequestURI(), CODE_500, request.getMethod(), exception);
        return exceptionResponseHandler(request.getRequestURI(), messageHandle,CODE_500,request.getMethod());
    }

    private HandlerGenericHttpResponse exceptionResponseHandler(String requestURI, String errorMessage,
                                                                int statusCode, String method) {
        return HandlerGenericHttpResponse.builder()
            .calledUrl(requestURI)
            .dateTime(formatUtil.dateTimeFormated())
            .errorMessage(errorMessage)
            .statusCode(statusCode)
            .method(method)
            .build();
    }

    private void loggerHandler(String message, String calledURL,
                               int statusCode, String method, Throwable exception) {
        log.error("Error: {} | URL requisitada: {} | Código de erro: {} | Método: {}",
            message, calledURL, statusCode, method, exception);
    }
}
