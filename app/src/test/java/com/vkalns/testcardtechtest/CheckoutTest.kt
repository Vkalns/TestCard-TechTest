package com.vkalns.testcardtechtest

import com.vkalns.testcardtechtest.extensions.orZero
import com.vkalns.testcardtechtest.model.Checkout
import com.vkalns.testcardtechtest.model.Deal
import com.vkalns.testcardtechtest.model.Product
import com.vkalns.testcardtechtest.model.ProductType
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal


class CheckoutTest {

    //region Empty Checkout
    @Test
    fun emptyCheckoutReturnsZeroFinalPayableAmount() {
        val emptyCheckout = Checkout()
        assertEquals(BigDecimal(0), emptyCheckout.getFinalPayableAmount())
    }

    @Test
    fun emptyCheckoutReturnsZeroCheckoutFullAmount() {
        val emptyCheckout = Checkout()
        assertEquals(BigDecimal(0), emptyCheckout.getFullAmount())
    }

    @Test
    fun emptyCheckoutReturnsZeroTotalDiscount() {
        val emptyCheckout = Checkout()
        assertEquals(BigDecimal(0), emptyCheckout.getTotalDiscount())
    }
    //endregion

    //region Single discounted product
    @Test
    fun discountedProductInCheckoutShowsCorrectFinalAmount() {
        val checkout = Checkout()
        val product = Product(ProductType.DAIRY, "Milk", BigDecimal("1.00"), BigDecimal("0.10"))
        checkout.productList.add(product)
        assertEquals(product.fullPrice - product.discountAmount.orZero(), checkout.getFinalPayableAmount())
    }

    @Test
    fun discountedProductInCheckoutShowsCorrectFullAmount() {
        val checkout = Checkout()
        val product = Product(ProductType.DAIRY, "Milk", BigDecimal("1.00"), BigDecimal("0.10"))
        checkout.productList.add(product)
        assertEquals(product.fullPrice, checkout.getFullAmount())
    }

    @Test
    fun discountedProductInCheckoutShowsCorrectDiscountAmount() {
        val checkout = Checkout()
        val product = Product(ProductType.DAIRY, "Milk", BigDecimal("1.00"), BigDecimal("0.10"))
        checkout.productList.add(product)
        assertEquals(product.discountAmount, checkout.getTotalDiscount())
    }
    //endregion

    //region Two discounted products
    @Test
    fun twoDiscountedProductsInCheckoutShowsCorrectFinalAmount() {
        val checkout = Checkout()
        val product1 = Product(ProductType.DAIRY, "Milk", BigDecimal("1.00"), BigDecimal("0.10"))
        val product2 = Product(ProductType.BREAD, "Seedel Loaf", BigDecimal("1.50"), BigDecimal("0.25"))
        checkout.productList.add(product1)
        checkout.productList.add(product2)
        assertEquals((product1.fullPrice + product2.fullPrice) - (product1.discountAmount.orZero() + product2.discountAmount.orZero()), checkout.getFinalPayableAmount())
    }

    @Test
    fun twoDiscountedProductsInCheckoutShowsCorrectFullAmount() {
        val checkout = Checkout()
        val product1 = Product(ProductType.DAIRY, "Milk", BigDecimal("1.00"), BigDecimal("0.10"))
        val product2 = Product(ProductType.BREAD, "Seedel Loaf", BigDecimal("1.50"), BigDecimal("0.25"))
        checkout.productList.add(product1)
        checkout.productList.add(product2)
        assertEquals(product1.fullPrice + product2.fullPrice, checkout.getFullAmount())
    }

    @Test
    fun twoDiscountedProductsInCheckoutShowsCorrectDiscountAmount() {
        val checkout = Checkout()
        val product1 = Product(ProductType.DAIRY, "Milk", BigDecimal("1.00"), BigDecimal("0.10"))
        val product2 = Product(ProductType.BREAD, "Seeded Loaf", BigDecimal("1.50"), BigDecimal("0.25"))
        checkout.productList.add(product1)
        checkout.productList.add(product2)
        assertEquals(product1.discountAmount.orZero() + product2.discountAmount.orZero(), checkout.getTotalDiscount())
    }
    //endregion

    //region Multi Deal applied
    @Test
    fun threeProductMealDealInCheckoutShowsCorrectFinalAmount() {
        val checkout = Checkout()
        val product1 = Product(ProductType.SANDWICH, "Ham and cheese Sandwich", BigDecimal("2.20"))
        val product2 = Product(ProductType.SNACK, "Ready salted crisps", BigDecimal("1.10"))
        val product3 = Product(ProductType.SOFT_DRINK, "Bottle of pepsi", BigDecimal("1.20"))
        val mealDeal = Deal(listOf(ProductType.SANDWICH, ProductType.SNACK, ProductType.SOFT_DRINK), BigDecimal("3.50"))
        checkout.activeDeals.add(mealDeal)
        checkout.productList.add(product1)
        checkout.productList.add(product2)
        checkout.productList.add(product3)
        assertEquals(mealDeal.dealDiscountedPrice, checkout.getFinalPayableAmount())
    }

    @Test
    fun threeProductMealDealInCheckoutShowsCorrectFullAmount() {
        val checkout = Checkout()
        val product1 = Product(ProductType.SANDWICH, "Ham and cheese Sandwich", BigDecimal("2.20"))
        val product2 = Product(ProductType.SNACK, "Ready salted crisps", BigDecimal("1.10"))
        val product3 = Product(ProductType.SOFT_DRINK, "Bottle of pepsi", BigDecimal("1.20"))
        val mealDeal = Deal(listOf(ProductType.SANDWICH, ProductType.SNACK, ProductType.SOFT_DRINK), BigDecimal("3.50"))
        checkout.activeDeals.add(mealDeal)
        checkout.productList.add(product1)
        checkout.productList.add(product2)
        checkout.productList.add(product3)
        assertEquals(product1.fullPrice + product2.fullPrice + product3.fullPrice, checkout.getFullAmount())
    }

    @Test
    fun threeProductMealDealInCheckoutShowsCorrectDiscountAmount() {
        val checkout = Checkout()
        val product1 = Product(ProductType.SANDWICH, "Ham and cheese Sandwich", BigDecimal("2.20"))
        val product2 = Product(ProductType.SNACK, "Ready salted crisps", BigDecimal("1.10"))
        val product3 = Product(ProductType.SOFT_DRINK, "Bottle of pepsi", BigDecimal("1.20"))
        val mealDeal = Deal(listOf(ProductType.SANDWICH, ProductType.SNACK, ProductType.SOFT_DRINK), BigDecimal("3.50"))
        checkout.activeDeals.add(mealDeal)
        checkout.productList.add(product1)
        checkout.productList.add(product2)
        checkout.productList.add(product3)
        assertEquals((product1.fullPrice + product2.fullPrice + product3.fullPrice) - mealDeal.dealDiscountedPrice, checkout.getTotalDiscount())
    }
    // endregion

    //region Two For One deal applied
    @Test
    fun twoForOneProductDealInCheckoutShowsCorrectFinalAmount() {
        val checkout = Checkout()
        val product1 = Product(ProductType.SOFT_DRINK, "Bottle of Coca-cola", BigDecimal("1.50"))
        val product2 = Product(ProductType.SOFT_DRINK, "Bottle of Coca-cola", BigDecimal("1.50"))
        val mealDeal = Deal(listOf(ProductType.SOFT_DRINK, ProductType.SOFT_DRINK), BigDecimal("1.50"))
        checkout.activeDeals.add(mealDeal)
        checkout.productList.add(product1)
        checkout.productList.add(product2)
        assertEquals(mealDeal.dealDiscountedPrice, checkout.getFinalPayableAmount())
    }

    @Test
    fun twoForOneProductDealInCheckoutShowsCorrectFullAmount() {
        val checkout = Checkout()
        val product1 = Product(ProductType.SOFT_DRINK, "Bottle of Coca-cola", BigDecimal("1.50"))
        val product2 = Product(ProductType.SOFT_DRINK, "Bottle of Coca-cola", BigDecimal("1.50"))
        val mealDeal = Deal(listOf(ProductType.SOFT_DRINK, ProductType.SOFT_DRINK), BigDecimal("1.50"))
        checkout.activeDeals.add(mealDeal)
        checkout.productList.add(product1)
        checkout.productList.add(product2)
        assertEquals(product1.fullPrice + product2.fullPrice, checkout.getFullAmount())
    }

    @Test
    fun twoForOneProductDealInCheckoutShowsCorrectDiscountAmount() {
        val checkout = Checkout()
        val product1 = Product(ProductType.SOFT_DRINK, "Bottle of Coca-cola", BigDecimal("1.50"))
        val product2 = Product(ProductType.SOFT_DRINK, "Bottle of Coca-cola", BigDecimal("1.50"))
        val mealDeal = Deal(listOf(ProductType.SOFT_DRINK, ProductType.SOFT_DRINK), BigDecimal("1.50"))
        checkout.activeDeals.add(mealDeal)
        checkout.productList.add(product1)
        checkout.productList.add(product2)
        assertEquals(mealDeal.dealDiscountedPrice, checkout.getTotalDiscount())
    }
    //endregion
}