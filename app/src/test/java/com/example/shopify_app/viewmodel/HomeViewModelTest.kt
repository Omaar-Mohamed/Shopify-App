import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.home.data.models.LoginCustomer.Customer
import com.example.shopify_app.features.home.data.models.LoginCustomer.LoginCustomer
import com.example.shopify_app.features.home.data.models.ProductsResponse.Product
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRule
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollection
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import com.example.shopify_app.features.home.data.repo.HomeRepo
import com.example.shopify_app.features.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var repository: HomeRepo

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getCustomer`() = runTest {
        val email = "test@example.com"
        val mockCustomer = mock(Customer::class.java)
        val loginCustomer = LoginCustomer(listOf(mockCustomer))

        `when`(repository.getCustomer(email)).thenReturn(flow { emit(loginCustomer) })

        viewModel.getCustomer(email)

        assertEquals(ApiState.Success(loginCustomer), viewModel.customer.value)

        verify(repository).getCustomer(email)
    }

    @Test
    fun `test getPriceRules`() = runTest {
        val mockPriceRule = mock(PriceRule::class.java)
        val priceRulesResponse = PriceRulesResponse(listOf(mockPriceRule))

        `when`(repository.getPriceRules()).thenReturn(flow { emit(priceRulesResponse) })

        viewModel.getPriceRules()

        assertEquals(ApiState.Success(priceRulesResponse), viewModel.priceRules.value)

        verify(repository).getPriceRules()
    }

    @Test
    fun `test getSmartCollections`() = runTest {
        val mockSmartCollection = mock(SmartCollection::class.java)
        val smartCollectionResponse = SmartCollectionResponse(listOf(mockSmartCollection))

        `when`(repository.getSmartCollections()).thenReturn(flow { emit(smartCollectionResponse) })

        viewModel.getSmartCollections()

        assertEquals(ApiState.Success(smartCollectionResponse), viewModel.smartCollections.value)

        verify(repository).getSmartCollections()
    }

    @Test
    fun `test getProducts`() = runTest {
        val mockProduct = mock(Product::class.java)
        val productsResponse = ProductsResponse(listOf(mockProduct))

        `when`(repository.getProducts()).thenReturn(flow { emit(productsResponse) })

        viewModel.getProducts()

        val shuffledProducts = productsResponse.products.shuffled().take(10)
        assertEquals(ApiState.Success(ProductsResponse(shuffledProducts)), viewModel.products.value)

        verify(repository).getProducts()
    }
}
