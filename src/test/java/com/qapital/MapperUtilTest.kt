package com.qapital

import com.qapital.savings.rule.SavingsRule
import com.qapital.savings.rule.apply.mapper.MapperUtil
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MapperUtilTest {
    @Test
    fun test_date_format_to_iso() {
        val localDateTime = LocalDateTime.of(LocalDate.of(2015, 7, 1), LocalTime.of(23, 59, 22))
        val localDateTimeString = MapperUtil.dateTimeFormatter.format(localDateTime)
        Assertions.assertEquals("2015-07-01T23:59:22Z", localDateTimeString)
    }
}