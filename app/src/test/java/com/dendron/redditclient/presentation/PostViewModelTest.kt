package com.dendron.redditclient.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dendron.redditclient.utils.MainCoroutineScopeRule
import com.dendron.redditclient.utils.POST_LIMIT
import com.dendron.redditclient.domain.PostRepository
import com.dendron.redditclient.domain.ResultWrapper
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.utils.mockPostList
import com.nhaarman.mockitokotlin2.times
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PostViewModelTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Mock
    private lateinit var postRepository: PostRepository

    @Mock
    private lateinit var viewStateObserver: Observer<UiState>

    private lateinit var viewModel: PostViewModel

    @Before
    fun setUp() {
        viewModel = PostViewModel(postRepository).apply {
            getEvents.observeForever(viewStateObserver)
        }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `Given a call to getPost, empty list, should call the repository and emit empty list`() =
        runBlockingTest {

            val list = emptyList<Post>()
            val result = ResultWrapper.Success(list)

            Mockito.`when`(postRepository.getPosts(POST_LIMIT)).thenReturn(result)

            viewModel.getPosts(POST_LIMIT)

            Mockito.verify(postRepository, times(1)).getPosts(POST_LIMIT)
            Mockito.verify(viewStateObserver).onChanged(UiState.Load(list))
        }

    @Test
    fun `Given a call to getPost, error, should call the repository and emit the error`() =
        runBlockingTest {

            val errorMessage = "ERROR"
            val result = ResultWrapper.Error(errorMessage)

            Mockito.`when`(postRepository.getPosts(POST_LIMIT)).thenReturn(result)

            viewModel.getPosts(POST_LIMIT)

            Mockito.verify(postRepository, times(1)).getPosts(POST_LIMIT)
            Mockito.verify(viewStateObserver).onChanged(UiState.Error(errorMessage))
        }

    @Test
    fun `Given a call to getPost, items, should call the repository and emit the items`() =
        runBlockingTest {

            val list = mockPostList()
            val result = ResultWrapper.Success(list)

            Mockito.`when`(postRepository.getPosts(POST_LIMIT)).thenReturn(result)

            viewModel.getPosts(POST_LIMIT)

            Mockito.verify(postRepository, times(1)).getPosts(POST_LIMIT)
            Mockito.verify(viewStateObserver).onChanged(UiState.Load(list))
        }

    @Test
    fun `Given a call to dismissPost, should call the repository and emit post dismissed`() =
        runBlockingTest {

            val post = mockPostList().first()

            viewModel.dismissPost(post)

            Mockito.verify(postRepository, times(1)).dismissPost(post)
        }

    @Test
    fun markPostAsRead() =
        runBlockingTest {

            val post = mockPostList().first()

            viewModel.markPostAsRead(post)

            Mockito.verify(viewStateObserver).onChanged(UiState.PostRead(post))
        }

    @Test
    fun dismissAll() {
        runBlockingTest {

            viewModel.dismissAll()

            Mockito.verify(viewStateObserver).onChanged(UiState.AllDismissed)
        }

    }
}