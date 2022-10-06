package com.qapital

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
open class SavingRuleApiTest {
    @LocalServerPort
    private var port = 0

    @Test
    fun test_guilty_pleasure_rule_apply() {
        RestAssured.given()
            .port(port)
            .headers(createHeaderWithSecurity())
            .contentType(ContentType.JSON)
            .request()
            .body(readFile("/request/guilty-pleasure-rule-appy-request.json"))
            .`when`()
            .post("/api/savings/rule/apply")
            .then()
            .assertThat()
            .statusCode(HttpStatus.ACCEPTED.value())
    }

    @Test
    fun test_round_up_rule_apply() {
        RestAssured.given()
            .port(port)
            .headers(createHeaderWithSecurity())
            .contentType(ContentType.JSON)
            .request()
            .body(readFile("/request/round-up-rule-appy-request.json"))
            .`when`()
            .post("/api/savings/rule/apply")
            .then()
            .assertThat()
            .statusCode(HttpStatus.ACCEPTED.value())
    }

    private fun createHeaderWithSecurity(): HttpHeaders {
        val headers = HttpHeaders()
        headers[HttpHeaders.AUTHORIZATION] = "Basic c2FjaGluOnNoZXR0eQ=="
        return headers
    }


    private fun readFile(filePath: String) = this::class.java.getResource(filePath).readText(Charsets.UTF_8)
}