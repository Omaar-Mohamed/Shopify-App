package com.example.shopify_app.features.ProductDetails.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DraftViewModelTest {
    val appliedDiscount = AppliedDiscount(
        amount = "10.00",
        description = "Discount description",
        title = "10% Off",
        value = "10.00",
        value_type = "fixed_amount"
    )

    val billingAddress = BillingAddress(
        address1 = "123 Street",
        address2 = "",
        city = "Citytown",
        company = Any(),
        country = "Country",
        country_code = Any(),
        default = true,
        first_name = Any(),
        id = 1,
        last_name = Any(),
        name = Any(),
        phone = "123-456-7890",
        province = "Province",
        province_code = Any(),
        zip = "12345"
    )

    val lineItems = listOf(
        LineItem(/* fill in with appropriate data */)
        // Assuming LineItem is another data class defined elsewhere
    )

    val paymentTerms = PaymentTerms(
        amount = 100,
        currency = "USD",
        due_in_days = 30,
        payment_schedules = listOf(
            PaymentSchedule(
                currency = "usd",
                amount = 5,
                due_at = "",
                issued_at = "",
                completed_at = "",
                expected_payment_method = ""
            )
            // Assuming PaymentSchedule is another data class defined elsewhere
        ),
        payment_terms_name = "Net 30",
        payment_terms_type = "standard"
    )

    val shippingAddress = ShippingAddress(
        address1 = "456 Avenue",
        address2 = "",
        city = "Citytown",
        company = Any(),
        country = "Country",
        country_code = "US",
        first_name = "John",
        last_name = "Doe",
        latitude = "40.7128",
        longitude = "-74.0060",
        name = "John Doe",
        phone = "987-654-3210",
        province = "Province",
        province_code = "NY",
        zip = "54321"
    )

    val shippingLine = ShippingLine(
        handle = "standard",
        price = 10,
        title = "Standard Shipping"
    )

    val draftOrder = DraftOrder(
        applied_discount = appliedDiscount,
        billing_address = billingAddress,
        completed_at = "2024-06-25T12:00:00Z",  // Example date/time string
        created_at = "2024-06-25T10:00:00Z",    // Example date/time string
        currency = "USD",
        customer = null,  // Replace with actual customer data if available
        email = "john.doe@example.com",
        id = 123456789,
        invoice_sent_at = null,
        invoice_url = "https://example.com/invoice123",
        line_items = lineItems,
        name = "Draft Order #123",
        note = null,
        note_attributes = null,  // Replace with actual note attributes if available
        order_id = null,  // Replace with actual order ID data if available
        payment_terms = paymentTerms,
        shipping_address = shippingAddress,
        shipping_line = shippingLine,
        source_name = "web",
        status = "draft",
        subtotal_price = "90.00",
        tags = "tag1,tag2",
        tax_exempt = false,
        tax_exemptions = null,
        tax_lines = null,
        taxes_included = false,
        total_price = "100.00",
        total_tax = "10.00",
        updated_at = null
    )

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repo: ProductsDetailsRepo

    private lateinit var viewModel: DraftViewModel

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DraftViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    private fun advanceTimeByy(timeMillis: Long) {
        // Function to advance coroutine time by specified milliseconds
        (testDispatcher as? TestCoroutineDispatcher)?.let {
            it.scheduler.apply {
                advanceTimeBy(timeMillis)
                runCurrent()
            }
        }
    }

    @Test
    fun `test getDraftOrder`() = testScope.runBlockingTest {
        val draftOrder = draftOrder
        val draftOrderResponse = DraftOrderResponse(draftOrder)

        `when`(repo.getDraftOrder("1")).thenReturn(flowOf(draftOrderResponse))

        viewModel.getDraftOrder("1")

        advanceTimeByy(100) // Adjust the time as per your coroutine timeout

        val currentState = viewModel.cartDraft.first()

        when (currentState) {
            is ApiState.Loading -> {
                // Do something if it's still loading
                println("Loading...")
            }
            is ApiState.Success -> {
                assertEquals(draftOrderResponse, currentState.data)

                // Verify that the repository method was called with the correct parameter
                verify(repo).getDraftOrder("1")
            }
            is ApiState.Failure -> {
                fail("Unexpected failure state: ${(currentState as ApiState.Failure).error}")
            }
        }
    }

    @Test
    fun `test addLineItemToDraft`() = testScope.runBlockingTest {
        val draftOrder = draftOrder
        val draftOrderResponse = DraftOrderResponse(draftOrder)
        val newLineItem = LineItem(variant_id = 0)

        `when`(repo.getDraftOrder("1")).thenReturn(flowOf(draftOrderResponse))
//        `when`(repo.updateDraftOrder(anyString(), any())).thenReturn(flowOf(draftOrderResponse))

        viewModel.addLineItemToDraft("1", newLineItem)

        advanceTimeByy(100) // Adjust the time as per your coroutine timeout

        val currentState = viewModel.updateDraftResponse.first()

        when (currentState) {
            is ApiState.Loading -> {
                // Do something if it's still loading
                println("Loading...")
            }
            is ApiState.Success -> {
                assertEquals(draftOrderResponse, currentState.data)

                // Verify that the repository method was called with the correct parameter
                verify(repo).updateDraftOrder(anyString(), any())
            }
            is ApiState.Failure -> {
                fail("Unexpected failure state: ${(currentState as ApiState.Failure).error}")
            }
        }
    }

    @Test
    fun `test removeLineItemFromDraft`() = testScope.runBlockingTest {
        val lineItem = LineItem(variant_id = 0)
        val draftOrder = draftOrder
        val draftOrderResponse = DraftOrderResponse(draftOrder)

        `when`(repo.getDraftOrder("1")).thenReturn(flowOf(draftOrderResponse))
//        `when`(repo.updateDraftOrder(anyString(), any())).thenReturn(flowOf(draftOrderResponse))

        viewModel.removeLineItemFromDraft("1", lineItem)

        advanceTimeByy(100) // Adjust the time as per your coroutine timeout

        val currentState = viewModel.updateDraftResponse.first()

        when (currentState) {
            is ApiState.Loading -> {
                // Do something if it's still loading
                println("Loading...")
            }
            is ApiState.Success -> {
                assertEquals(draftOrderResponse, currentState.data)

                // Verify that the repository method was called with the correct parameter
                verify(repo).updateDraftOrder(anyString(), any())
            }
            is ApiState.Failure -> {
                fail("Unexpected failure state: ${(currentState as ApiState.Failure).error}")
            }
        }
    }
}

// Utility class for setting the main dispatcher for tests
@ExperimentalCoroutinesApi
class MainCoroutineRule(val dispatcher: TestDispatcher = UnconfinedTestDispatcher()) :
    TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher) {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
