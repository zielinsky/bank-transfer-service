package com.jzielinski.banktransferservice.service.account

import com.jzielinski.banktransferservice.service.account.entity.Account
import com.jzielinski.banktransferservice.service.account.repository.AccountRepository
import spock.lang.Specification

class AccountServiceImplTest extends Specification {
    def accountRepository = Mock(AccountRepository)
    def accountService = new AccountServiceImpl(accountRepository)

    def "should credit and save an account"() {
        given:
        def account = new Account(1)
        accountRepository.getById(account.getId()) >> Optional.of(account)

        when:
        accountService.creditAccount(1, BigDecimal.valueOf(100))

        then:
        account.getBalance() == 100
        1 * accountRepository.save(account)
    }

    def "should debit and save an account"() {
        given:
        def account = new Account(1)
        account.creditAccount(BigDecimal.valueOf(100))
        accountRepository.getById(account.getId()) >> Optional.of(account)

        when:
        accountService.debitAccount(1, BigDecimal.valueOf(100))

        then:
        account.getBalance() == 0
        1 * accountRepository.save(account)
    }

    def "should return all accounts"() {
        given:
        def accounts = [new Account(1), new Account(2)]
        accountRepository.getAllAccounts() >> accounts

        when:
        def result = accountService.getAllAccounts()

        then:
        result == accounts
    }

    def "should return the account with given id"() {
        given:
        def account = new Account(1)
        accountRepository.getById(1) >> Optional.of(account)

        when:
        def result = accountService.getAccountById(1)

        then:
        result == Optional.of(account)
    }

    def "should create a new account with given initial balance"() {
        given:
        accountRepository.getById(1) >> Optional.empty()

        when:
        def account = accountService.createAccount(1, BigDecimal.valueOf(100))

        then:
        account != null
        account.id == 1
        account.balance == 100
        1 * accountRepository.save({ it.id == 1 && it.balance == 100 })
    }

    def "should create a new account with initial balance of zero"() {
        given:
        accountRepository.getById(1) >> Optional.empty()

        when:
        def account = accountService.createAccount(1)

        then:
        account != null
        account.id == 1
        account.balance == 0
        1 * accountRepository.save({ it.id == 1 && it.balance == 0 })
    }
}
