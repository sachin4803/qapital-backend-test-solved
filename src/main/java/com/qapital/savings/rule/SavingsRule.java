package com.qapital.savings.rule;


import com.qapital.savings.rule.validator.ValidSavingGoalIds;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The core configuration object for a Savings Rule.
 */
public class SavingsRule {
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    @NotBlank
    private String placeDescription;
    @Positive
    private BigDecimal amount;
    @ValidSavingGoalIds
    private List<Long> savingsGoalIds;
    private RuleType ruleType;
    private Status status;

    public SavingsRule() {
    }

    public static SavingsRule createGuiltyPleasureRule(
            Long id, Long userId, String placeDescription, BigDecimal penaltyAmount
    ) {
        SavingsRule guiltyPleasureRule = new SavingsRule();
        guiltyPleasureRule.setId(id);
        guiltyPleasureRule.setUserId(userId);
        guiltyPleasureRule.setPlaceDescription(placeDescription);
        guiltyPleasureRule.setAmount(penaltyAmount);
        guiltyPleasureRule.setSavingsGoalIds(new ArrayList<>());
        guiltyPleasureRule.setRuleType(RuleType.guilty_pleasure);
        guiltyPleasureRule.setStatus(Status.active);
        return guiltyPleasureRule;
    }

    public static SavingsRule createRoundupRule(Long id, Long userId, BigDecimal roundupToNearest) {
        SavingsRule roundupRule = new SavingsRule();
        roundupRule.setId(id);
        roundupRule.setUserId(userId);
        roundupRule.setAmount(roundupToNearest);
        roundupRule.setSavingsGoalIds(new ArrayList<>());
        roundupRule.setRuleType(RuleType.roundup);
        roundupRule.setStatus(Status.active);
        return roundupRule;
    }

    public void addSavingsGoal(Long savingsGoalId) {
        if (!savingsGoalIds.contains(savingsGoalId)) {
            savingsGoalIds.add(savingsGoalId);
        }
    }

    public void removeSavingsGoal(Long savingsGoalId) {
        savingsGoalIds.remove(savingsGoalId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<Long> getSavingsGoalIds() {
        return savingsGoalIds;
    }

    public void setSavingsGoalIds(List<Long> savingsGoalIds) {
        this.savingsGoalIds = savingsGoalIds;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isActive() {
        return Status.active.equals(getStatus());
    }

    public enum RuleType {
        guilty_pleasure, roundup
    }

    public enum Status {
        active, deleted, paused
    }

}
