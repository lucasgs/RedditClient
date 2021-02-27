package com.dendron.redditclient

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dendron.redditclient.data.IsOnlineChecker
import com.dendron.redditclient.data.PostRepositoryImp
import com.dendron.redditclient.data.datasource.LocalDataSource
import com.dendron.redditclient.data.datasource.RemoteDataSource
import com.dendron.redditclient.domain.ApiResult
import com.dendron.redditclient.domain.PostRepository
import com.dendron.redditclient.domain.ResultWrapper
import com.dendron.redditclient.presentation.PostViewModel.Companion.POST_LIMIT
import com.dendron.redditclient.utils.MainCoroutineScopeRule
import com.dendron.redditclient.utils.mockPostList
import com.nhaarman.mockitokotlin2.times
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PostRepositoryImpTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    private lateinit var localDataSource: LocalDataSource

    @Mock
    private lateinit var isOnlineChecker: IsOnlineChecker

    private lateinit var repository: PostRepository

    @Before
    fun setUp() {
        repository = PostRepositoryImp(remoteDataSource, localDataSource, isOnlineChecker)
    }

    @Test
    fun `Given a call to refreshPosts, empty list, should call the remote data source`() =
        runBlockingTest {

            val after = ""

            val result = ApiResult.Success(null, emptyList())
            val expected = ResultWrapper.Success(null)

            Mockito.`when`(isOnlineChecker.execute()).thenReturn(true)
            Mockito.`when`(remoteDataSource.fetchMorePosts(POST_LIMIT, after)).thenReturn(result)

            val actual = repository.fetchMorePosts(POST_LIMIT, after)

            Mockito.verify(remoteDataSource, times(1)).fetchMorePosts(POST_LIMIT, after)

            assert(actual == expected)
        }

    @Test
    fun `Given a call to refreshPosts, list with items, should call the remote data source`() =
        runBlockingTest {

            val after = ""

            val postList = mockPostList()
            val result = ApiResult.Success(null, postList)
            val expected = ResultWrapper.Success()

            Mockito.`when`(isOnlineChecker.execute()).thenReturn(true)
            Mockito.`when`(remoteDataSource.fetchMorePosts(POST_LIMIT, after)).thenReturn(result)
            //Mockito.`when`(localDataSource.getPosts(POST_LIMIT)).thenReturn(flowOf(postList))

            val actual = repository.fetchMorePosts(POST_LIMIT, after)

            Mockito.verify(remoteDataSource, times(1)).fetchMorePosts(POST_LIMIT, after)

            assert(actual == expected)

        }

    @Test
    fun `Given a call to refreshPosts, list with items and a limit, should call the remote data source`() =
        runBlockingTest {

            val after = ""

            val postList = mockPostList().subList(0, mockPostList().size - 1)
            val result = ApiResult.Success(null, postList)
            val expected = ResultWrapper.Success()

            Mockito.`when`(remoteDataSource.fetchMorePosts(POST_LIMIT, after)).thenReturn(result)
            Mockito.`when`(isOnlineChecker.execute()).thenReturn(true)

            val actual = repository.fetchMorePosts(POST_LIMIT, after)

            Mockito.verify(remoteDataSource, times(1)).fetchMorePosts(POST_LIMIT, after)

            assert(actual == expected)

        }

    @Test
    fun `Given a call to refreshPosts, an error occur, should call the remote data source`() =
        runBlockingTest {
            val after = ""
            val message = "ERROR"
            val result = ApiResult.Error(message)
            val expected = ResultWrapper.Error(message)

            Mockito.`when`(isOnlineChecker.execute()).thenReturn(true)
            Mockito.`when`(remoteDataSource.fetchMorePosts(POST_LIMIT, after)).thenReturn(result)

            val actual = repository.fetchMorePosts(POST_LIMIT, after)

            Mockito.verify(remoteDataSource, times(1)).fetchMorePosts(POST_LIMIT, after)

            assert(actual == expected)
        }


    @Test
    fun `Given a call to refreshPosts, device is online, should call the remote data source`() =
        runBlockingTest {

            val after = ""
            val result = ApiResult.Success(after, emptyList())

            Mockito.`when`(remoteDataSource.fetchMorePosts(POST_LIMIT, after)).thenReturn(result)
            Mockito.`when`(isOnlineChecker.execute()).thenReturn(true)

            repository.fetchMorePosts(POST_LIMIT, after)

            Mockito.verify(localDataSource, times(1)).insertAll(emptyList())
            Mockito.verify(remoteDataSource, times(1)).fetchMorePosts(POST_LIMIT, after)
        }


    @Test
    fun `Given a call to refreshPosts, device is offline, should not call the remote data source`() =
        runBlockingTest {

            val after = ""

            Mockito.`when`(isOnlineChecker.execute()).thenReturn(false)

            repository.fetchMorePosts(POST_LIMIT, after)

            Mockito.verify(localDataSource, times(0)).insertAll(emptyList())
            Mockito.verify(remoteDataSource, times(0)).fetchMorePosts(POST_LIMIT, after)
        }


    @Test
    fun `Given a call to dismissPost, should call the local data source`() =
        runBlockingTest {
            val post = mockPostList().first()
            repository.dismissPost(post)
            Mockito.verify(localDataSource, times(1)).dismissPost(post)
        }

    @Test
    fun `Given a call to dismissAll, should call the local data source`() =
        runBlockingTest {
            repository.dismissAll()
            Mockito.verify(localDataSource, times(1)).dismissAll()
        }


}