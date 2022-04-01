package com.vkalns.testcardtechtest.model

import com.vkalns.testcardtechtest.extensions.orZero
import java.math.BigDecimal


class Checkout {
    var productList: MutableList<Product> = mutableListOf()
    var activeDeals: MutableList<Deal> = mutableListOf()
    private var dealsProducts: MutableList<Product> = mutableListOf()

    fun getFullAmount(): BigDecimal {
        return if (productList.isNotEmpty()) {
            var fullAmount = BigDecimal(0)
            for (product in productList) {
                fullAmount += product.fullPrice
            }
            fullAmount
        } else {
            BigDecimal(0)
        }
    }

    fun getTotalDiscount(): BigDecimal {
        return if (productList.isNotEmpty()) {
            var totalDiscount = BigDecimal(0)
            totalDiscount += findActiveDealDiscounts()
            for (product in productList) {
                if (!dealsProducts.contains(product)) {
                    totalDiscount += product.discountAmount.orZero()
                }
            }
            totalDiscount
        } else {
            BigDecimal(0)
        }
    }

    fun getFinalPayableAmount(): BigDecimal {
        return if (productList.isNotEmpty()) {
            getFullAmount() - getTotalDiscount()
        } else {
            BigDecimal(0)
        }

    }

    private fun findActiveDealDiscounts(): BigDecimal {
        var totalDealsDiscount = BigDecimal(0)
        if (activeDeals.isNotEmpty()) {
            for (deal in activeDeals) {
                var dealMatchingTypeCounter = 0
                var fullComboPriceBeforeDeal = BigDecimal(0)
                val listOfDealProducts = mutableListOf<Product>()
                for (dealProductType in deal.productTypeCombo) {
                    for (product in productList) {
                        if (product.type == dealProductType) {
                            dealMatchingTypeCounter++
                            fullComboPriceBeforeDeal += product.fullPrice
                            listOfDealProducts.add(product)
                            break
                        }
                    }
                }
                if (deal.productTypeCombo.size == dealMatchingTypeCounter) {
                    //we have found a combo deal apply the discount
                    totalDealsDiscount += fullComboPriceBeforeDeal - deal.dealDiscountedPrice
                    dealsProducts.addAll(listOfDealProducts) // keeping the track of which products are in combo deal
                }
            }
        }
        return totalDealsDiscount
    }
}