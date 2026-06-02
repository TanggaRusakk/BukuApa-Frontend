package com.example.bukuapa_frontend.ui.viewmodels.auth

import android.app.Application
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.unmockkConstructor
import io.mockk.unmockkObject
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import com.example.bukuapa_frontend.data.api.ApiClient
import com.example.bukuapa_frontend.utils.TokenManager
import com.example.bukuapa_frontend.data.api.ApiResponse
import com.example.bukuapa_frontend.data.models.User
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AccountViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AccountViewModel
    private val mockApplication = mockk<Application>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(ApiClient)
        mockkConstructor(TokenManager::class)
        
        // Default mocks to prevent crashes
        every { anyConstructed<TokenManager>().getToken() } returns flowOf(null)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkObject(ApiClient)
        unmockkConstructor(TokenManager::class)
    }

    @Test
    fun testAccountViewModelInstantiation() {
        viewModel = AccountViewModel(mockApplication)
        Assert.assertNotNull(viewModel)
    }

    @Test
    fun testAccountInitialState() = runTest(testDispatcher) {
        viewModel = AccountViewModel(mockApplication)

        Assert.assertNull(viewModel.user.first())
        Assert.assertFalse(viewModel.isLoading.first())
    }

    @Test
    fun testAccountIsLoadingFlow() = runTest(testDispatcher) {
        viewModel = AccountViewModel(mockApplication)
        val initialLoading = viewModel.isLoading.first()
        Assert.assertFalse(initialLoading)
    }

    @Test
    fun testAccountUserFlow() = runTest(testDispatcher) {
        viewModel = AccountViewModel(mockApplication)
        val initialUser = viewModel.user.first()
        Assert.assertNull(initialUser)
    }

    @Test
    fun testFetchUserInfoMethodExists() = runTest(testDispatcher) {
        every { anyConstructed<TokenManager>().getToken() } returns flowOf("fake_token")
        coEvery { ApiClient.instance.getCurrentUser(any()) } returns ApiResponse(
            data = User(id = 1, name = "Test User", email = "test@example.com", role = "MEMBER"),
            message = "Success"
        )
        
        viewModel = AccountViewModel(mockApplication)

        viewModel.fetchUserInfo()
        advanceUntilIdle()

        Assert.assertFalse(viewModel.isLoading.first())
        Assert.assertNotNull(viewModel.user.first())
    }

    @Test
    fun testFetchUserInfoMultipleCalls() = runTest(testDispatcher) {
        every { anyConstructed<TokenManager>().getToken() } returns flowOf("fake_token")
        coEvery { ApiClient.instance.getCurrentUser(any()) } returns ApiResponse(
            data = User(id = 1, name = "Test User", email = "test@example.com", role = "MEMBER"),
            message = "Success"
        )
        
        viewModel = AccountViewModel(mockApplication)

        for (i in 0..2) {
            viewModel.fetchUserInfo()
            advanceUntilIdle()
        }

        Assert.assertFalse(viewModel.isLoading.first())
    }

    @Test
    fun testAccountViewModelLifecycle() = runTest(testDispatcher) {
        every { anyConstructed<TokenManager>().getToken() } returns flowOf(null)
        
        viewModel = AccountViewModel(mockApplication)

        viewModel.fetchUserInfo()
        advanceUntilIdle()

        Assert.assertFalse(viewModel.isLoading.first())
        Assert.assertNull(viewModel.user.first())
    }
}
