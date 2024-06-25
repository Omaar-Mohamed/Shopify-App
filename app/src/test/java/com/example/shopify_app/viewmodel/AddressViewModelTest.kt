import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopify_app.core.networking.ApiState
import com.example.shopify_app.features.personal_details.data.model.*
import com.example.shopify_app.features.personal_details.data.repo.PersonalRepo
import com.example.shopify_app.features.personal_details.viewmodels.AddressViewModel
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

    private val testDispatcher = UnconfinedTestDispatcher()
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

    private fun advanceTimeByy(timeMillis: Long) {
        // Function to advance coroutine time by specified milliseconds
        (testDispatcher as? TestCoroutineDispatcher)?.let {
            it.scheduler.apply {
                advanceTimeBy(
                    timeMillis
                ); runCurrent()
            }
        }
    }

    @Test
    fun `test getAddresses`() = testScope.runBlockingTest {
        val customerId = "123"

        // Create AddressX instances with actual data
        val addressX1 = AddressX(
            address1 = "123 Main St",
            address2 = "",
            city = "Springfield",
            company = "ACME Inc.",
            country = "US",
            country_code = "US",
            country_name = "United States",
            customer_id = 1,
            default = true,
            first_name = "John",
            id = 1,
            last_name = "Doe",
            name = "John Doe",
            phone = "123-456-7890",
            province = "IL",
            province_code = "IL",
            zip = "12345"
        )

        val addressX2 = AddressX(
            address1 = "456 Oak Ave",
            address2 = "Apt 2",
            city = "Oakland",
            company = "XYZ Corp.",
            country = "US",
            country_code = "US",
            country_name = "United States",
            customer_id = 2,
            default = false,
            first_name = "Jane",
            id = 2,
            last_name = "Smith",
            name = "Jane Smith",
            phone = "987-654-3210",
            province = "CA",
            province_code = "CA",
            zip = "54321"
        )

        val addressResponse = AddressResponse(listOf(addressX1, addressX2))

        // Stub the repository method to return a flow of AddressResponse
        `when`(personalRepo.getAddresses(customerId)).thenReturn(flowOf(addressResponse))

        // Call the method in your ViewModel that fetches addresses
        addressViewModel.getAddresses(customerId)

        // Ensure that the coroutine advances to the next step
        advanceTimeByy(100) // Adjust the time as per your coroutine timeout

        // Retrieve the current state from the ViewModel
        val currentState = addressViewModel.addresses.first()

        // Assertions
        when (currentState) {
            is ApiState.Loading -> {
                // Do something if it's still loading
                println("Loading...")
            }
            is ApiState.Success -> {
                assertEquals(addressResponse, currentState.data)

                // Verify that the repository method was called with the correct parameter
                verify(personalRepo).getAddresses(customerId)
            }
            is ApiState.Failure -> {
                fail("Unexpected failure state: ${(currentState as ApiState.Failure).error}")
            }
        }
    }

    @Test
    fun `test addAddress`() = testScope.runBlockingTest {
        val customerId = "123"
        val addressX = AddressX(
            address1 = "123 Main St",
            address2 = "",
            city = "Springfield",
            company = "ACME Inc.",
            country = "US",
            country_code = "US",
            country_name = "United States",
            customer_id = 1,
            default = true,
            first_name = "John",
            id = 1,
            last_name = "Doe",
            name = "John Doe",
            phone = "123-456-7890",
            province = "IL",
            province_code = "IL",
            zip = "12345"
        )
        val address = PostAddressRequest(addressX)
        val postAddressResponse = PostAddressResponse(addressX)
        `when`(personalRepo.addAddresses(customerId, address)).thenReturn(flowOf(postAddressResponse))

        addressViewModel.addAddress(customerId, address)

        advanceTimeByy(100)

        val currentState = addressViewModel.addResponse.first()
        when (currentState) {
            is ApiState.Loading -> {
                // Do something if it's still loading
                println("Loading...")
            }
            is ApiState.Success -> {
                assertEquals(postAddressResponse, currentState.data)

                // Verify that the repository method was called with the correct parameter
                verify(personalRepo).getAddresses(customerId)
            }
            is ApiState.Failure -> {
                fail("Unexpected failure state: ${(currentState as ApiState.Failure).error}")
            }
        }
    }

    @Test
    fun `test updateAddress`() = testScope.runBlockingTest {
        val customerId = "123"
        val addressId = "456"
        val addressX = AddressX(
            address1 = "456 Oak Ave",
            address2 = "Apt 2",
            city = "Oakland",
            company = "XYZ Corp.",
            country = "US",
            country_code = "US",
            country_name = "United States",
            customer_id = 2,
            default = false,
            first_name = "Jane",
            id = 2,
            last_name = "Smith",
            name = "Jane Smith",
            phone = "987-654-3210",
            province = "CA",
            province_code = "CA",
            zip = "54321"
        )
        val address = PostAddressRequest(addressX)
        val postAddressResponse = PostAddressResponse(addressX)
        `when`(personalRepo.updateAddress(customerId, addressId, address)).thenReturn(flowOf(postAddressResponse))

        addressViewModel.updateAddress(customerId, addressId, address)

        advanceTimeByy(100)

        val currentState = addressViewModel.addResponse.first()
        when (currentState) {
            is ApiState.Loading -> {
                // Do something if it's still loading
                println("Loading...")
            }
            is ApiState.Success -> {
                assertEquals(postAddressResponse, currentState.data)

                // Verify that the repository method was called with the correct parameter
                verify(personalRepo).getAddresses(customerId)
            }
            is ApiState.Failure -> {
                fail("Unexpected failure state: ${(currentState as ApiState.Failure).error}")
            }
        }
    }

    @Test
    fun `test makeAddressDefault`() = testScope.runBlockingTest {
        val customerId = "123"
        val addressId = "456"
        val addressX = AddressX(
            address1 = "456 Oak Ave",
            address2 = "Apt 2",
            city = "Oakland",
            company = "XYZ Corp.",
            country = "US",
            country_code = "US",
            country_name = "United States",
            customer_id = 2,
            default = false,
            first_name = "Jane",
            id = 2,
            last_name = "Smith",
            name = "Jane Smith",
            phone = "987-654-3210",
            province = "CA",
            province_code = "CA",
            zip = "54321"
        )
        val postAddressResponse = PostAddressResponse(addressX)
        val addressResponse = AddressResponse(listOf(addressX))
        `when`(personalRepo.makeAddressDefault(customerId, addressId)).thenReturn(flowOf(postAddressResponse))

        addressViewModel.makeAddressDefault(customerId, addressId)

        advanceTimeByy(100)

        val currentState = addressViewModel.addResponse.first()
        when (currentState) {
            is ApiState.Loading -> {
                // Do something if it's still loading
                println("Loading...")
            }
            is ApiState.Success -> {
                assertEquals(postAddressResponse, currentState.data)

                // Verify that the repository method was called with the correct parameter
                verify(personalRepo).getAddresses(customerId)
            }
            is ApiState.Failure -> {
                fail("Unexpected failure state: ${(currentState as ApiState.Failure).error}")
            }
        }
    }

    @Test
    fun `test deleteAddress`() = testScope.runBlockingTest {
        // Prepare test data
        val customerId = "123"
        val addressId = "456"
        val addressX = AddressX(
            address1 = "456 Oak Ave",
            address2 = "Apt 2",
            city = "Oakland",
            company = "XYZ Corp.",
            country = "US",
            country_code = "US",
            country_name = "United States",
            customer_id = 2,
            default = false,
            first_name = "Jane",
            id = 2,
            last_name = "Smith",
            name = "Jane Smith",
            phone = "987-654-3210",
            province = "CA",
            province_code = "CA",
            zip = "54321"
        )
        val postAddressResponse = PostAddressResponse(addressX)
        val addressResponse = AddressResponse(listOf(addressX))

        // Stub the repository methods
        `when`(personalRepo.deleteAddress(customerId, addressId)).thenReturn(flowOf(postAddressResponse))
        `when`(personalRepo.getAddresses(customerId)).thenReturn(flowOf(addressResponse))

        // Call the method in your ViewModel that deletes an address
        addressViewModel.deleteAddress(customerId, addressId)

        // Ensure that the coroutine advances to the next step
        advanceTimeByy(100) // Adjust the time as per your coroutine timeout

        // Retrieve the current state from the ViewModel for deleteResponse
        val currentStateDelete = addressViewModel.deleteResponse.first()

        // Assertions for deleteResponse
        when (currentStateDelete) {
            is ApiState.Loading -> {
                // Do something if it's still loading
                println("Deleting Address... Still Loading...")
            }
            is ApiState.Success -> {
                assertEquals(postAddressResponse, currentStateDelete.data)

                // Verify that the repository method was called with the correct parameter
                verify(personalRepo).deleteAddress(customerId, addressId)
            }
            is ApiState.Failure -> {
                fail("Unexpected failure state: ${(currentStateDelete as ApiState.Failure).error}")
            }
        }

        // Retrieve the current state from the ViewModel for addresses
        val currentStateAddresses = addressViewModel.addresses.first()

        // Assertions for addresses
        when (currentStateAddresses) {
            is ApiState.Loading -> {
                // Do something if it's still loading
                println("Fetching Addresses... Still Loading...")
            }
            is ApiState.Success -> {
                assertEquals(addressResponse, currentStateAddresses.data)

                // Verify that the repository method was called with the correct parameter
                verify(personalRepo).getAddresses(customerId)
            }
            is ApiState.Failure -> {
                fail("Unexpected failure state: ${(currentStateAddresses as ApiState.Failure).error}")
            }
        }
    }

}
