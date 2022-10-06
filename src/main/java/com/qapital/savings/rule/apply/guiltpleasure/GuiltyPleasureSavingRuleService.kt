package com.qapital.savings.rule.apply.guiltpleasure

import com.qapital.bankdata.transaction.Transaction
import com.qapital.bankdata.transaction.TransactionsService
import com.qapital.savings.event.SavingsEvent
import com.qapital.savings.rule.SavingsRule
import com.qapital.savings.rule.apply.mapper.createSavingAmountOnEachGoalOnTransactionAmount
import com.qapital.savings.rule.apply.mapper.createSavingsEvent
import com.qapital.savings.rule.apply.mapper.isAnExpense
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

interface GuiltyPleasureSavingRuleService {
    fun executeRule(savingsRule: SavingsRule): List<SavingsEvent>
}

class GuiltyPleasureSavingRuleServiceImpl(private val transactionsService: TransactionsService) :
    GuiltyPleasureSavingRuleService {


    override fun executeRule(savingsRule: SavingsRule): List<SavingsEvent> {
        val transactions = transactionsService.latestTransactionsForUser(savingsRule.userId)
        return transactions.filter { isEligibleToApplyRule(it, savingsRule) }.flatMap {
            createSavingEventsOnGoals(savingsRule, it)
        }
    }

    private fun isEligibleToApplyRule(
        transaction: Transaction,
        savingsRule: SavingsRule
    ): Boolean {
        return transaction.description.equals(savingsRule.placeDescription, true) &&
                isAnExpense(transaction)
    }

    private fun createSavingEventsOnGoals(
        savingsRule: SavingsRule,
        transaction: Transaction
    ): List<SavingsEvent> {
        val savingAmountOnEachGoal = createSavingAmountOnEachGoalOnTransactionAmount(
            savingsRule.amount,
            savingsRule.savingsGoalIds.size
        )
        return savingsRule.savingsGoalIds.map { goalId: Long ->
            createSavingsEvent(
                savingsRule, goalId,
                savingAmountOnEachGoal,
                transaction = transaction
            )
        }
    }
}

@Configuration
open class GuiltyPleasureSavingRuleServiceConfiguration {

    @Bean
    open fun guiltyPleasureSavingRuleService(transactionsService: TransactionsService): GuiltyPleasureSavingRuleService {
        return GuiltyPleasureSavingRuleServiceImpl(transactionsService)
    }

}