import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.shopify_app.core.models.CheckoutRequest
import com.example.shopify_app.core.models.CheckoutResponse
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.payment.data.repo.PaymentRepo
import com.example.shopify_app.features.payment.viewmodels.PaymentViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class PaymentViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var paymentRepo: PaymentRepo
    private lateinit var paymentViewModel: PaymentViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        paymentRepo = mock(PaymentRepo::class.java)
        paymentViewModel = PaymentViewModel(paymentRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `createCheckout should post Success state`() = runTest {
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
            url = "http://checkout.url"
        )

        val flow = flowOf(checkoutResponse)
        `when`(paymentRepo.createCheckout(checkoutRequest)).thenReturn(flow)

        // Act
        paymentViewModel.createCheckout(checkoutRequest)

        // Assert
        paymentViewModel.checkoutResponse.test {
            assertEquals(ApiState.Loading, awaitItem())
            assertEquals(ApiState.Success(checkoutResponse), awaitItem())
        }
    }
}
