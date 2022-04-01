package com.vkalns.testcardtechtest.model

import java.math.BigDecimal


data class Deal (
    var productTypeCombo: List<ProductType> = listOf(),
    val dealDiscountedPrice: BigDecimal = BigDecimal(0)
)