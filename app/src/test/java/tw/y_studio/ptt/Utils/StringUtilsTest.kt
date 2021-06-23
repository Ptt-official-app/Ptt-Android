package tw.y_studio.ptt.utils

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StringUtilsTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun notNullString() {
        Assert.assertEquals("", StringUtils.notNullString(null))
        Assert.assertEquals("ptt", StringUtils.notNullString("ptt"))
    }

    @Test
    fun isAccount() {
        Assert.assertTrue(StringUtils.isAccount("ptt"))
        Assert.assertTrue(StringUtils.isAccount("PTT"))
        Assert.assertFalse(StringUtils.isAccount("P"))
        Assert.assertTrue(StringUtils.isAccount("P111111111111"))
    }

    @Test
    fun getImgUrl() {
        val expected = listOf("https://i.imgur.com/MbkBGEG.jpg", "https://i.imgur.com/MbkBGEG.jpg")
        Assert.assertArrayEquals(expected.toTypedArray(), StringUtils.getImgUrl("https://i.imgur.com/MbkBGEG.jpg 嗨 https://i.imgur.com/MbkBGEG.jpg").toTypedArray())
    }

    @Test
    fun notNullImageString() {
        Assert.assertEquals("https://i.imgur.com/MbkBGEG.jpg", StringUtils.notNullImageString("https://i.imgur.com/MbkBGEG.jpg"))
    }

    @Test
    fun clearStart() {
        Assert.assertEquals("ptt", StringUtils.clearStart(" ptt"))
    }
}
