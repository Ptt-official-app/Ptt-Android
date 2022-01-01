package cc.ptt.android.common.ptt

import org.junit.Assert
import org.junit.Test

class AidBeanTest {

    private val aid = "1Xpy6SRj"
    private val boardName = "Test"
    private val url = "https://www.ptt.cc/bbs/Test/M.1641005468.A.6ED.html"

    @Test
    fun `Test isEmpty when both of AidBean are empty then return true`() {
        val aidBean = AidBean()
        Assert.assertEquals(true, aidBean.isEmpty())
    }

    @Test
    fun `Test isEmpty when board name of AidBean is empty then return true`() {
        val aidBean = AidBean(null, aid)
        Assert.assertEquals(true, aidBean.isEmpty())
    }

    @Test
    fun `Test isEmpty when aid of AidBean is empty then return true`() {
        val aidBean = AidBean(boardName, null)
        Assert.assertEquals(true, aidBean.isEmpty())
    }

    @Test
    fun `Test toUrl when data is correct then converter correct`() {
        val aidBean = AidBean(boardName, aid)
        Assert.assertEquals(url, aidBean.toUrl())
    }

    @Test
    fun `Test toUrl when data is incorrect then converter error`() {
        val aidBean = AidBean(boardName, "aid")
        Assert.assertNotEquals(url, aidBean.toUrl())
    }

    @Test
    fun `Test parse when data is correct then converter correct`() {
        val oriAidBean = AidBean(boardName, aid)
        val aidBean = AidBean.parse(url)
        Assert.assertEquals(oriAidBean, aidBean)
    }

    @Test
    fun `Test parse when data is incorrect then converter error`() {
        val aidBean = AidBean.parse("https://www.ptt.cc/bbs/Test/M.1641005468.A.6ED")
        Assert.assertEquals(true, aidBean.isEmpty())
    }
}
