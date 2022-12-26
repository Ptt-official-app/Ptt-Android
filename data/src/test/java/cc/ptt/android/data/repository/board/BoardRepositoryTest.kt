package cc.ptt.android.data.repository.board

import cc.ptt.android.data.ApiTestBase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.inject

class BoardRepositoryTest : ApiTestBase(needLogin = true) {

    private val boardRepository: BoardRepository by inject()

    @Test
    fun testFetchPopularBoards() = runBlocking {
        boardRepository.getPopularBoards().catch {
            assert(false)
        }.collect {
            assert(it.list.isNotEmpty())
        }
    }

    @Test
    fun testFetchBoardArticles() = runBlocking {
        val boardId = "Baseball"
        val limit = 200
        boardRepository.getBoardArticles(boardId = boardId, limit = limit).catch {
            assert(false)
        }.collect {
            assert(it.list.isNotEmpty())
            assert(it.list.size <= limit)
            it.list.forEach { article ->
                assert(article.title.isNotEmpty())
                assert(article.boardId.isNotEmpty())
                assert(article.articleId.isNotEmpty())
            }
        }
    }
}
