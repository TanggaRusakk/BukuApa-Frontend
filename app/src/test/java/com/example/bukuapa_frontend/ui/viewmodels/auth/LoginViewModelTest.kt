package com.example.bukuapa_frontend.ui.viewmodels.auth

import android.app.Application
import io.mockk.mockk
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockkConstructor
import io.mockk.unmockkConstructor
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import com.example.bukuapa_frontend.data.repositories.AuthRepository
import com.example.bukuapa_frontend.utils.TokenManager
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: LoginViewModel
    private val mockApplication = mockk<Application>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkConstructor(TokenManager::class)
        mockkConstructor(AuthRepository::class)
        
        coJustRun { anyConstructed<TokenManager>().saveToken(any()) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkConstructor(TokenManager::class)
        unmockkConstructor(AuthRepository::class)
    }

    @Test
    fun testLoginViewModelInstantiation() {
        viewModel = LoginViewModel(mockApplication)
        Assert.assertNotNull(viewModel)
    }

    @Test
    fun testLoginInitialState() = runTest(testDispatcher) {
        viewModel = LoginViewModel(mockApplication)

        Assert.assertNull(viewModel.loginToken.first())
        Assert.assertNull(viewModel.errorMessage.first())
        Assert.assertFalse(viewModel.isLoading.first())
    }

    @Test
    fun testLoginIsLoadingFlow() = runTest(testDispatcher) {
        viewModel = LoginViewModel(mockApplication)
        val initialLoading = viewModel.isLoading.first()
        Assert.assertFalse(initialLoading)
    }

    @Test
    fun testLoginErrorMessageFlow() = runTest(testDispatcher) {
        viewModel = LoginViewModel(mockApplication)
        val initialError = viewModel.errorMessage.first()
        Assert.assertNull(initialError)
    }

    @Test
    fun testLoginTokenFlow() = runTest(testDispatcher) {
        viewModel = LoginViewModel(mockApplication)
        val initialToken = viewModel.loginToken.first()
        Assert.assertNull(initialToken)
    }

    @Test
    fun testLoginMethodExists() = runTest(testDispatcher) {
        coEvery { anyConstructed<AuthRepository>().login(any(), any()) } returns Result.success("fake_token")
        
        viewModel = LoginViewModel(mockApplication)
        
        viewModel.login("test@example.com", "password")
        advanceUntilIdle()
        
        Assert.assertFalse(viewModel.isLoading.first())
        Assert.assertEquals("fake_token", viewModel.loginToken.first())
    }

    @Test
    fun testLoginMultipleCalls() = runTest(testDispatcher) {
        coEvery { anyConstructed<AuthRepository>().login(any(), any()) } returns Result.success("fake_token")
        
        viewModel = LoginViewModel(mockApplication)

        for (i in 0..2) {
            viewModel.login("test$i@example.com", "password$i")
            advanceUntilIdle()
        }

        Assert.assertFalse(viewModel.isLoading.first())
    }
}
