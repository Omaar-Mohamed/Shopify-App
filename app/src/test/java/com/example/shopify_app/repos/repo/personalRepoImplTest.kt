package com.example.shopify_app.features.personal_details.data.repo

import com.example.shopify_app.core.networking.AppRemoteDataSourse
import com.example.shopify_app.features.personal_details.data.model.AddressResponse
import com.example.shopify_app.features.personal_details.data.model.PostAddressRequest
import com.example.shopify_app.features.personal_details.data.model.PostAddressResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class PersonalRepoImplTest {

    private lateinit var appRemoteDataSource: AppRemoteDataSourse
    private lateinit var personalRepo: PersonalRepoImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        appRemoteDataSource = mock(AppRemoteDataSourse::class.java)
        personalRepo = PersonalRepoImpl(appRemoteDataSource)
    }

    @Test
    fun `test getAddresses`() = runBlockingTest {
        val customerId = "123"
        val addressResponse = mock(AddressResponse::class.java)
        `when`(appRemoteDataSource.getAddresses(customerId)).thenReturn(flow { emit(addressResponse) })

        val result: Flow<AddressResponse> = personalRepo.getAddresses(customerId)

        result.collect { response ->
            assertEquals(response, addressResponse)
        }

        verify(appRemoteDataSource).getAddresses(customerId)
    }

    @Test
    fun `test addAddresses`() = runBlockingTest {
        val customerId = "123"
        val address = mock(PostAddressRequest::class.java)
        val postAddressResponse = mock(PostAddressResponse::class.java)
        `when`(appRemoteDataSource.addAddress(customerId, address)).thenReturn(flow { emit(postAddressResponse) })

        val result: Flow<PostAddressResponse> = personalRepo.addAddresses(customerId, address)

        result.collect { response ->
            assertEquals(response, postAddressResponse)
        }

        verify(appRemoteDataSource).addAddress(customerId, address)
    }

    @Test
    fun `test updateAddress`() = runBlockingTest {
        val customerId = "123"
        val addressId = "456"
        val address = mock(PostAddressRequest::class.java)
        val postAddressResponse = mock(PostAddressResponse::class.java)
        `when`(appRemoteDataSource.updateAddress(customerId, addressId, address)).thenReturn(flow { emit(postAddressResponse) })

        val result: Flow<PostAddressResponse> = personalRepo.updateAddress(customerId, addressId, address)

        result.collect { response ->
            assertEquals(response, postAddressResponse)
        }

        verify(appRemoteDataSource).updateAddress(customerId, addressId, address)
    }

    @Test
    fun `test makeAddressDefault`() = runBlockingTest {
        val customerId = "123"
        val addressId = "456"
        val postAddressResponse = mock(PostAddressResponse::class.java)
        `when`(appRemoteDataSource.makeAddressDefault(customerId, addressId)).thenReturn(flow { emit(postAddressResponse) })

        val result: Flow<PostAddressResponse> = personalRepo.makeAddressDefault(customerId, addressId)

        result.collect { response ->
            assertEquals(response, postAddressResponse)
        }

        verify(appRemoteDataSource).makeAddressDefault(customerId, addressId)
    }

    @Test
    fun `test deleteAddress`() = runBlockingTest {
        val customerId = "123"
        val addressId = "456"
        val postAddressResponse = mock(PostAddressResponse::class.java)
        `when`(appRemoteDataSource.deleteAddress(customerId, addressId)).thenReturn(flow { emit(postAddressResponse) })

        val result: Flow<PostAddressResponse> = personalRepo.deleteAddress(customerId, addressId)

        result.collect { response ->
            assertEquals(response, postAddressResponse)
        }

        verify(appRemoteDataSource).deleteAddress(customerId, addressId)
    }
}
