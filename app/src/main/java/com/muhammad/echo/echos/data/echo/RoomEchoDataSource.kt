package com.muhammad.echo.echos.data.echo

import com.muhammad.echo.core.database.echo.EchoDao
import com.muhammad.echo.echos.domain.echo.Echo
import com.muhammad.echo.echos.domain.echo.EchoDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomEchoDataSource(
    private val echoDao: EchoDao
)  : EchoDataSource {
    override fun observeEchos(): Flow<List<Echo>> {
        return echoDao.observeEchos().map {echoWithTopics ->
            echoWithTopics.map { echoWithTopic->
                echoWithTopic.toEcho()
            }
        }
    }

    override fun observeTopics(): Flow<List<String>> {
        return echoDao.observeTopics().map {topicEntities ->
            topicEntities.map { it.topic }
        }
    }

    override fun searchTopics(query: String): Flow<List<String>> {
        return echoDao.searchTopics(query).map {topicEntities ->
            topicEntities.map { it.topic }
        }
    }

    override suspend fun insertEcho(echo: Echo) {
        echoDao.insertEchoWithTopics(echo.toEchoWithTopics())
    }
}