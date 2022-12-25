package cc.ptt.android.data.repository.search

import cc.ptt.android.ApiTestBase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.inject

class SearchBoardRepositoryTest : ApiTestBase(needLogin = true) {

    private val searchBoardRepository: SearchBoardRepository by inject()

    @Test
    fun testFetchSearchBoardByKeyword() = runBlocking {
        val keyWord = "e"
        val limit = 200
        searchBoardRepository.searchBoardByKeyword(keyword = keyWord, limit = limit).catch {
            assert(false)
        }.collect {
            assert(it.list.isNotEmpty())
            assert(it.list.size <= limit)
            it.list.forEach { article ->
                assert(article.boardId.isNotEmpty())
            }
        }
    }
}
