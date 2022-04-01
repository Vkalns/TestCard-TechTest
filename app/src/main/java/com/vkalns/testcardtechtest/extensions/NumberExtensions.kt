package com.vkalns.testcardtechtest.extensions

import java.math.BigDecimal


fun BigDecimal?.orZero(): BigDecimal = this ?: BigDecimal(0)