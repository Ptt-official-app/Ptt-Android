package tw.y_studio.ptt.source.remote.post

import com.google.common.truth.Truth
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import tw.y_studio.ptt.api.PostAPI
import tw.y_studio.ptt.api.PostRankMark
import tw.y_studio.ptt.api.model.PartialPost
import tw.y_studio.ptt.api.model.Post

class PostRemoteDataSourceTest {

    private lateinit var postRemoteDataSource: IPostRemoteDataSource

    @MockK
    private lateinit var postAPI: PostAPI

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        postRemoteDataSource = PostRemoteDataSourceImpl(
            postAPI
        )
    }

    @Test
    fun get_post_list_data_then_return_data() {
        every { postAPI.getPostList("nba", 1) } returns mutableListOf(
            PartialPost(
                title = "this is title"
            )
        )

        val result = postRemoteDataSource.getPostList("nba", 1)

        Truth.assertThat(result).apply {
            isNotEmpty()
            hasSize(1)
            containsExactly(PartialPost(title = "this is title"))
        }
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

        val result = postRemoteDataSource.getPost("nba", "")

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

    @Test(expected = Exception::class)
    fun get_post_list_data_then_throw_exception() {
        every { postAPI.getPostList(any(), any()) } throws Exception()
        postRemoteDataSource.getPostList("", 0)
    }

    @Test
    fun set_post_rank_then_success() {
        every { postAPI.setPostRank(any(), any(), any(), any()) } just Runs
        postRemoteDataSource.setPostRank("", "", "", PostRankMark.None)
        verify { postAPI.setPostRank(any(), any(), any(), any()) }
    }

    @Test
    fun dispose_all_then_check_invoke() {
        every { postAPI.close() } just Runs
        postRemoteDataSource.disposeAll()
        verify { postAPI.close() }
    }

    @After
    fun tearDown() {
    }
}
