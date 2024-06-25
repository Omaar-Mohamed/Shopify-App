import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.categories.data.model.CustomCategoriesResponse
import com.example.shopify_app.features.categories.data.repo.CategoriesRepo
import com.example.shopify_app.features.categories.viewmodel.CategoriesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CategoriesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var categoriesRepo: CategoriesRepo

    private lateinit var categoriesViewModel: CategoriesViewModel

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        categoriesViewModel = CategoriesViewModel(categoriesRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `test getCategories`() = testScope.runBlockingTest {
        val categoriesResponse = CustomCategoriesResponse(listOf(/* add test data here */))

        `when`(categoriesRepo.getCategories()).thenReturn(flowOf(categoriesResponse))

        categoriesViewModel.getCategories()

        advanceTimeByy(100) // Adjust the time as per your coroutine timeout

        val currentState = categoriesViewModel.categories.first()

        when (currentState) {
            is ApiState.Loading -> {
                println("Loading...")
            }
            is ApiState.Success -> {
                assertEquals(categoriesResponse, currentState.data)

                // Verify that the repository method was called with the correct parameter
                verify(categoriesRepo).getCategories()
            }
            is ApiState.Failure -> {
                fail("Unexpected failure state: ${(currentState as ApiState.Failure).error}")
            }
        }
    }

    private fun advanceTimeByy(timeMillis: Long) {
        (testDispatcher as? TestCoroutineDispatcher)?.let {
            it.scheduler.apply {
                advanceTimeBy(timeMillis)
                runCurrent()
            }
        }
    }
}
