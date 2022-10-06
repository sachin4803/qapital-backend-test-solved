package com.qapital

import com.qapital.bankdata.transaction.TransactionsService
import com.qapital.savings.rule.SavingsRulesService
import com.qapital.savings.rule.StandardSavingsRulesService
import com.qapital.savings.rule.apply.guiltpleasure.GuiltyPleasureSavingRuleServiceImpl
import com.qapital.savings.rule.apply.roundup.RoundUpSavingRuleServiceImpl
import java.math.BigDecimal
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class StandardSavingRulesServiceTest : TestBase() {
    lateinit var savingsRulesService: SavingsRulesService

    @Mock
    lateinit var transactionsService: TransactionsService

    @BeforeEach
    fun init() {
        Mockito.`when`(transactionsService.latestTransactionsForUser(1L)).thenReturn(
            listOf(
                createTransaction(10L, 1L, -3.5, "starbucks"),
                createTransaction(20L, 1L, -10.26, "weekendParty"),
                createTransaction(30L, 1L, 1000.09, "salary"),
                createTransaction(40L, 1L, -27.23, "weekendParty")
            )
        )
        savingsRulesService = StandardSavingsRulesService(
            GuiltyPleasureSavingRuleServiceImpl(transactionsService),
            RoundUpSavingRuleServiceImpl(transactionsService)
        )
    }

    @Test
    fun test_guilty_pleasure_saving_rules_on_weekend_party() {
        val savingsEvents = savingsRulesService.executeRule(createGuiltyPleasureGoal("weekendparty", 5.77))
        Assertions.assertEquals(savingsEvents.size, 4)
        savingsEvents.forEach { savingsEvent ->
            Assertions.assertEquals(
                savingsEvent.amount.compareTo(BigDecimal.valueOf(2.9)), 0
            )
        }
    }

    @Test
    fun test_guilty_pleasure_saving_rules_on_unknown_transaction() {
        val savingsEvents = savingsRulesService.executeRule(createGuiltyPleasureGoal("travel", 5.77))
        Assertions.assertEquals(savingsEvents.size, 0)
    }

    @Test
    fun test_round_up_saving_rules_on_transactions() {
        val savingsEvents = savingsRulesService.executeRule(createRoundUpGoal(6.0))
        Assertions.assertEquals(savingsEvents.size, 9)
        savingsEvents.forEach { savingsEvent ->
            if (savingsEvent.triggerId == 10L) {
                Assertions.assertEquals(savingsEvent.amount.compareTo(BigDecimal.valueOf(0.83)), 0)
            }
            if (savingsEvent.triggerId == 20L) {
                Assertions.assertEquals(savingsEvent.amount.compareTo(BigDecimal.valueOf(0.58)), 0)
            }
            if (savingsEvent.triggerId == 40L) {
                Assertions.assertEquals(savingsEvent.amount.compareTo(BigDecimal.valueOf(0.92)), 0)
            }
        }
    }
}