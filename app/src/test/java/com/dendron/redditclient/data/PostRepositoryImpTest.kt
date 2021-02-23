package com.dendron.redditclient.data

import com.dendron.redditclient.data.datasource.RemoteDataSource
import com.dendron.redditclient.domain.PostRepository
import com.dendron.redditclient.domain.ResultWrapper
import com.dendron.redditclient.domain.model.Post
import com.nhaarman.mockitokotlin2.times
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PostRepositoryImpTest {

    private lateinit var repository: PostRepository

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource


    @Before
    fun setUp() {
        repository = PostRepositoryImp(remoteDataSource)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `Given a call to getPost, empty list, should call the remote data source`() =
        runBlockingTest {

            val result = ResultWrapper.Success<List<Post>>(emptyList())

            Mockito.`when`(remoteDataSource.getPosts(POST_LIMIT)).thenReturn(result)

            val expected = repository.getPosts(POST_LIMIT)

            Mockito.verify(remoteDataSource, times(1)).getPosts(POST_LIMIT)

            assert(result == expected)

        }

    @Test
    fun `Given a call to getPost, list with items, should call the remote data source`() =
        runBlockingTest {

            val postList = mockPostList()

            val result = ResultWrapper.Success(postList)

            Mockito.`when`(remoteDataSource.getPosts(POST_LIMIT)).thenReturn(result)

            val expected = repository.getPosts(POST_LIMIT)

            Mockito.verify(remoteDataSource, times(1)).getPosts(POST_LIMIT)

            assert(result == expected)

        }

    @Test
    fun `Given a call to getPost, list with items and a limit, should call the remote data source`() =
        runBlockingTest {

            val postList = mockPostList().subList(0, POST_LIMIT)

            val result = ResultWrapper.Success(postList)

            Mockito.`when`(remoteDataSource.getPosts(POST_LIMIT)).thenReturn(result)

            val expected = repository.getPosts(POST_LIMIT)

            Mockito.verify(remoteDataSource, times(1)).getPosts(POST_LIMIT)

            assert(result == expected)

        }

    @Test
    fun `Given a call to getPost, an error occur, should call the remote data source`() =
        runBlockingTest {
            val message = "ERROR"
            val result = ResultWrapper.Error(message)
            Mockito.`when`(remoteDataSource.getPosts(POST_LIMIT)).thenReturn(result)

            val expected = repository.getPosts(POST_LIMIT)

            Mockito.verify(remoteDataSource, times(1)).getPosts(POST_LIMIT)

            assert(result == expected)
        }


    private fun mockPostList() = listOf<Post>(
       Post(id = "1", title = "title1", author = "author1", thumbnail = "thumb1", comments = 1, created = 1 ),
       Post(id = "2", title = "title2", author = "author2", thumbnail = "thumb2", comments = 2, created = 2 ),
       Post(id = "3", title = "title3", author = "author3", thumbnail = "thumb3", comments = 3, created = 3 ),
    )

    companion object {
        const val POST_LIMIT = 2;
    }
}