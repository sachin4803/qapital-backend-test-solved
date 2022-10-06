package com.qapital.savings.rule.validator

import com.qapital.savings.rule.SavingsRule.Status
import com.qapital.savings.rule.SavingsRule.Status.active
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_GETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [SavingGoalIdsValidator::class])
@MustBeDocumented
annotation class ValidSavingGoalIds(
    val message: String = "There should be at least one goal id present in the request.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Payload>> = []
)

class SavingGoalIdsValidator : ConstraintValidator<ValidSavingGoalIds, List<Long>> {
    override fun initialize(validSavingGoalIds: ValidSavingGoalIds) {}
    override fun isValid(savingsGoalIds: List<Long>, constraintValidatorContext: ConstraintValidatorContext) =
        savingsGoalIds.isNotEmpty()
}

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_GETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [StatusValidator::class])
@MustBeDocumented
annotation class ValidStatus(
    val message: String = "Saving rule must be active to apply on transactions",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Payload>> = []
)

class StatusValidator : ConstraintValidator<ValidStatus, Status> {
    override fun initialize(validStatus: ValidStatus) {}
    override fun isValid(status: Status, constraintValidatorContext: ConstraintValidatorContext) =
        active == status
}
