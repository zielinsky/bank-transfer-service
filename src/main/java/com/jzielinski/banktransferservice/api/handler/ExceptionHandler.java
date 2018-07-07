package com.jzielinski.banktransferservice.api.handler;

import com.jzielinski.banktransferservice.api.RequestHandlerException;
import com.jzielinski.banktransferservice.api.dto.ErrorDto;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.error.ServerErrorHandler;
import ratpack.handling.Context;
import ratpack.jackson.Jackson;
import ratpack.parse.NoSuchParserException;

public class ExceptionHandler implements ServerErrorHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public void error(Context context, Throwable throwable) {
        if(throwable instanceof RequestHandlerException) {
            RequestHandlerException requestHandlerException = (RequestHandlerException) throwable;
            context.getResponse().status(requestHandlerException.getResponseStatusCode());
            context.render(Jackson.json(new ErrorDto(requestHandlerException.getMessage())));
        }
        else if(throwable instanceof NoSuchParserException) {
            context.getResponse().status(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE.code());
            context.render(Jackson.json(new ErrorDto("Content type not supported.")));
        }
        else {
            LOGGER.warn("An internal server error has occurred.", throwable);
            context.getResponse().status(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
            context.render(Jackson.json(new ErrorDto("An internal server error has occurred.")));
        }
    }
}
