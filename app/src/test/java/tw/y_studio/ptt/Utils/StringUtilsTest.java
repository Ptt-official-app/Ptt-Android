package tw.y_studio.ptt.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest {

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void notNullString() {
        assertSame("", StringUtils.notNullString(null));
        assertSame("ptt", StringUtils.notNullString("ptt"));

        assertSame("", StringUtils2.notNullString(null));
        assertSame("ptt", StringUtils2.notNullString("ptt"));
    }

    @Test
    public void isAccount() {
        assertTrue(StringUtils.isAccount("ptt"));
        assertTrue(StringUtils.isAccount("PTT"));
        assertFalse(StringUtils.isAccount("P"));
        assertTrue(StringUtils.isAccount("P111111111111"));

        assertTrue(StringUtils2.isAccount("ptt"));
        assertTrue(StringUtils2.isAccount("PTT"));
        assertFalse(StringUtils2.isAccount("P"));
        assertTrue(StringUtils2.isAccount("P111111111111"));
    }

    @Test
    public void clearStart() {
        assertEquals("ptt", StringUtils.clearStart(" ptt"));
        assertEquals("ptt", StringUtils2.clearStart(" ptt"));
    }
}
