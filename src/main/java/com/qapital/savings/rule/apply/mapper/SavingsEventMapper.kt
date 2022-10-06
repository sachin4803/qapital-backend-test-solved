package com.qapital.savings.rule.apply.mapper

import com.qapital.bankdata.transaction.Transaction
import com.qapital.savings.event.SavingsEvent
import com.qapital.savings.rule.SavingsRule
import com.qapital.savings.rule.apply.mapper.MapperUtil.savingId
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDateTime


fun isAnExpense(transaction: Transaction): Boolean {
    return transaction.amount < BigDecimal.ZERO
}

fun createSavingsEvent(
    savingsRule: SavingsRule, savingGoalId: Long, savingAmount: BigDecimal, transaction: Transaction
): SavingsEvent {
    return SavingsEvent(
        savingId.getAndSet(savingId.get() + 1),
        savingsRule.userId,
        savingGoalId,
        savingsRule.id,
        SavingsEvent.EventName.rule_application,
        MapperUtil.dateTimeFormatter.format(transaction.date.atStartOfDay()),
        savingAmount,
        transaction.id,
        savingsRule,
        MapperUtil.dateTimeFormatter.format(LocalDateTime.now())
    )
}

fun createSavingAmountOnEachGoalOnTransactionAmount(
    savingAmountOnCompleteGoal: BigDecimal, numberOfGoals: Int
): BigDecimal {
    return savingAmountOnCompleteGoal.divide(
        BigDecimal(numberOfGoals), MathContext(2, RoundingMode.HALF_UP)
    )
}

fun createSavingAmountRoundUpGoal(
    transactionAmount: BigDecimal,
    roundingUpAmount: BigDecimal
): BigDecimal {
    val roundedUpAmount: BigDecimal = if (transactionAmount > roundingUpAmount) {
        val dividedNumber = transactionAmount.divide(roundingUpAmount, MathContext(2, RoundingMode.HALF_UP))
        roundingUpAmount.multiply(
            BigDecimal(dividedNumber.toInt() + 1)
        ).setScale(2, RoundingMode.HALF_UP)
    } else {
        roundingUpAmount
    }
    return roundedUpAmount.subtract(transactionAmount)
}