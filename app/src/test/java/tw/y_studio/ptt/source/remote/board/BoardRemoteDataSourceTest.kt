package tw.y_studio.ptt.source.remote.board

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tw.y_studio.ptt.MainCoroutineRule
import tw.y_studio.ptt.TestJsonFileUtils
import tw.y_studio.ptt.api.board.BoardApiService
import tw.y_studio.ptt.api.model.board.article.ArticleList
import tw.y_studio.ptt.api.model.board.hot_board.HotBoard
import tw.y_studio.ptt.utils.fromJson

@ExperimentalCoroutinesApi
class BoardRemoteDataSourceTest {
    private lateinit var boardRemoteDataSource: IBoardRemoteDataSource

    @MockK
    private lateinit var boardApi: BoardApiService

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        boardRemoteDataSource = BoardRemoteDataSourceImpl(boardApi)
    }

    @Test
    fun get_popular_board_data_then_return_data() = runBlockingTest {
        // GIVEN
        val jsonFile = TestJsonFileUtils.loadJsonFile("api/board/popular_boards.json")
        val hotBoard = Gson().fromJson<HotBoard>(jsonFile)

        coEvery { boardApi.getPopularBoard() } returns hotBoard

        // WHEN
        val result = boardRemoteDataSource.getPopularBoards()

        // THEN
        Truth.assertThat(result).apply {
            isEqualTo(hotBoard)
        }
    }

    private fun createSampleData() = mapOf("foo" to "bar")

    @Test(expected = Exception::class)
    fun get_popular_board_data_then_throw_exception() = runBlockingTest {
        coEvery { boardApi.getPopularBoard() } throws Exception()

        boardRemoteDataSource.getPopularBoards()
    }

    @Test
    fun get_post_list_data_then_return_data() = runBlockingTest {
        val jsonFile = TestJsonFileUtils.loadJsonFile("api/board/articles.json")
        val articleList = Gson().fromJson<ArticleList>(jsonFile)

        coEvery {
            boardApi.getArticles(
                "nba",
                "foo",
                "bar",
                200,
                true
            )
        } returns articleList

        val result = boardRemoteDataSource.getBoardArticles(
            "nba",
            "foo",
            "bar",
            200,
            true
        )

        Truth.assertThat(result).isEqualTo(articleList)

        Truth.assertThat(result.list).apply {
            isNotEmpty()
            hasSize(articleList.list.size)
        }
    }

    @Test(expected = Exception::class)
    fun get_post_list_data_then_throw_exception() = runBlockingTest {
        coEvery { boardApi.getArticles(any(), any(), any(), any(), any()) } throws Exception()
        boardRemoteDataSource.getBoardArticles(
            "nba",
            "foo",
            "bar",
            200,
            true
        )
    }

    @After
    fun tearDown() {
    }
}
