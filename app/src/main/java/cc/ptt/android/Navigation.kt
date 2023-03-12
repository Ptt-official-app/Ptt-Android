package cc.ptt.android

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import cc.ptt.android.articlelist.ArticleListFragment
import cc.ptt.android.articleread.ArticleReadFragment
import cc.ptt.android.common.extension.navigateForward
import cc.ptt.android.data.model.remote.board.article.Article

object Navigation {

    fun switchToLoginPage(activity: FragmentActivity) {
        getNavController(activity)?.navigateForward(
            R.id.include_login,
            Bundle(),
            isSingleTop = false,
            useDefaultAnim = true
        )
    }

    fun switchToArticleListPage(activity: FragmentActivity, title: String, subtitle: String, boardId: String) {
        val args = Bundle().apply {
            putString(ArticleListFragment.KEY_TITLE, title)
            putString(ArticleListFragment.KEY_SUBTITLE, subtitle)
            putString(ArticleListFragment.KEY_BOARD_ID, boardId)
        }
        getNavController(activity)?.navigateForward(
            R.id.articleListFragment,
            args,
            isSingleTop = false,
            useDefaultAnim = true
        )
    }

    fun switchToArticleReadPage(activity: FragmentActivity, article: Article?, boardName: String) {
        val args = Bundle().apply {
            putParcelable(ArticleReadFragment.KEY_ARTICLE, article)
            putString(ArticleReadFragment.KEY_BOARD_NAME, boardName)
        }
        getNavController(activity)?.navigateForward(
            R.id.articleReadFragment,
            args,
            isSingleTop = false,
            useDefaultAnim = true
        )
    }

    fun switchToArticleListSearchPage(activity: FragmentActivity) {
        getNavController(activity)?.navigateForward(
            R.id.articleListSearchFragment,
            Bundle(),
            isSingleTop = false,
            useDefaultAnim = true
        )
    }

    fun switchToPostArticlePage(activity: FragmentActivity) {
        getNavController(activity)?.navigateForward(
            R.id.postArticleFragment,
            Bundle(),
            isSingleTop = false,
            useDefaultAnim = true
        )
    }

    fun switchToSearchBoardsPage(activity: FragmentActivity) {
        getNavController(activity)?.navigateForward(
            R.id.searchBoardsFragment,
            Bundle(),
            isSingleTop = false,
            useDefaultAnim = false
        )
    }

    fun switchToHotArticleFilterPage(activity: FragmentActivity) {
        getNavController(activity)?.navigateForward(
            R.id.hotArticleFilterFragment,
            Bundle(),
            isSingleTop = false,
            useDefaultAnim = false
        )
    }

    fun popup(activity: FragmentActivity) {
        getNavController(activity)?.popBackStack()
    }

    fun isRoot(activity: FragmentActivity): Boolean {
        return activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments.isNullOrEmpty()
    }

    private fun getNavController(activity: FragmentActivity): NavController? {
        return (activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment)?.navController
    }
}
