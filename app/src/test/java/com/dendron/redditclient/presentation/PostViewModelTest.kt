package com.dendron.redditclient.presentation

import app.cash.turbine.test
import com.dendron.redditclient.domain.PostRepository
import com.dendron.redditclient.domain.ResultWrapper
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.utils.MainCoroutineScopeRule
import com.dendron.redditclient.utils.POST_LIMIT
import com.dendron.redditclient.utils.mockPostList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PostViewModelTest {

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Mock
    private lateinit var postRepository: PostRepository

    private lateinit var viewModel: PostViewModel

    @Before
    fun setUp() {
        viewModel = PostViewModel(postRepository)
    }

    @Test
    fun `Given a call to refreshPosts, empty list, should call the repository and emit empty list`() =
        coroutineScope.dispatcher.runBlockingTest {

            val expected = emptyList<Post>()

            Mockito.`when`(postRepository.getPosts()).thenReturn(flowOf(emptyList()))
            Mockito.`when`(postRepository.refreshPosts(POST_LIMIT))
                .thenReturn(ResultWrapper.Success)

            viewModel.refreshPosts(POST_LIMIT)

            viewModel.posts.test {
                assert(expectItem() == expected)
                expectComplete()
            }

            viewModel.events.test {
                assert(expectItem() == UiState.LoadingFinished)
            }

            Mockito.verify(postRepository, times(1)).refreshPosts(POST_LIMIT)
        }

    @Test
    fun `Given a call to refreshPosts, items, should call the repository and emit the items`() =
        runBlockingTest {

            val list = mockPostList()

            Mockito.`when`(postRepository.getPosts()).thenReturn(flowOf(list))
            Mockito.`when`(postRepository.refreshPosts(POST_LIMIT))
                .thenReturn(ResultWrapper.Success)

            viewModel.refreshPosts(POST_LIMIT)

            viewModel.posts.test {
                assert(expectItem() == list)
                expectComplete()
            }

            viewModel.events.test {
                assert(expectItem() == UiState.LoadingFinished)
            }

            Mockito.verify(postRepository, times(1)).refreshPosts(POST_LIMIT)
        }

    @Test
    fun `Given a call to refreshPosts, error, should call the repository and emit the error`() =
        runBlockingTest {

            val list = emptyList<Post>()

            val errorMessage = "ERROR"

            Mockito.`when`(postRepository.getPosts()).thenReturn(flowOf(list))
            Mockito.`when`(postRepository.refreshPosts(POST_LIMIT))
                .thenReturn(ResultWrapper.Error(errorMessage))

            viewModel.refreshPosts(POST_LIMIT)

            viewModel.posts.test {
                assert(expectItem() == list)
                expectComplete()
            }

            viewModel.events.test {
                assert(expectItem() == UiState.Error(errorMessage))
            }

            Mockito.verify(postRepository, times(1)).refreshPosts(POST_LIMIT)
        }

    @Test
    fun `Given a call to dismissPost, should call the repository and emit post dismissed`() =
        runBlockingTest {

            val post = mockPostList().first()

            viewModel.dismissPost(post)

            Mockito.verify(postRepository, times(1)).dismissPost(post)

        }

    @Test
    fun `Given a call to markPostAsRead, should call the repository`() =
        runBlockingTest {

            val post = mockPostList().first()

            viewModel.markPostAsRead(post)

            //Mockito.verify(postRepository, times(1)).dismissPost(post)
        }

    @Test
    fun `Given a call to dismissAll, should call the repository`() {
        runBlockingTest {

            viewModel.dismissAll()

            Mockito.verify(postRepository, times(1)).dismissAll()
        }

    }
}