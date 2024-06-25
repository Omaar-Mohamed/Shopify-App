import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.categories.data.model.CustomCategoriesResponse
import com.example.shopify_app.features.categories.data.model.CustomCollection
import com.example.shopify_app.features.categories.data.repo.CategoriesRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CategoriesRepoImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var appRemoteDataSourse: AppRemoteDataSourse

    private lateinit var categoriesRepo: CategoriesRepoImpl

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        categoriesRepo = CategoriesRepoImpl.getInstance(appRemoteDataSourse)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `test getCategories`() = testScope.runBlockingTest {
        val categoriesResponse = CustomCategoriesResponse(listOf(
            mock(CustomCollection::class.java)
        ))

        `when`(appRemoteDataSourse.getCustomCollections()).thenReturn(flowOf(categoriesResponse))

        val result = categoriesRepo.getCategories().first()

        assertEquals(categoriesResponse, result)

        verify(appRemoteDataSourse).getCustomCollections()
    }
}
