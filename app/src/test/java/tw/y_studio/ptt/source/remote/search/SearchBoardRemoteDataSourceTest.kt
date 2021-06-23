package tw.y_studio.ptt.source.remote.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.json.JSONException
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tw.y_studio.ptt.MainCoroutineRule
import tw.y_studio.ptt.TestJsonFileUtils
import tw.y_studio.ptt.api.SearchBoardAPI
import tw.y_studio.ptt.api.board.BoardApiService
import tw.y_studio.ptt.api.model.board.hot_board.HotBoard
import tw.y_studio.ptt.utils.fromJson

@ExperimentalCoroutinesApi
class SearchBoardRemoteDataSourceTest {

    private lateinit var searchBoardRemoteDataSource: ISearchBoardRemoteDataSource

    @MockK
    private lateinit var searchBoardAPI: SearchBoardAPI

    @MockK
    private lateinit var boardApi: BoardApiService

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        searchBoardRemoteDataSource = SearchBoardRemoteDataSourceImpl(boardApi)
    }

    @Test
    fun search_board_by_keyboard_then_return_correct_board() = runBlockingTest {
        // GIVEN
        val jsonFile = TestJsonFileUtils.loadJsonFile("api/board/search_boards.json")
        val hotBoard = Gson().fromJson<HotBoard>(jsonFile)

        coEvery { boardApi.searchBoards(any(), any(), any(), any()) } returns hotBoard

        val result = searchBoardRemoteDataSource.searchBoardByKeyword("BaseBall", "", 200, true)

        Truth.assertThat(result).apply {
            isEqualTo(hotBoard)
        }
    }

    // TODO: 2020/11/7 驗證輸入錯誤的文字後找不到看板，尚未實作此功能
    @Test
    fun search_board_by_keyboard_then_return_empty_board() = runBlockingTest {
        // GIVEN
        val jsonFile = TestJsonFileUtils.loadJsonFile("api/board/search_boards.json")
        val hotBoard = Gson().fromJson<HotBoard>(jsonFile)

        coEvery { boardApi.searchBoards(any(), any(), any(), any()) } returns hotBoard

        val result = searchBoardRemoteDataSource.searchBoardByKeyword("Base Ball", "", 200, true)

        Truth.assertThat(result).apply {
            isEqualTo(hotBoard)
        }
    }

    @Test(expected = Exception::class)
    fun search_board_by_keyboard_then_throw_exception() = runBlockingTest {
        coEvery { boardApi.searchBoards(any(), any(), any(), any()) } throws Exception()
        searchBoardRemoteDataSource.searchBoardByKeyword("", "", 200, true)
    }

    @Test(expected = JSONException::class)
    fun search_board_by_keyboard_then_throw_json_exception() = runBlockingTest {
        coEvery { boardApi.searchBoards(any(), any(), any(), any()) } throws JSONException("error!")
        searchBoardRemoteDataSource.searchBoardByKeyword("", "", 200, true)
    }

    private fun createSampleData() = mapOf("foo" to "BaseBall")

    @After
    fun tearDown() {}
}
