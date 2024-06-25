import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.home.data.models.LoginCustomer.Customer
import com.example.shopify_app.features.home.data.models.LoginCustomer.LoginCustomer
import com.example.shopify_app.features.home.data.models.ProductsResponse.Product
import com.example.shopify_app.features.home.data.models.ProductsResponse.ProductsResponse
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRule
import com.example.shopify_app.features.home.data.models.priceRulesResponse.PriceRulesResponse
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollection
import com.example.shopify_app.features.home.data.models.smartcollection.SmartCollectionResponse
import com.example.shopify_app.features.home.data.repo.HomeRepo
import com.example.shopify_app.features.home.data.repo.HomeRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeRepoImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var appRemoteDataSourse: AppRemoteDataSourse

    private lateinit var homeRepo: HomeRepo

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        homeRepo = HomeRepoImpl.getInstance(appRemoteDataSourse)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
        resetHomeRepoSingleton()
    }

    private fun resetHomeRepoSingleton() {
        val field = HomeRepoImpl::class.java.getDeclaredField("instance")
        field.isAccessible = true
        field.set(null, null)
    }

    @Test
    fun `test getCustomer`() = testScope.runTest {
        val email = "test@example.com"
        val loginCustomer = LoginCustomer(listOf(
            mock(Customer::class.java)
        ))

        `when`(appRemoteDataSourse.getCustomer(email)).thenReturn(flowOf(loginCustomer))

        val result = homeRepo.getCustomer(email).first()

        assertEquals(loginCustomer, result)

        verify(appRemoteDataSourse).getCustomer(email)
    }

    @Test
    fun `test getPriceRules`() = testScope.runTest {
        val priceRulesResponse = PriceRulesResponse(listOf(mock(PriceRule::class.java)))

        `when`(appRemoteDataSourse.getPriceRules()).thenReturn(flowOf(priceRulesResponse))

        val result = homeRepo.getPriceRules().first()

        assertEquals(priceRulesResponse, result)

        verify(appRemoteDataSourse).getPriceRules()
    }

    @Test
    fun `test getSmartCollections`() = testScope.runTest {
        val smartCollectionResponse = SmartCollectionResponse(listOf(mock(SmartCollection::class.java)))

        `when`(appRemoteDataSourse.getBrandsRules()).thenReturn(flowOf(smartCollectionResponse))

        val result = homeRepo.getSmartCollections().first()

        assertEquals(smartCollectionResponse, result)

        verify(appRemoteDataSourse).getBrandsRules()
    }

    @Test
    fun `test getProducts`() = testScope.runTest {
        val productsResponse = ProductsResponse(listOf(mock(Product::class.java)))

        `when`(appRemoteDataSourse.getProducts()).thenReturn(flowOf(productsResponse))

        val result = homeRepo.getProducts().first()

        assertEquals(productsResponse, result)

        verify(appRemoteDataSourse).getProducts()
    }
}
