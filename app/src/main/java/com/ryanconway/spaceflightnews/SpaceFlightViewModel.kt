package com.ryanconway.spaceflightnews

import androidx.lifecycle.*
import com.ryanconway.spaceflightnews.domain.Result
import com.ryanconway.spaceflightnews.domain.model.Article
import com.ryanconway.spaceflightnews.domain.repository.ArticleRepository
import com.ryanconway.spaceflightnews.domain.repository.ConnectivityRepository
import com.ryanconway.spaceflightnews.domain.repository.DispatcherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SpaceFlightViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val connectivityRepository: ConnectivityRepository,
    private val dispatchers: DispatcherRepository
) : ViewModel() {

    private var _articles = MediatorLiveData<Result<List<Article>>>()
    val articles: LiveData<Result<List<Article>>>
        get() = _articles

    private val _eventNetworkError = MutableLiveData<Boolean>()
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _eventServerError = MutableLiveData<Boolean>()
    val eventServerError: LiveData<Boolean>
        get() = _eventServerError

    private var _articlesDataSource: LiveData<Result<List<Article>>> = MutableLiveData()

    init {
        _articles.postValue(Result.Loading())
        if (!connectivityRepository.isConnectedToNetwork()) {
            onNetworkError()
        }
    }

    suspend fun getArticles(filter: String?) = withContext(dispatchers.getMain()) {
        _articles.removeSource(_articlesDataSource)
        _articlesDataSource = articleRepository.fetchArticles(filter).asLiveData()
        _articles.addSource(_articlesDataSource) { _articles.postValue(it) }
    }

    fun isConnected() = connectivityRepository.isConnectedToNetwork()

    suspend fun refresh() = withContext(dispatchers.getIO()) {
        if (!connectivityRepository.isConnectedToNetwork()) {
            onNetworkError()
        } else {
            val articles = _articles.value
            if (articles !is Result.Success || articles.value.isEmpty()) {
                _articles.postValue(Result.Loading())
            }

            val result = articleRepository.refreshArticles()
            if (result is Result.Failure) onError()

            getArticles(null)
        }
    }

    suspend fun loadMoreArticles(position: Int) = withContext(dispatchers.getIO()) {
        try {
            articleRepository.fetchArticles(position)
        } catch (e: Exception) {
            onError()
        }
    }

    private fun onError() {
        if (connectivityRepository.isConnectedToNetwork()) {
            onServerError()
        } else {
            onNetworkError()
        }
    }

    private fun onServerError() = _eventServerError.postValue(true)

    fun onDoneShowServerError() = _eventServerError.postValue(null)

    private fun onNetworkError() = _eventNetworkError.postValue(true)

    fun onDoneShowNetworkError() = _eventNetworkError.postValue(null)
}