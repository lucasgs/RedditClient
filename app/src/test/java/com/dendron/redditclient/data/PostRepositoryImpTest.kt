package com.dendron.redditclient.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dendron.redditclient.data.datasource.LocalDataSource
import com.dendron.redditclient.data.datasource.RemoteDataSource
import com.dendron.redditclient.domain.ApiResult
import com.dendron.redditclient.domain.PostRepository
import com.dendron.redditclient.domain.ResultWrapper
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.utils.MainCoroutineScopeRule
import com.dendron.redditclient.utils.POST_LIMIT
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

    @After
    fun tearDown() {

    }

    @Test
    fun `Given a call to refreshPosts, empty list, should call the remote data source`() =
        runBlockingTest {

            val result = ApiResult.Success<List<Post>>(emptyList())
            val expected = ResultWrapper.Success

            Mockito.`when`(isOnlineChecker.execute()).thenReturn(true)
            Mockito.`when`(remoteDataSource.getPosts(POST_LIMIT)).thenReturn(result)

            val actual = repository.refreshPosts(POST_LIMIT)

            Mockito.verify(remoteDataSource, times(1)).getPosts(POST_LIMIT)

            assert(actual == expected)
        }

    @Test
    fun `Given a call to refreshPosts, list with items, should call the remote data source`() =
        runBlockingTest {

            val postList = mockPostList()
            val result = ApiResult.Success(postList)
            val expected = ResultWrapper.Success

            Mockito.`when`(isOnlineChecker.execute()).thenReturn(true)
            Mockito.`when`(remoteDataSource.getPosts(POST_LIMIT)).thenReturn(result)
            //Mockito.`when`(localDataSource.getPosts(POST_LIMIT)).thenReturn(flowOf(postList))

            val actual = repository.refreshPosts(POST_LIMIT)

            Mockito.verify(remoteDataSource, times(1)).getPosts(POST_LIMIT)

            assert(actual == expected)

        }

    @Test
    fun `Given a call to refreshPosts, list with items and a limit, should call the remote data source`() =
        runBlockingTest {

            val postList = mockPostList().subList(0, POST_LIMIT)
            val result = ApiResult.Success(postList)
            val expected = ResultWrapper.Success

            Mockito.`when`(remoteDataSource.getPosts(POST_LIMIT)).thenReturn(result)
            Mockito.`when`(isOnlineChecker.execute()).thenReturn(true)

            val actual = repository.refreshPosts(POST_LIMIT)

            Mockito.verify(remoteDataSource, times(1)).getPosts(POST_LIMIT)

            assert(actual == expected)

        }

    @Test
    fun `Given a call to refreshPosts, an error occur, should call the remote data source`() =
        runBlockingTest {
            val message = "ERROR"
            val result = ApiResult.Error(message)
            val expected = ResultWrapper.Error(message)

            Mockito.`when`(isOnlineChecker.execute()).thenReturn(true)
            Mockito.`when`(remoteDataSource.getPosts(POST_LIMIT)).thenReturn(result)

            val actual = repository.refreshPosts(POST_LIMIT)

            Mockito.verify(remoteDataSource, times(1)).getPosts(POST_LIMIT)

            assert(actual == expected)
        }


    @Test
    fun `Given a call to refreshPosts, device is online, should call the remote data source`() =
        runBlockingTest {

            val result = ApiResult.Success<List<Post>>(emptyList())

            Mockito.`when`(remoteDataSource.getPosts(POST_LIMIT)).thenReturn(result)
            Mockito.`when`(isOnlineChecker.execute()).thenReturn(true)

            repository.refreshPosts(POST_LIMIT)

            Mockito.verify(localDataSource, times(1)).insertAll(emptyList())
            Mockito.verify(remoteDataSource, times(1)).getPosts(POST_LIMIT)
        }


    @Test
    fun `Given a call to refreshPosts, device is offline, should not call the remote data source`() =
        runBlockingTest {

            Mockito.`when`(isOnlineChecker.execute()).thenReturn(false)

            repository.refreshPosts(POST_LIMIT)

            Mockito.verify(localDataSource, times(0)).insertAll(emptyList())
            Mockito.verify(remoteDataSource, times(0)).getPosts(POST_LIMIT)
        }


    @Test
    fun `Given a call to dismissPost, should call the local data source`() =
        runBlockingTest {
            val post = mockPostList().first()
            repository.dismissPost(post)
            Mockito.verify(localDataSource, times(1)).delete(post)
        }

    @Test
    fun `Given a call to dismissAll, should call the local data source`() =
        runBlockingTest {
            repository.dismissAll()
            Mockito.verify(localDataSource, times(1)).deleteAll()
        }


}