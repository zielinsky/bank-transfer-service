package com.jzielinski.banktransferservice.service.account.entity

import com.jzielinski.banktransferservice.service.account.exception.NotEnoughFundsException
import spock.lang.Specification

class AccountTest extends Specification {

    def "should create an account with zero balance"() {
        when:
        def account = new Account(1)

        then:
        account.getBalance() == 0
    }

    def "should create an account with given initial balance"() {
        when:
        def account = new Account(1, BigDecimal.valueOf(50))

        then:
        account.getBalance() == 50
    }

    def "should be able to credit an account"() {
        given:
        def account = new Account(1)

        when:
        account.creditAccount(BigDecimal.valueOf(100))

        then:
        account.getBalance() == BigDecimal.valueOf(100)
    }

    def "should be able to debit an account"() {
        given:
        def account = new Account(1)
        account.creditAccount(BigDecimal.valueOf(100))

        when:
        account.debitAccount(BigDecimal.valueOf(100))

        then:
        account.getBalance() == BigDecimal.valueOf(0)
    }

    def "should not be able to debit an account with insufficient funds" () {
        given:
        def account = new Account(1)
        account.creditAccount(BigDecimal.valueOf(50))

        when:
        account.debitAccount(BigDecimal.valueOf(100))

        then:
        thrown(NotEnoughFundsException)
    }

    def "debitAccount should be thread safe" () {
        given:
        def account = new Account(1, BigDecimal.valueOf(100))
        def threads = (0..<100).collect { new Thread({ account.debitAccount(BigDecimal.valueOf(1)) })}

        when:
        threads.each { it.start() }
        threads.each { it.join() }

        then:
        account.getBalance() == BigDecimal.valueOf(0)
    }

    def "creditAccount should be thread safe" () {
        given:
        def account = new Account(1, BigDecimal.valueOf(0))
        def threads = (0..<100).collect { new Thread({ account.creditAccount(BigDecimal.valueOf(1)) })}

        when:
        threads.each { it.start() }
        threads.each { it.join() }

        then:
        account.getBalance() == BigDecimal.valueOf(100)
    }
}
