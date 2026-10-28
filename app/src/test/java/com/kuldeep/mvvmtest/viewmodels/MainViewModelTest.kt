package com.kuldeep.mvvmtest.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kuldeep.mvvmtest.getOrAwaitValue
import com.kuldeep.mvvmtest.repository.ProductRepository
import com.kuldeep.mvvmtest.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Mock
    lateinit var repository: ProductRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun test_GetProducts()= runTest{
        Mockito.`when`(repository.getProducts()).thenReturn(NetworkResult.Success(emptyList()))

        val sut = MainViewModel(repository)
        sut.getProducts()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.products.getOrAwaitValue()
        Assert.assertEquals(1, result.data!!.size)
    }
    @Test
    fun test_GetProducts_Expected_Error()= runTest{
        Mockito.`when`(repository.getProducts()).thenReturn(NetworkResult.Error("Something went wrong"))

        val sut = MainViewModel(repository)
        sut.getProducts()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.products.getOrAwaitValue()
        Assert.assertEquals(true, result is NetworkResult.Error)
        Assert.assertEquals("Something went wrong", result.message)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}












