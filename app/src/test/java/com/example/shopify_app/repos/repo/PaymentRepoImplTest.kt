package com.example.shopify_app.repos.repo

import com.example.shopify_app.core.models.CheckoutRequest
import com.example.shopify_app.core.models.CheckoutResponse
import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.payment.data.repo.PaymentRepo
import com.example.shopify_app.features.payment.data.repo.PaymentRepoImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class PaymentRepoImplTest {

    private lateinit var appRemoteDataSource: AppRemoteDataSourse
    private lateinit var paymentRepo: PaymentRepo

    @Before
    fun setUp() {
        appRemoteDataSource = mock(AppRemoteDataSourse::class.java)
        paymentRepo = PaymentRepoImpl(appRemoteDataSource)
    }

    @Test
    fun `createCheckout should return CheckoutResponse`(): Unit = runTest {
        // Arrange
        val checkoutRequest = CheckoutRequest(
            line_items = listOf(),
            success_url = "http://success.url",
            cancel_url = "http://cancel.url",
            mode = "payment",
            customer_email = "customer@example.com"
        )
        val checkoutResponse = CheckoutResponse(
            id = "12345",
            url = "http://checkout.url",
        )

        `when`(appRemoteDataSource.createCheckout(checkoutRequest)).thenReturn(flowOf(checkoutResponse))

        // Act
        val result = paymentRepo.createCheckout(checkoutRequest)

        // Assert
        result.collect { response ->
            assertEquals(checkoutResponse, response)
        }
        verify(appRemoteDataSource, times(1)).createCheckout(checkoutRequest)
    }
}