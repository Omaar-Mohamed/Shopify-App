import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.personal_details.data.model.*
import com.example.shopify_app.features.personal_details.data.repo.PersonalRepo
import com.example.shopify_app.features.personal_details.viewmodels.AddressViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddressViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var personalRepo: PersonalRepo

    private lateinit var addressViewModel: AddressViewModel

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        addressViewModel = AddressViewModel(personalRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `test getAddresses`() = testScope.runBlockingTest {
        val customerId = "123"
        val addressX = mock(AddressX::class.java)
        val addressResponse = AddressResponse(listOf(addressX))
        `when`(personalRepo.getAddresses(customerId)).thenReturn(flowOf(addressResponse))

        addressViewModel.getAddresses(customerId)

        val currentState : ApiState<AddressResponse> = addressViewModel.addresses.first()
        println("test getAddresses: current state ${(currentState as ApiState.Success).data}")
        println("test getAddresses: addressResponse $addressResponse")
        assertTrue(currentState is ApiState.Success)
        assertEquals(addressResponse, (currentState as ApiState.Success).data)

        verify(personalRepo).getAddresses(customerId)
    }

    @Test
    fun `test addAddress`() = testScope.runBlockingTest {
        val customerId = "123"
        val addressX = mock(AddressX::class.java)
        val address = PostAddressRequest(addressX)
        val postAddressResponse = PostAddressResponse(addressX)
        `when`(personalRepo.addAddresses(customerId, address)).thenReturn(flowOf(postAddressResponse))

        addressViewModel.addAddress(customerId, address)

        val currentState = addressViewModel.addResponse.first()
        assertTrue(currentState is ApiState.Success)
        assertEquals(postAddressResponse, (currentState as ApiState.Success).data)

        verify(personalRepo).addAddresses(customerId, address)
    }

    @Test
    fun `test updateAddress`() = testScope.runBlockingTest {
        val customerId = "123"
        val addressId = "456"
        val addressX = mock(AddressX::class.java)
        val address = PostAddressRequest(addressX)
        val postAddressResponse = PostAddressResponse(addressX)
        `when`(personalRepo.updateAddress(customerId, addressId, address)).thenReturn(flowOf(postAddressResponse))

        addressViewModel.updateAddress(customerId, addressId, address)

        val currentState = addressViewModel.updateResponse.first()
        assertTrue(currentState is ApiState.Success)
        assertEquals(postAddressResponse, (currentState as ApiState.Success).data)

        verify(personalRepo).updateAddress(customerId, addressId, address)
    }

    @Test
    fun `test makeAddressDefault`() = testScope.runBlockingTest {
        val customerId = "123"
        val addressId = "456"
        val addressX = mock(AddressX::class.java)
        val postAddressResponse = PostAddressResponse(addressX)
        val addressResponse = AddressResponse(listOf(addressX))
        `when`(personalRepo.makeAddressDefault(customerId, addressId)).thenReturn(flowOf(postAddressResponse))
        `when`(personalRepo.getAddresses(customerId)).thenReturn(flowOf(addressResponse))

        addressViewModel.makeAddressDefault(customerId, addressId)

        val currentState = addressViewModel.addresses.first()
        assertTrue(currentState is ApiState.Success)
        assertEquals(addressResponse, (currentState as ApiState.Success).data)

        verify(personalRepo).makeAddressDefault(customerId, addressId)
        verify(personalRepo).getAddresses(customerId)
    }

    @Test
    fun `test deleteAddress`() = testScope.runBlockingTest {
        val customerId = "123"
        val addressId = "456"
        val addressX = mock(AddressX::class.java)
        val postAddressResponse = PostAddressResponse(addressX)
        val addressResponse = AddressResponse(listOf(addressX))
        `when`(personalRepo.deleteAddress(customerId, addressId)).thenReturn(flowOf(postAddressResponse))
        `when`(personalRepo.getAddresses(customerId)).thenReturn(flowOf(addressResponse))

        addressViewModel.deleteAddress(customerId, addressId)

        val currentStateDelete = addressViewModel.deleteResponse.first()
        assertTrue(currentStateDelete is ApiState.Success)
        assertEquals(postAddressResponse, (currentStateDelete as ApiState.Success).data)

        val currentStateAddresses = addressViewModel.addresses.first()
        assertTrue(currentStateAddresses is ApiState.Success)
        assertEquals(addressResponse, (currentStateAddresses as ApiState.Success).data)

        verify(personalRepo).deleteAddress(customerId, addressId)
        verify(personalRepo).getAddresses(customerId)
    }
}
