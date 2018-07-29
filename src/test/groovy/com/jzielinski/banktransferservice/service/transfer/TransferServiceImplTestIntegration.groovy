package com.jzielinski.banktransferservice.service.transfer

import com.jzielinski.banktransferservice.service.account.AccountServiceImpl
import com.jzielinski.banktransferservice.service.account.repository.InMemoryAccountRepository
import com.jzielinski.banktransferservice.service.transfer.repository.TransferRepository
import spock.lang.Specification

class TransferServiceImplTestIntegration extends Specification {
    def transferRepository = Mock(TransferRepository)
    def accountService = new AccountServiceImpl(new InMemoryAccountRepository())
    def transferService = new TransferServiceImpl(transferRepository, accountService)

    def "transferring money should be thread safe"() {
        given:
        accountService.createAccount(1, BigDecimal.valueOf(100))
        accountService.createAccount(2, BigDecimal.valueOf(0))

        def threads = (0..<100).collect {
            new Thread({
                transferService.transferFunds(1, 2, BigDecimal.valueOf(1))
            })
        }

        when:
        threads.each { it.start() }
        threads.each { it.join() }

        then:
        accountService.getAccountById(1).get().getBalance() == BigDecimal.valueOf(0)
        accountService.getAccountById(2).get().getBalance() == BigDecimal.valueOf(100)
    }
}
