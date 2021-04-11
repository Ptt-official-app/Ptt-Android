package tw.y_studio.ptt.source.remote.article

import com.google.common.truth.Truth
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tw.y_studio.ptt.MainCoroutineRule
import tw.y_studio.ptt.api.PostAPI
import tw.y_studio.ptt.api.PostRankMark
import tw.y_studio.ptt.api.article.ArticleApiService
import tw.y_studio.ptt.api.model.Post

@ExperimentalCoroutinesApi
class PostRemoteDataSourceTest {

    private lateinit var articleRemoteDataSource: IArticleRemoteDataSource

    @MockK
    private lateinit var articleApi: ArticleApiService

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var postAPI: PostAPI

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        articleRemoteDataSource = ArticleRemoteDataSourceImpl(
            postAPI,
            articleApi,
            Dispatchers.Main
        )
    }

    @Test
    fun get_post_then_return_data() {
        every { postAPI.getPost("nba", "") } returns Post(
            "",
            "",
            "",
            "",
            "",
            "",
            emptyList()
        )

        val result = articleRemoteDataSource.getPost("nba", "")

        Truth.assertThat(
            result == Post(
                title = "",
                classString = "",
                date = "",
                auth = "",
                authNickName = "",
                content = "",
                comments = emptyList()
            )
        )
    }

    @Test
    fun set_post_rank_then_success() {
        every { postAPI.setPostRank(any(), any(), any(), any()) } just Runs
        articleRemoteDataSource.setPostRank("", "", "", PostRankMark.None)
        verify { postAPI.setPostRank(any(), any(), any(), any()) }
    }

    @Test
    fun dispose_all_then_check_invoke() {
        every { postAPI.close() } just Runs
        articleRemoteDataSource.disposeAll()
        verify { postAPI.close() }
    }

    @After
    fun tearDown() {
    }
}
