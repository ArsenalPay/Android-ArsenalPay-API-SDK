package ru.arsenalpay.api.unit;

import org.junit.Test;
import ru.arsenalpay.api.util.Utils;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    private final String FILE = "src/test/java/ru/arsenalpay/api/unit/support/api_error_function_response.xml";
    private final String TEXT = "<main>\n" +
            "    <rrn>567456755678</rrn>\n" +
            "    <stan>123456</stan>\n" +
            "    <status>ERR_FUNCTION</status>\n" +
            "</main>\n";

    @Test
    public void testGetStringFromFile() throws IOException {
        String text1 = Utils.getStringFromFile(FILE);
        assertEquals(TEXT,text1);
    }
}
