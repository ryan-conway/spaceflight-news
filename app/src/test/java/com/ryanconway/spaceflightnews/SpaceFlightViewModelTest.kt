package com.ryanconway.spaceflightnews

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ryanconway.spaceflightnews.domain.Result
import com.ryanconway.spaceflightnews.domain.model.Article
import com.ryanconway.spaceflightnews.domain.repository.ArticleRepository
import com.ryanconway.spaceflightnews.domain.repository.ConnectivityRepository
import com.ryanconway.spaceflightnews.domain.repository.DispatcherRepository
import com.ryanconway.spaceflightnews.testutil.DummyData
import com.ryanconway.spaceflightnews.testutil.TestCoroutineScopeRule
import com.ryanconway.spaceflightnews.testutil.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Exception

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SpaceFlightViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testScopeRule = TestCoroutineScopeRule()

    private val dispatchers = mock(DispatcherRepository::class.java)
    private val articleRepository = mock(ArticleRepository::class.java)
    private val connectivityRepository = mock(ConnectivityRepository::class.java)

    @Before
    fun setUp() {
        `when`(dispatchers.getIO()).thenReturn(testScopeRule.testDispatcher)
        `when`(dispatchers.getMain()).thenReturn(Dispatchers.Main)
    }

    @Test
    fun `has no internet connection`() = testScopeRule.runBlockingTest {
        val expected = true

        `when`(connectivityRepository.isConnectedToNetwork()).thenReturn(false)
        val viewModel = SpaceFlightViewModel(articleRepository, connectivityRepository, dispatchers)

        val actual = viewModel.eventNetworkError.getOrAwaitValue()

        assertEquals(expected, actual)
    }

    @Test
    fun `is connected returns true when connected`() = testScopeRule.runBlockingTest {
        val expected = true
        `when`(connectivityRepository.isConnectedToNetwork()).thenReturn(expected)
        val viewModel = SpaceFlightViewModel(articleRepository, connectivityRepository, dispatchers)

        val actual = viewModel.isConnected()

        assertEquals(expected, actual)
    }

    @Test
    fun `is connected returns false when disconnected`() = testScopeRule.runBlockingTest {
        val expected = false
        `when`(connectivityRepository.isConnectedToNetwork()).thenReturn(expected)
        val viewModel = SpaceFlightViewModel(articleRepository, connectivityRepository, dispatchers)

        val actual = viewModel.isConnected()

        assertEquals(expected, actual)
    }

    @Test
    fun `has server error during refresh`() = testScopeRule.runBlockingTest {
        val expected = true

        val articleRepo = object : MockableArticleRepository() {
            override suspend fun refreshArticles(): Result<Unit> = Result.Failure(Exception())
            override fun fetchArticles(filter: String?): Flow<Result<List<Article>>> = flowOf()
        }

        `when`(connectivityRepository.isConnectedToNetwork()).thenReturn(true)
        val viewModel = SpaceFlightViewModel(articleRepo, connectivityRepository, dispatchers)
        viewModel.refresh()

        val actual = viewModel.eventServerError.getOrAwaitValue()

        assertEquals(expected, actual)
    }

    @Test
    fun `has network error during refresh`() = testScopeRule.runBlockingTest {
        val expected = true

        val articleRepo = object : MockableArticleRepository() {
            override suspend fun refreshArticles(): Result<Unit> = Result.Failure(Exception())
            override fun fetchArticles(filter: String?): Flow<Result<List<Article>>> = flowOf()
        }

        `when`(connectivityRepository.isConnectedToNetwork()).thenReturn(true, true, false)
        val viewModel = SpaceFlightViewModel(articleRepo, connectivityRepository, dispatchers)
        viewModel.refresh()

        val actual = viewModel.eventNetworkError.getOrAwaitValue()

        assertEquals(expected, actual)
    }

    @Test
    fun `get articles has value`() = testScopeRule.runBlockingTest {
        `when`(articleRepository.fetchArticles(null)).thenReturn(flowOf(Result.Success(DummyData.articles)))
        val viewModel = SpaceFlightViewModel(articleRepository, connectivityRepository, dispatchers)
        viewModel.getArticles(null)

        val articleResult = viewModel.articles.getOrAwaitValue()
        assertTrue(articleResult is Result.Success)

        val articles = articleResult as Result.Success
        assertEquals(DummyData.articles.size, articles.value.size)
    }
}