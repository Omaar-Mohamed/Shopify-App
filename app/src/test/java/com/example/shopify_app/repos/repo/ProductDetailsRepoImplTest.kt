import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.ProductDetails.data.model.Image
import com.example.shopify_app.features.ProductDetails.data.model.ImageX
import com.example.shopify_app.features.ProductDetails.data.model.Option
import com.example.shopify_app.features.ProductDetails.data.model.Product
import com.example.shopify_app.features.ProductDetails.data.model.ProductDetailResponse
import com.example.shopify_app.features.ProductDetails.data.model.Variant
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepo
import com.example.shopify_app.features.ProductDetails.data.repo.ProductsDetailsRepoImpl
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.AppliedDiscount
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.BillingAddress
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrder
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.DraftOrderResponse
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.LineItem
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.PaymentSchedule
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.PaymentTerms
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.ShippingAddress
import com.example.shopify_app.features.signup.data.model.DarftOrderRespones.ShippingLine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ProductsDetailsRepoImplTest {
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

    @Mock
    private lateinit var appRemoteDataSource: AppRemoteDataSourse

    private lateinit var productsDetailsRepoImpl: ProductsDetailsRepo

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        productsDetailsRepoImpl = ProductsDetailsRepoImpl(appRemoteDataSource)
    }

    @Test
    fun `test getProductsDetails`() = testScope.runBlockingTest {
        // Mock data
        val productId = "123"
        val image = mock(Image::class.java)

// Mock ImageX class
        val imageX1 = mock(ImageX::class.java)
        val imageX2 = mock(ImageX::class.java)
        val imagesList = listOf(imageX1, imageX2)

// Mock Option class
        val option1 = mock(Option::class.java)
        val option2 = mock(Option::class.java)
        val optionsList = listOf(option1, option2)

// Mock Variant class
        val variant1 = mock(Variant::class.java)
        val variant2 = mock(Variant::class.java)
        val variantsList = listOf(variant1, variant2)

// Now mock the Product class
        val product = Product(
            admin_graphql_api_id = "admin_graphql_api_id",
            body_html = "<html><body>Product description</body></html>",
            created_at = "2024-06-24T10:15:30Z",
            handle = "product-handle",
            id = 123456789L,
            image = image,
            images = imagesList,
            options = optionsList,
            product_type = "product type",
            published_at = "2024-06-24T10:15:30Z",
            published_scope = "global",
            status = "active",
            tags = "tag1, tag2, tag3", // You can mock as per your requirement
            title = "Product Title",
            updated_at = "2024-06-25T08:20:45Z",
            variants = variantsList,
            vendor = "Vendor Name",
        )
        val expectedResponse = ProductDetailResponse(product)
        `when`(appRemoteDataSource.getProductsDetails(productId)).thenReturn(flowOf(expectedResponse))

        // Call the repository method
        val flowResult = productsDetailsRepoImpl.getProductsDetails(productId)

        // Retrieve the result from the flow
        val actualResponse = flowResult.first()

        // Assert
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test getDraftOrder`() = testScope.runBlockingTest {

        // Mock data
        val orderId = "456"
        val expectedResponse = DraftOrderResponse(draftOrder)
        `when`(appRemoteDataSource.getDraftOrder(orderId)).thenReturn(flowOf(expectedResponse))

        // Call the repository method
        val flowResult = productsDetailsRepoImpl.getDraftOrder(orderId)

        // Retrieve the result from the flow
        val actualResponse = flowResult.first()

        // Assert
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test updateDraftOrder`() = testScope.runBlockingTest {
        // Mock data
        val orderId = "456"
        val newDraftOrder = draftOrder
        val expectedResponse = DraftOrderResponse(draftOrder)
        `when`(appRemoteDataSource.updateDraftOrder(orderId, newDraftOrder)).thenReturn(flowOf(expectedResponse))

        // Call the repository method
        val flowResult = productsDetailsRepoImpl.updateDraftOrder(orderId, newDraftOrder)

        // Retrieve the result from the flow
        val actualResponse = flowResult.first()

        // Assert
        assertEquals(expectedResponse, actualResponse)
    }
}
