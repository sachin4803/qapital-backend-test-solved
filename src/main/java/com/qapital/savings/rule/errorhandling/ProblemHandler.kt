package com.qapital.savings.rule.errorhandling

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.ControllerAdvice
import org.zalando.problem.jackson.ProblemModule
import org.zalando.problem.spring.web.advice.ProblemHandling
import org.zalando.problem.violations.ConstraintViolationProblemModule


@ControllerAdvice
class ExceptionHandler : ProblemHandling

@Configuration
open class ProblemConfiguration {
    @Bean
    open fun problemModule(): ProblemModule {
        return ProblemModule()
    }

    @Bean
    open fun objectMapper(): ObjectMapper? {
        return ObjectMapper()
            .registerModule(ProblemModule())
            .registerModule(ConstraintViolationProblemModule())
    }
}
