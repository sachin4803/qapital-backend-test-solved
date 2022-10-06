package com.qapital.savings.rule.apply.roundup

import com.qapital.bankdata.transaction.Transaction
import com.qapital.bankdata.transaction.TransactionsService
import com.qapital.savings.event.SavingsEvent
import com.qapital.savings.rule.SavingsRule
import com.qapital.savings.rule.apply.mapper.createSavingAmountOnEachGoalOnTransactionAmount
import com.qapital.savings.rule.apply.mapper.createSavingAmountRoundUpGoal
import com.qapital.savings.rule.apply.mapper.createSavingsEvent
import com.qapital.savings.rule.apply.mapper.isAnExpense
import java.math.BigDecimal
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

interface RoundUpSavingRuleService {
    fun executeRule(savingsRule: SavingsRule): List<SavingsEvent>
}

class RoundUpSavingRuleServiceImpl(private val transactionsService: TransactionsService) : RoundUpSavingRuleService {

    override fun executeRule(savingsRule: SavingsRule): List<SavingsEvent> {
        val transactions = transactionsService.latestTransactionsForUser(savingsRule.userId)

        return transactions.filter { isEligibleToApplyRule(it, savingsRule) }.flatMap { transaction ->
            createSavingsEvents(transaction, savingsRule)
        }
    }

    private fun createSavingsEvents(
        transaction: Transaction,
        savingsRule: SavingsRule
    ): List<SavingsEvent> {
        val savingsAmount = createSavingAmountRoundUpGoal(transaction.amount.abs(), savingsRule.amount)
        return if (BigDecimal.ZERO != savingsAmount) {
            val savingAmountOnEachGoal = createSavingAmountOnEachGoalOnTransactionAmount(
                savingAmountOnCompleteGoal = savingsAmount,
                numberOfGoals = savingsRule.savingsGoalIds.size
            )
            savingsRule.savingsGoalIds.map { goalId: Long ->
                createSavingsEvent(
                    savingsRule = savingsRule, savingGoalId = goalId,
                    savingAmount = savingAmountOnEachGoal,
                    transaction = transaction
                )
            }
        } else emptyList()
    }

    private fun isEligibleToApplyRule(
        transaction: Transaction,
        savingsRule: SavingsRule
    ): Boolean {
        return isAnExpense(transaction) &&
                transaction.amount != savingsRule.amount
    }
}

@Configuration
open class RoundUpSavingRuleServiceConfiguration {

    @Bean
    open fun roundUpSavingRuleService(transactionsService: TransactionsService): RoundUpSavingRuleService {
        return RoundUpSavingRuleServiceImpl(transactionsService)
    }

}