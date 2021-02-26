package com.dendron.redditclient.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

interface IsOnlineChecker {
    suspend fun execute(): Boolean
}

class IsOnlineCheckerImp : IsOnlineChecker {
    override suspend fun execute(): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                socket.connect(socketAddress, 2000)
                socket.close()

            }

            true
        } catch (ex: IOException) {
            false
        }
    }
}
