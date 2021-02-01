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
import tw.y_studio.ptt.api.model.hot_board.HotBoard
import tw.y_studio.ptt.utils.fromJson

@ExperimentalCoroutinesApi
class PopularRemoteDataSourceTest {
    private lateinit var popularRemoteDataSource: IPopularRemoteDataSource

    @MockK
    private lateinit var boardApi: BoardApiService

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        popularRemoteDataSource = PopularRemoteDataSourceImpl(boardApi)
    }

    @Test
    fun get_popular_board_data_then_return_data() = runBlockingTest {
        // GIVEN
        val jsonFile = TestJsonFileUtils.loadJsonFile("api/board/popular_boards.json")
        val hotBoard = Gson().fromJson<HotBoard>(jsonFile)

        coEvery { boardApi.getPopularBoard() } returns hotBoard

        // WHEN
        val result = popularRemoteDataSource.getPopularBoards()

        // THEN
        Truth.assertThat(result).apply {
            isEqualTo(hotBoard)
        }
    }

    private fun createSampleData() = mapOf("foo" to "bar")

    @Test(expected = Exception::class)
    fun get_popular_board_data_then_throw_exception() = runBlockingTest {
        coEvery { boardApi.getPopularBoard() } throws Exception()

        popularRemoteDataSource.getPopularBoards()
    }

    @After
    fun tearDown() {
    }
}
