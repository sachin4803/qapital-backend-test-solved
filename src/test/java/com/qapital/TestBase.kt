package com.qapital

import com.qapital.bankdata.transaction.Transaction
import com.qapital.savings.rule.SavingsRule
import java.math.BigDecimal
import java.time.LocalDate

open class TestBase {
    protected fun createGuiltyPleasureGoal(description: String?, amount: Double): SavingsRule {
        val savingsRule = SavingsRule.createGuiltyPleasureRule(
            112L,
            1L,
            description,
            BigDecimal.valueOf(amount)
        )
        savingsRule.addSavingsGoal(10L)
        savingsRule.addSavingsGoal(11L)
        return savingsRule
    }

    protected fun createRoundUpGoal(amount: Double): SavingsRule {
        val savingsRule = SavingsRule.createRoundupRule(
            111L,
            1L,
            BigDecimal.valueOf(amount)
        )
        savingsRule.addSavingsGoal(10L)
        savingsRule.addSavingsGoal(11L)
        savingsRule.addSavingsGoal(12L)
        return savingsRule
    }

    protected fun createTransaction(id: Long?, userId: Long?, amount: Double, description: String?): Transaction {
        return Transaction(id, userId, BigDecimal.valueOf(amount), description, LocalDate.of(2015, 7, 1))
    }
}