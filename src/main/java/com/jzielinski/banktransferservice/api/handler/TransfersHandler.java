package com.jzielinski.banktransferservice.api.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.jzielinski.banktransferservice.api.RequestHandlerException;
import com.jzielinski.banktransferservice.api.dto.TransferDto;
import com.jzielinski.banktransferservice.service.account.exception.AccountDoesNotExistException;
import com.jzielinski.banktransferservice.service.account.exception.NotEnoughFundsException;
import com.jzielinski.banktransferservice.service.transfer.TransferService;
import com.jzielinski.banktransferservice.service.transfer.entity.Transfer;
import io.netty.handler.codec.http.HttpResponseStatus;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.jackson.Jackson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class TransfersHandler implements Handler {
    private final TransferService transferService;

    @Inject
    public TransfersHandler(TransferService transferService) {
        this.transferService = transferService;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.byMethod(x -> x
                .post(this::createTransfer)
                .get(this::getTransfers));
    }

    private void createTransfer(Context ctx) {
        ctx.parse(Jackson.fromJson(TransferDto.class))
                .mapError(JsonParseException.class, e -> {
                    throw new RequestHandlerException(HttpResponseStatus.BAD_REQUEST.code(),
                            "There was an error in the request body.", e);
                })
                .then(transferDto -> createTransfer(ctx, transferDto));
    }

    private void createTransfer(Context ctx, TransferDto transferDto) {
        try {
            Transfer transfer = transferService.transferFunds(
                    transferDto.getFromAccount(),
                    transferDto.getToAccount(),
                    transferDto.getAmount());

            ctx.getResponse().status(HttpResponseStatus.CREATED.code());
            ctx.render(Jackson.json(new TransferDto(
                    transfer.getUuid(),
                    transfer.getFromAccount(),
                    transfer.getToAccount(),
                    transfer.getAmount(),
                    transfer.getCreatedOn()
            )));
        } catch (AccountDoesNotExistException e) {
            throw new RequestHandlerException(HttpResponseStatus.BAD_REQUEST.code(),
                    "Account with the given id does not exist.", e);
        } catch (NotEnoughFundsException e) {
            throw new RequestHandlerException(HttpResponseStatus.FORBIDDEN.code(),
                    "The account does not have enough funds to complete the transfer.", e);
        }
    }

    private void getTransfers(Context ctx) {
        List<TransferDto> transferDtoList = transferService.getAllTransfers().stream()
                .map(transfer ->
                        new TransferDto(
                                transfer.getUuid(),
                                transfer.getFromAccount(),
                                transfer.getToAccount(),
                                transfer.getAmount(),
                                transfer.getCreatedOn()))
                .collect(Collectors.toList());
        ctx.render(Jackson.json(transferDtoList));
    }
}
