package com.example.bukuapa_frontend.ui.viewmodels.auth

import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import com.example.bukuapa_frontend.data.repositories.AuthRepository
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkConstructor(AuthRepository::class)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkConstructor(AuthRepository::class)
    }

    @Test
    fun testRegisterViewModelInstantiation() {
        viewModel = RegisterViewModel()
        Assert.assertNotNull(viewModel)
    }

    @Test
    fun testRegisterInitialState() = runTest(testDispatcher) {
        viewModel = RegisterViewModel()

        Assert.assertFalse(viewModel.isSuccess.first())
        Assert.assertNull(viewModel.errorMessage.first())
        Assert.assertFalse(viewModel.isLoading.first())
    }

    @Test
    fun testRegisterIsLoadingFlow() = runTest(testDispatcher) {
        viewModel = RegisterViewModel()
        val initialLoading = viewModel.isLoading.first()
        Assert.assertFalse(initialLoading)
    }

    @Test
    fun testRegisterErrorMessageFlow() = runTest(testDispatcher) {
        viewModel = RegisterViewModel()
        val initialError = viewModel.errorMessage.first()
        Assert.assertNull(initialError)
    }

    @Test
    fun testRegisterSuccessFlow() = runTest(testDispatcher) {
        viewModel = RegisterViewModel()
        val initialSuccess = viewModel.isSuccess.first()
        Assert.assertFalse(initialSuccess)
    }

    @Test
    fun testRegisterMethodExists() = runTest(testDispatcher) {
        coEvery { anyConstructed<AuthRepository>().register(any(), any(), any()) } returns Result.success(true)
        
        viewModel = RegisterViewModel()

        viewModel.register("John Doe", "john@example.com", "password123")
        advanceUntilIdle()

        Assert.assertFalse(viewModel.isLoading.first())
        Assert.assertTrue(viewModel.isSuccess.first())
    }

    @Test
    fun testRegisterWithDifferentInputs() = runTest(testDispatcher) {
        coEvery { anyConstructed<AuthRepository>().register(any(), any(), any()) } returns Result.success(true)
        
        viewModel = RegisterViewModel()

        viewModel.register("Test User", "test@example.com", "password123")
        advanceUntilIdle()
        val firstErrorMessage = viewModel.errorMessage.first()

        viewModel.register("Another User", "another@example.com", "password456")
        advanceUntilIdle()
        val secondErrorMessage = viewModel.errorMessage.first()

        Assert.assertNull(secondErrorMessage)
    }

    @Test
    fun testRegisterMultipleCalls() = runTest(testDispatcher) {
        coEvery { anyConstructed<AuthRepository>().register(any(), any(), any()) } returns Result.success(true)
        
        viewModel = RegisterViewModel()

        for (i in 0..2) {
            viewModel.register("User $i", "user$i@example.com", "password$i")
            advanceUntilIdle()
        }

        Assert.assertFalse(viewModel.isLoading.first())
    }
}
