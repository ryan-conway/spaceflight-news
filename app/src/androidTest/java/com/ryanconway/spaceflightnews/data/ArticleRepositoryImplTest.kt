package com.ryanconway.spaceflightnews.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ryanconway.spaceflightnews.androidtestutil.DummyData
import com.ryanconway.spaceflightnews.androidtestutil.TestCoroutineScopeRule
import com.ryanconway.spaceflightnews.androidtestutil.getOrAwaitValue
import com.ryanconway.spaceflightnews.data.cache.ArticleCacheImpl
import com.ryanconway.spaceflightnews.data.cache.room.SpaceFlightDatabase
import com.ryanconway.spaceflightnews.data.datasource.ArticleApi
import com.ryanconway.spaceflightnews.data.datasource.ArticleCache
import com.ryanconway.spaceflightnews.data.repository.ArticleRepositoryImpl
import com.ryanconway.spaceflightnews.domain.model.Article
import com.ryanconway.spaceflightnews.domain.repository.ArticleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.ryanconway.spaceflightnews.domain.Result

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ArticleRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testScopeRule = TestCoroutineScopeRule()

    private lateinit var database: SpaceFlightDatabase
    private lateinit var cache: ArticleCache
    private lateinit var api: ArticleApi

    private lateinit var repository: ArticleRepository

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, SpaceFlightDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        cache = ArticleCacheImpl(database)
        api = ArticleApiFake()
        repository = ArticleRepositoryImpl(cache, api)
    }

    @After
    fun teardown() {
        database.clearAllTables()
    }

    @Test
    fun getArticles_noData_returnEmptyList() = testScopeRule.runBlockingTest {
        val expected = DummyData.articles.take(0)

        val result = repository.fetchArticles(null).asLiveData().getOrAwaitValue()
        val actual = (result as Result.Success).value

        assertEquals(expected.size, actual.size)
    }

    @Test
    fun getArticles_refresh_returnFiveItems() = testScopeRule.runBlockingTest {
        val expected = DummyData.articles.take(5)

        repository.refreshArticles()
        val result = repository.fetchArticles(null).asLiveData().getOrAwaitValue()
        val actual = (result as Result.Success).value

        assertEquals(expected.size, actual.size)
    }

    @Test
    fun getArticles_refreshTwice_returnFiveItems() = testScopeRule.runBlockingTest {
        val expected = DummyData.articles.take(5)

        repository.refreshArticles()
        repository.refreshArticles()
        val result = repository.fetchArticles(null).asLiveData().getOrAwaitValue()
        val actual = (result as Result.Success).value

        assertEquals(expected.size, actual.size)
        assertEquals(expected[0], actual[0])
        assertEquals(expected[actual.lastIndex], actual[actual.lastIndex])
    }

    @Test
    fun getArticles_fetchNextPage_returnTenItems() = testScopeRule.runBlockingTest {
        val expected = DummyData.articles.take(10)

        repository.refreshArticles()
        repository.fetchArticles(5)
        val result = repository.fetchArticles(null).asLiveData().getOrAwaitValue()
        val actual = (result as Result.Success).value

        assertEquals(expected.size, actual.size)
        assertEquals(expected[0], actual[0])
        assertEquals(expected[actual.lastIndex], actual[actual.lastIndex])
    }

    @Test
    fun getArticles_fetchNextPageThenRefresh_returnFiveItems() = testScopeRule.runBlockingTest {
        val expected = DummyData.articles.take(5)

        repository.refreshArticles()
        repository.fetchArticles(5)
        repository.refreshArticles()
        val result = repository.fetchArticles(null).asLiveData().getOrAwaitValue()
        val actual = (result as Result.Success).value

        assertEquals(expected.size, actual.size)
        assertEquals(expected[0], actual[0])
        assertEquals(expected[actual.lastIndex], actual[actual.lastIndex])
    }

    @Test
    fun getArticles_fetchNextPageOutOfBounds_returnFiveItems() = testScopeRule.runBlockingTest {
        val expected = DummyData.articles.take(5)

        repository.refreshArticles()
        repository.fetchArticles(50)
        repository.refreshArticles()
        val result = repository.fetchArticles(null).asLiveData().getOrAwaitValue()
        val actual = (result as Result.Success).value

        assertEquals(expected.size, actual.size)
        assertEquals(expected[0], actual[0])
        assertEquals(expected[actual.lastIndex], actual[actual.lastIndex])
    }

    @Test
    fun getArticles_filterBySpaceX_returnTwoItems() = testScopeRule.runBlockingTest {
        val filter = "sPaCeX"
        val expected = DummyData.articles.take(5).filter { it.title.contains(filter, ignoreCase = true) }

        repository.refreshArticles()
        val result = repository.fetchArticles(filter).asLiveData().getOrAwaitValue()
        val actual = (result as Result.Success).value

        assertEquals(expected.size, actual.size)
        assertEquals(expected[0], actual[0])
        assertEquals(expected[actual.lastIndex], actual[actual.lastIndex])
    }

    @Test
    fun getArticles_filterBySpaceXCaseInsensitive_returnTwoItems() = testScopeRule.runBlockingTest {
        val filter = "sPaCeX"
        val expected = DummyData.articles.take(5).filter { it.title.contains(filter, ignoreCase = true) }

        repository.refreshArticles()
        val result = repository.fetchArticles(filter).asLiveData().getOrAwaitValue()
        val actual = (result as Result.Success).value

        assertEquals(expected.size, actual.size)
        assertEquals(expected[0], actual[0])
        assertEquals(expected[actual.lastIndex], actual[actual.lastIndex])
    }

}