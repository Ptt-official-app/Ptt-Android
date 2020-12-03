package tw.y_studio.ptt.source.remote.search

import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.json.JSONException
import org.junit.After
import org.junit.Before
import org.junit.Test
import tw.y_studio.ptt.network.api.SearchBoardAPI

class SearchBoardRemoteDataSourceTest {

    private lateinit var searchBoardRemoteDataSource: ISearchBoardRemoteDataSource

    @MockK
    private lateinit var searchBoardAPI: SearchBoardAPI

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        searchBoardRemoteDataSource = SearchBoardRemoteDataSourceImpl(
            searchBoardAPI
        )
    }

    @Test
    fun search_board_by_keyboard_then_return_correct_board() {
        // GIVEN
        every { searchBoardAPI.searchBoard("BaseBall") } returns mutableListOf(createSampleData())

        val result = searchBoardRemoteDataSource.searchBoardByKeyword("BaseBall")

        Truth.assertThat(result).apply {
            isNotEmpty()
            hasSize(1)
            containsExactly(createSampleData())
        }
    }

    // TODO: 2020/11/7 驗證輸入錯誤的文字後找不到看板，尚未實作此功能
    @Test
    fun search_board_by_keyboard_then_return_empty_board() {
        // GIVEN
        every { searchBoardAPI.searchBoard("Base Ball") } returns mutableListOf(mapOf("foo" to "string"))

        val result = searchBoardRemoteDataSource.searchBoardByKeyword("Base Ball")

        Truth.assertThat(result).apply {
            isNotEmpty()
            hasSize(1)
            containsExactly(mapOf("foo" to "string"))
        }
    }

    @Test(expected = Exception::class)
    fun search_board_by_keyboard_then_throw_exception() {
        every { searchBoardAPI.searchBoard(any()) } throws Exception()
        searchBoardRemoteDataSource.searchBoardByKeyword("")
    }

    @Test(expected = JSONException::class)
    fun search_board_by_keyboard_then_throw_json_exception() {
        every { searchBoardAPI.searchBoard(any()) } throws JSONException("error!")
        searchBoardRemoteDataSource.searchBoardByKeyword("")
    }

    private fun createSampleData() = mapOf("foo" to "BaseBall")

    @After
    fun tearDown() {}
}
