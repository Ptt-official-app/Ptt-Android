package tw.y_studio.ptt.source.remote.rank

import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import tw.y_studio.ptt.api.PostRankAPI

/**
 * Created by Michael.Lien
 * on 2020/12/1
 */
class PostRankRemoteDataSourceTest {

    private lateinit var postRankRemoteDataSource: IPostRankRemoteDataSource

    @MockK
    private lateinit var postRankAPI: PostRankAPI

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        postRankRemoteDataSource = PostRankRemoteDataSourceImpl(postRankAPI)
    }

    @Test
    fun post_rank_then_check_invoke() {
        every { postRankAPI.setPostRank("nba", "test", "test", any()) } just Runs

        postRankRemoteDataSource.setPostRank("nba", "test", "test", PostRankAPI.PostRank.Like)

        verify { postRankAPI.setPostRank(any(), any(), any(), any()) }
    }

    @After
    fun tearDown() {}
}
