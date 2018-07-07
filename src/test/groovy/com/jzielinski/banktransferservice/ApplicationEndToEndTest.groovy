package com.jzielinski.banktransferservice

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import ratpack.http.client.RequestSpec
import ratpack.test.MainClassApplicationUnderTest
import ratpack.test.ServerBackedApplicationUnderTest
import ratpack.test.http.TestHttpClient
import spock.lang.Specification

class ApplicationEndToEndTest extends Specification{
    ServerBackedApplicationUnderTest aut = new MainClassApplicationUnderTest(Main.class)
    @Delegate TestHttpClient client = testHttpClient(aut)
    def json = new JsonSlurper()

    def "should be able to create and retrieve an account"() {
        when:
        requestSpec { RequestSpec requestSpec ->
            requestSpec.body.type("application/json")
            requestSpec.body.text(JsonOutput.toJson([id: 5]))
        }
        post("accounts")

        then:
        response.statusCode == 201
        def account = json.parseText(response.body.text)
        with(account) {
            id == 5
            balance == 0
        }

        and:
        resetRequest()
        def retrievedAccount = json.parseText(get("accounts/5").body.text)
        with(retrievedAccount) {
            id == 5
            balance == 0
        }
    }

    def "should be able to transfer money between accounts"() {
        given:
        requestSpec { RequestSpec requestSpec ->
            requestSpec.body.type("application/json")
            requestSpec.body.text(JsonOutput.toJson([id: 1, balance: 100]))
        }
        post("accounts")

        and:
        resetRequest()
        requestSpec { RequestSpec requestSpec ->
            requestSpec.body.type("application/json")
            requestSpec.body.text(JsonOutput.toJson([id: 2, balance: 100]))
        }
        post("accounts")

        when:
        resetRequest()
        requestSpec { RequestSpec requestSpec ->
            requestSpec.body.type("application/json")
            requestSpec.body.text(JsonOutput.toJson([fromAccount: 1, toAccount: 2, amount: 50]))
        }
        post("transfers")

        then:
        response.statusCode == 201
        def transfer = json.parseText(response.body.text)
        with(transfer) {
            fromAccount == 1
            toAccount == 2
            amount == 50
        }

        and:
        resetRequest()
        def account1 = json.parseText(get("accounts/1").body.text)
        with(account1) {
            balance == 50
        }

        and:
        resetRequest()
        def account2 = json.parseText(get("accounts/2").body.text)
        with(account2) {
            balance == 150
        }
    }

    def "should not be able to transfer money from account with not enough money"() {
        given:
        requestSpec { RequestSpec requestSpec ->
            requestSpec.body.type("application/json")
            requestSpec.body.text(JsonOutput.toJson([id: 1, balance: 100]))
        }
        post("accounts")

        and:
        resetRequest()
        requestSpec { RequestSpec requestSpec ->
            requestSpec.body.type("application/json")
            requestSpec.body.text(JsonOutput.toJson([id: 2, balance: 100]))
        }
        post("accounts")

        when:
        resetRequest()
        requestSpec { RequestSpec requestSpec ->
            requestSpec.body.type("application/json")
            requestSpec.body.text(JsonOutput.toJson([fromAccount: 1, toAccount: 2, amount: 200]))
        }
        post("transfers")

        then:
        response.statusCode == 403

        and:
        resetRequest()
        def account1 = json.parseText(get("accounts/1").body.text)
        with(account1) {
            balance == 100
        }

        and:
        resetRequest()
        def account2 = json.parseText(get("accounts/2").body.text)
        with(account2) {
            balance == 100
        }
    }
}
