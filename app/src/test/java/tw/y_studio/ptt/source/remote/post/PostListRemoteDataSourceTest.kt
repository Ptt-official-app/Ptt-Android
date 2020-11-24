package tw.y_studio.ptt.source.remote.post

import com.google.common.truth.Truth
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import tw.y_studio.ptt.api.PostListAPI
import tw.y_studio.ptt.model.PartialPost

class PostListRemoteDataSourceTest {

    private lateinit var postListRemoteDataSource: IPostListRemoteDataSource

    @MockK
    private lateinit var postListAPI: PostListAPI

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        postListRemoteDataSource = PostListRemoteDataSourceImpl(
            postListAPI
        )
    }

    @Test
    fun get_post_list_data_then_return_data() {
        every { postListAPI.loadBroadListData("nba", 1) } returns mutableListOf(
            PartialPost(
                title = "this is title"
            )
        )

        val result = postListRemoteDataSource.getPostListData("nba", 1)

        Truth.assertThat(result).apply {
            isNotEmpty()
            hasSize(1)
            containsExactly(PartialPost(title = "this is title"))
        }
    }

    @Test(expected = Exception::class)
    fun get_post_list_data_then_throw_exception() {
        every { postListAPI.loadBroadListData(any(), any()) } throws Exception()
        postListRemoteDataSource.getPostListData("", 0)
    }

    @Test
    fun dispose_all_then_check_invoke() {
        every { postListAPI.close() } just Runs
        postListRemoteDataSource.disposeAll()
        verify { postListAPI.close() }
    }

    @After
    fun tearDown() {}
}
