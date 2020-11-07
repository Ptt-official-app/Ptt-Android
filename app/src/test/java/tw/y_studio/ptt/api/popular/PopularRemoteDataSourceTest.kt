package tw.y_studio.ptt.api.popular

import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.json.JSONException
import org.junit.After
import org.junit.Before
import org.junit.Test
import tw.y_studio.ptt.api.PopularBoardListAPI

class PopularRemoteDataSourceTest {
    private lateinit var popularRemoteDataSource: IPopularRemoteDataSource

    @MockK
    private lateinit var popularBoardListAPI: PopularBoardListAPI

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        popularRemoteDataSource = PopularRemoteDataSourceImpl(popularBoardListAPI)
    }

    @Test
    fun get_popular_board_data_then_return_data() {
        // GIVEN
        val data = mapOf("foo" to "bar")
        every { popularBoardListAPI.refresh(any(), any()) } returns mutableListOf(data)

        // WHEN
        val result = popularRemoteDataSource.getPopularBoardData(1, 10)

        // THEN
        Truth.assertThat(result).apply {
            isNotEmpty()
            hasSize(1)
            containsExactly(data)
        }
    }

    private fun createSampleData() = mapOf("foo" to "bar")

    @Test(expected = Exception::class)
    fun get_popular_board_data_then_throw_exception() {
        every { popularBoardListAPI.refresh(any(), any()) } throws Exception()

        popularRemoteDataSource.getPopularBoardData(0, 0)
    }

    @Test(expected = JSONException::class)
    fun get_popular_board_data_then_throw_json_exception() {
        every { popularBoardListAPI.refresh(any(), any()) } throws JSONException("error!")

        popularRemoteDataSource.getPopularBoardData(0, 0)
    }

    @After
    fun tearDown() {
    }
}
