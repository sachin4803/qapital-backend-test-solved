package com.qapital.savings.rule.apply.mapper

import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicReference

object MapperUtil {
    @JvmField
    var dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

    @JvmField
    val savingId = AtomicReference(10L)
}