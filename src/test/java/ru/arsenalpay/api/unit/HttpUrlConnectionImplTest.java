package ru.arsenalpay.api.unit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.arsenalpay.api.client.ApiResponse;
import ru.arsenalpay.api.client.impl.HttpUrlConnectionImpl;
import ru.arsenalpay.api.command.ApiCommand;
import ru.arsenalpay.api.util.ArsenalpayUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.arsenalpay.api.enums.HttpMethod.GET;
import static org.mockito.Mockito.when;
import static ru.arsenalpay.api.enums.HttpMethod.POST;

/**
 * Created by Ярослав on 12.02.2015.
 */
public class HttpUrlConnectionImplTest {

    public static final String BASE_URI = "https://arsenalpay.ru/init_pay_mk/";

    private Map<String, String> params;

    private String apiResponseBody;

    @Mock
    private HttpUrlConnectionImpl httpUrlConnectionMock;

    @Mock
    private ApiResponse apiResponseMock;

    @Mock
    private ApiCommand apiCommandMock;

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);

        apiResponseBody = ArsenalpayUtils.getStringFromFile("src/test/java/ru/arsenalpay/api/unit/support/api_ok_response.xml");

        params = new HashMap<String, String>() {{
            put("SIGN", "726142fc2d784895406b8a7b1ba8eaad");
            put("PHONE", "9140001111");
            put("FUNCTION", "init_pay_mk");
            put("CURRENCY", "RUR");
            put("ID", "9987");
            put("AMOUNT", "125.0");
            put("ACCOUNT", "123456789");
        }};


        when(httpUrlConnectionMock.executeCommand(apiCommandMock)).thenReturn(apiResponseMock);
        when(apiResponseMock.getBody()).thenReturn(apiResponseBody);
        when(apiResponseMock.getStatusCode()).thenReturn(200);

    }

    @Test
    public void testExecuteGet() throws Exception {
        System.out.println("HttpUrlConnectionImplTest ---> testExecuteGet");
        when(apiCommandMock.getHttpMethod()).thenReturn(GET);

        ApiResponse apiResponse = httpUrlConnectionMock.executeCommand(apiCommandMock);

        assertNotNull(apiResponse);
        assertEquals(200, apiResponse.getStatusCode());
        assertEquals(apiResponseBody, apiResponse.getBody());
        System.out.println(apiResponse.getBody());
    }

    @Test
    public void testExecutePost() throws Exception {
        System.out.println("HttpUrlConnectionImplTest ---> testExecutePost");
        when(apiCommandMock.getHttpMethod()).thenReturn(POST);

        ApiResponse apiResponse = httpUrlConnectionMock.executeCommand(apiCommandMock);

        assertNotNull(apiResponse);
        assertEquals(200, apiResponse.getStatusCode());
        assertEquals(apiResponseBody, apiResponse.getBody());
    }
}

