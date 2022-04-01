package com.vkalns.testcardtechtest.model

import java.math.BigDecimal


data class Product (
    val type: ProductType,
    val name: String,
    val fullPrice: BigDecimal,
    val discountAmount: BigDecimal? = null
    )

enum class ProductType {
    FRUIT, SANDWICH, SNACK, SOFT_DRINK, DAIRY, BREAD
}