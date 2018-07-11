package com.jzielinski.banktransferservice.service.transfer

import com.jzielinski.banktransferservice.service.account.AccountService
import com.jzielinski.banktransferservice.service.account.entity.Account
import com.jzielinski.banktransferservice.service.account.exception.NotEnoughFundsException
import com.jzielinski.banktransferservice.service.transfer.entity.Transfer
import com.jzielinski.banktransferservice.service.transfer.repository.TransferRepository
import spock.lang.Specification

import java.time.ZonedDateTime

class TransferServiceImplTest extends Specification {
    def transferRepository = Mock(TransferRepository)
    def accountService = Mock(AccountService)
    def transferService = new TransferServiceImpl(transferRepository, accountService)

    def "should create and save new transfer"() {
        when:
        def result = transferService.transferFunds(1, 2, BigDecimal.valueOf(50))

        then:
        result != null
        result.fromAccount == 1
        result.toAccount == 2
        result.amount == 50
        1 * transferRepository.save({ it.fromAccount == 1 && it.toAccount == 2 && it.amount == 50 })
    }

    def "should transfer money between accounts"() {
        when:
        transferService.transferFunds(1, 2, BigDecimal.valueOf(50))

        then:
        1 * accountService.debitAccount(1, BigDecimal.valueOf(50))
        1 * accountService.creditAccount(2, BigDecimal.valueOf(50))
    }

    def "should throw exception if account has insufficient funds"() {
        given:
        accountService.debitAccount(1, BigDecimal.valueOf(100)) >> { throw new NotEnoughFundsException() }

        when:
        transferService.transferFunds(1, 2, BigDecimal.valueOf(100))

        then:
        thrown(NotEnoughFundsException)
    }

    def "should return all transfers"() {
        given:
        def transfers = [new Transfer(1, 2, BigDecimal.valueOf(50), ZonedDateTime.now()),
                         new Transfer(2, 1, BigDecimal.valueOf(50), ZonedDateTime.now()),]
        transferRepository.getAllTransfers() >> transfers

        when:
        def result = transferService.getAllTransfers()

        then:
        result == transfers
    }

    def "should return all incoming transfers for the given user"() {
        given:
        def transfers = [new Transfer(1, 2, BigDecimal.valueOf(50), ZonedDateTime.now()),
                         new Transfer(1, 2, BigDecimal.valueOf(50), ZonedDateTime.now())]
        accountService.getAccountById(2) >> Optional.of(new Account(2,BigDecimal.valueOf(100)))
        transferRepository.getByToAccount(2)>> transfers

        when:
        def result = transferService.getIncomingTransfers(2)

        then:
        result == transfers
    }

    def "should return all outgoing transfers for the given user"() {
        given:
        def transfers = [new Transfer(1, 2, BigDecimal.valueOf(50), ZonedDateTime.now()),
                         new Transfer(1, 2, BigDecimal.valueOf(50), ZonedDateTime.now())]
        accountService.getAccountById(1) >> Optional.of(new Account(1, BigDecimal.valueOf(100)))
        transferRepository.getByFromAccount(1) >> transfers

        when:
        def result = transferService.getOutgoingTransfers(1)

        then:
        result == transfers
    }
}
