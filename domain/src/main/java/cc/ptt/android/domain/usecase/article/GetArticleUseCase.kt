package cc.ptt.android.domain.usecase.article

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import cc.ptt.android.common.StaticValue
import cc.ptt.android.common.StringUtils
import cc.ptt.android.common.date.DateFormatUtils
import cc.ptt.android.common.date.DatePatternConstants
import cc.ptt.android.common.logger.PttLogger
import cc.ptt.android.data.model.remote.article.ArticleDetail
import cc.ptt.android.data.repository.article.ArticleRepository
import cc.ptt.android.domain.base.UseCaseBase
import cc.ptt.android.domain.model.ui.article.ArticleInfo
import cc.ptt.android.domain.model.ui.article.ArticleReadInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetArticleUseCase constructor(
    private val articleRepository: ArticleRepository,
    private val logger: PttLogger
) : UseCaseBase() {

    fun getArticleDetail(boardId: String, articleId: String): Flow<ArticleInfo> {
        return articleRepository.getArticleDetail(boardId, articleId).map { detail ->
            val list = mutableListOf<ArticleReadInfo>().apply {
                add(getHeaderInfo(detail))
                addAll(getContent(detail))
                add(getCenterBarInfo(detail))
            }.toList()
            ArticleInfo(list, detail.rank)
        }
    }

    fun getArticleComments(boardId: String, articleId: String): Flow<List<ArticleReadInfo>> {
        return articleRepository.getArticleComments(boardId, articleId).map { comments ->
            val list = mutableListOf<ArticleReadInfo>()
            for ((index, articleComment) in comments.list.withIndex()) {
                val content = articleComment.content
                if (content == null) {
                    list.add(ArticleReadInfo.CommentInfo(index, "", articleComment.owner))
                    list.add(
                        ArticleReadInfo.CommentBarInfo(
                            index,
                            DateFormatUtils.secondsToDateTime(
                                articleComment.createTime.toLong(),
                                DatePatternConstants.articleCommentDateTime
                            ),
                            "${index + 1}F",
                            "0"
                        )
                    )
                } else {
                    content.forEach { listContent ->
                        val text = StringBuilder()
                        listContent.forEach { content ->
                            text.append(content.text)
                        }

                        list.add(ArticleReadInfo.CommentInfo(index, text.toString(), articleComment.owner))
                        val imageUrl: List<String> = StringUtils.getImgUrl(text.toString())
                        for (urlString in imageUrl) {
                            list.add(ArticleReadInfo.ImageInfo(index, urlString))
                        }

                        list.add(
                            ArticleReadInfo.CommentBarInfo(
                                index,
                                DateFormatUtils.secondsToDateTime(articleComment.createTime.toLong(), DatePatternConstants.articleCommentDateTime),
                                "${index + 1}F",
                                "0"
                            )
                        )
                    }
                }
            }
            list.toList()
        }.catch { e ->
            logger.e(TAG, "$e", e)
            throw e
        }
    }

    @kotlin.jvm.Throws
    private fun getCenterBarInfo(detail: ArticleDetail): ArticleReadInfo.CenterBarInfo {
        return ArticleReadInfo.CenterBarInfo(detail.recommend.toString(), detail.nComments.toString())
    }

    @kotlin.jvm.Throws
    private fun getContent(
        detail: ArticleDetail
    ): List<ArticleReadInfo> {
        val list = mutableListOf<ArticleReadInfo>()
        val contentBuilder = StringBuilder()
        val spannedString = SpannableStringBuilder()

        detail.content.forEach { listContent ->

            listContent.forEach {
                contentBuilder.append(it.text)
                val spannable = SpannableString(it.text)
                spannable.setSpan(
                    ForegroundColorSpan(it.color0.foregroundColor),
                    0,
                    it.text.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    BackgroundColorSpan(it.color0.backgroundColor),
                    0,
                    it.text.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                val matcher = StringUtils.UrlPattern.matcher(it.text)
                while (matcher.find()) {
                    val urlTemp = matcher.group()
                    val start = spannable.indexOf(urlTemp)
                    val end = start + urlTemp.length
                    spannable.setSpan(
                        URLSpan(urlTemp),
                        start,
                        end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannable.setSpan(
                        ForegroundColorSpan(StaticValue.webUrlColor),
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                }

                spannedString.append(spannable)
            }

            contentBuilder.appendLine()
            spannedString.appendLine()

            val imageUrl = StringUtils.getImgUrl(contentBuilder.toString())
            if (imageUrl.isNotEmpty()) {
                list.add(ArticleReadInfo.ContentLineInfo(contentBuilder.toString(), spannedString))
                contentBuilder.clear()
                spannedString.clear()
            }
            for (urlString in imageUrl) {
                list.add(ArticleReadInfo.ImageInfo(-2, urlString))
            }
        }

        list.add(ArticleReadInfo.ContentLineInfo(contentBuilder.toString(), spannedString))
        return list
    }

    @kotlin.jvm.Throws
    private fun getHeaderInfo(detail: ArticleDetail): ArticleReadInfo.HeaderInfo {
        return ArticleReadInfo.HeaderInfo(
            detail.title,
            detail.owner,
            DateFormatUtils.secondsToDateTime(detail.createTime.toLong(), DatePatternConstants.articleDateTime),
            detail.classX,
            detail.boardName
        )
    }

    companion object {
        private val TAG = GetArticleUseCase::class.java.simpleName
    }
}
