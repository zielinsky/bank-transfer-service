package com.jzielinski.banktransferservice.api.handler;

import com.jzielinski.banktransferservice.api.RequestHandlerException;
import com.jzielinski.banktransferservice.api.dto.TransferDto;
import com.jzielinski.banktransferservice.service.account.exception.AccountDoesNotExistException;
import com.jzielinski.banktransferservice.service.transfer.TransferService;
import io.netty.handler.codec.http.HttpResponseStatus;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.jackson.Jackson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class OutgoingTransfersHandler implements Handler {
    private final TransferService transferService;

    @Inject
    public OutgoingTransfersHandler(TransferService transferService) {
        this.transferService = transferService;
    }

    @Override
    public void handle(Context ctx) {
        try {
            List<TransferDto> transferDtoList = transferService.getOutgoingTransfers(
                    Long.parseLong(ctx.getPathTokens().get("id"))).stream()
                    .map(transfer ->
                            new TransferDto(
                                    transfer.getUuid(),
                                    transfer.getFromAccount(),
                                    transfer.getToAccount(),
                                    transfer.getAmount(),
                                    transfer.getCreatedOn()))
                    .collect(Collectors.toList());
            ctx.render(Jackson.json(transferDtoList));
        } catch (NumberFormatException e) {
            throw new RequestHandlerException(HttpResponseStatus.BAD_REQUEST.code(),
                    "Account id has to be an integer number.", e);
        } catch (AccountDoesNotExistException e) {
            throw new RequestHandlerException(HttpResponseStatus.NOT_FOUND.code(),
                    "Account with the given id does not exist.", e);
        }
    }
}
