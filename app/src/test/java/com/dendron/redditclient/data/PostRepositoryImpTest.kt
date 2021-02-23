package com.dendron.redditclient.data

import com.dendron.redditclient.data.datasource.RemoteDataSource
import com.dendron.redditclient.domain.PostRepository
import com.dendron.redditclient.remote.ResultWrapper
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

            val result = ResultWrapper.Success(emptyList())

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

    companion object {
        const val POST_LIMIT = 2;
    }
}