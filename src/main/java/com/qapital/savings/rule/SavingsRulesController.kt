package com.qapital.savings.rule

import com.qapital.savings.event.SavingsEvent
import java.net.URI
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.zalando.problem.Problem
import org.zalando.problem.Status


@RestController
@RequestMapping("/api/savings/rule")
class SavingsRulesController @Autowired constructor(private val savingsRulesService: SavingsRulesService) {

    @GetMapping("/active/{userId}")
    fun activeRulesForUser(@PathVariable userId: Long?): List<SavingsRule>? {
        return savingsRulesService.activeRulesForUser(userId)
    }

    @PostMapping("/apply")
    fun applyRule(@Valid @RequestBody savingRule: SavingsRule): ResponseEntity<List<SavingsEvent>> {
        try {
            return ResponseEntity.accepted().body(savingsRulesService.executeRule(savingRule))
        } catch (exception: Exception) {
            throw failedToApplySavingRule(savingRule, exception)
        }
    }

    private fun failedToApplySavingRule(savingsRule: SavingsRule, exception: java.lang.Exception): Throwable {
        return Problem.builder()
            .withType(URI.create("https://qapital.com/failed-to-apply-rule"))
            .withTitle("Failed to apply rule")
            .withStatus(Status.INTERNAL_SERVER_ERROR)
            .withDetail(
                "Failed to apply rule on transaction for user : ${savingsRule.userId}, " +
                        "error message: ${exception.stackTraceToString()}"
            )
            .build()
    }

}