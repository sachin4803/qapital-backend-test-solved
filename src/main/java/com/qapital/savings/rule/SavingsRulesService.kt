package com.qapital.savings.rule

import com.qapital.savings.event.SavingsEvent
import com.qapital.savings.rule.SavingsRule.RuleType.guilty_pleasure
import com.qapital.savings.rule.SavingsRule.RuleType.roundup
import com.qapital.savings.rule.apply.guiltpleasure.GuiltyPleasureSavingRuleService
import com.qapital.savings.rule.apply.roundup.RoundUpSavingRuleService
import java.math.BigDecimal
import java.net.URI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.zalando.problem.Problem
import org.zalando.problem.Status

interface SavingsRulesService {
    /**
     * Fetches the active savings rules for the specified user
     * @param userId the user id
     * @return a list of savings rules
     */
    fun activeRulesForUser(userId: Long?): List<SavingsRule>?

    /**
     * Executes the logic for given savings rule
     * @param savingsRule the configured savings rule
     * @return a list of savings events that are the result of the execution of the rule
     */
    fun executeRule(savingsRule: SavingsRule): List<SavingsEvent>
}

@Service
class StandardSavingsRulesService @Autowired constructor(
    private val guiltyPleasureRuleService: GuiltyPleasureSavingRuleService,
    private val roundUpSavingRuleService: RoundUpSavingRuleService
) : SavingsRulesService {

    override fun activeRulesForUser(userId: Long?): List<SavingsRule> {
        val guiltyPleasureRule = SavingsRule.createGuiltyPleasureRule(1L, userId, "Starbucks", BigDecimal("3.00"))
        guiltyPleasureRule.addSavingsGoal(1L)
        guiltyPleasureRule.addSavingsGoal(2L)
        val roundupRule = SavingsRule.createRoundupRule(2L, userId, BigDecimal("2.00"))
        roundupRule.addSavingsGoal(1L)
        return listOf(guiltyPleasureRule, roundupRule)
    }

    override fun executeRule(savingsRule: SavingsRule): List<SavingsEvent> {
        return when (savingsRule.ruleType) {
            guilty_pleasure -> guiltyPleasureRuleService.executeRule(savingsRule)
            roundup -> roundUpSavingRuleService.executeRule(savingsRule)
            else -> throw throw createNotValidRuleTypeError(savingsRule)
        }
    }

    private fun createNotValidRuleTypeError(savingsRule: SavingsRule): Throwable {
        return Problem.builder()
            .withType(URI.create("https://qapital.com/ivalid_rule_type"))
            .withTitle("Invalid Rule Type")
            .withStatus(Status.INTERNAL_SERVER_ERROR)
            .withDetail(
                "${savingsRule.ruleType.name} is not valid RuleType to apply on savings."
            )
            .build()
    }
}