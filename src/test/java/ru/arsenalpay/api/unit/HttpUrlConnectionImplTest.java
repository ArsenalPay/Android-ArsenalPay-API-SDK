package ru.arsenalpay.api.unit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import ru.arsenalpay.api.client.ApiResponse;
import ru.arsenalpay.api.client.impl.HttpUrlConnectionImpl;
import ru.arsenalpay.api.command.ApiCommand;
import ru.arsenalpay.api.exception.InternalApiException;
import ru.arsenalpay.api.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static ru.arsenalpay.api.enums.HttpMethod.GET;
import static ru.arsenalpay.api.enums.HttpMethod.POST;

public class HttpUrlConnectionImplTest {

    private static final String BASE_URI = "https://arsenalpay.ru/init_pay_mk/";

    private Map<String, String> params;

    private String apiResponseBody;

    @Spy
    private HttpUrlConnectionImpl httpUrlConnectionSpy = new HttpUrlConnectionImpl();

    @Mock
    private HttpURLConnection connectionMock;

    @Mock
    private ApiResponse apiResponseMock;

    @Mock
    private ApiCommand apiCommandMock;

    @Mock
    private InputStream inputStreamMock;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        apiResponseBody = Utils.getStringFromFile("src/test/java/ru/arsenalpay/api/unit/support/api_ok_response.xml");

        params = new HashMap<String, String>() {{
            put("SIGN", "726142fc2d784895406b8a7b1ba8eaad");
            put("PHONE", "9140001111");
            put("FUNCTION", "init_pay_mk");
            put("CURRENCY", "RUR");
            put("ID", "9987");
            put("AMOUNT", "125.0");
            put("ACCOUNT", "123456789");
        }};

        when(apiCommandMock.getBaseUri()).thenReturn(BASE_URI);
        when(apiCommandMock.getParams()).thenReturn(params);

        doReturn(connectionMock).when(httpUrlConnectionSpy).getConnection(apiCommandMock);
        doReturn(apiResponseBody).when(httpUrlConnectionSpy).readDataFromHTTP(any(InputStream.class));

        when(connectionMock.getResponseCode()).thenReturn(200);

        when(apiResponseMock.getBody()).thenReturn(apiResponseBody);
        when(apiResponseMock.getStatusCode()).thenReturn(200);
    }

    @Test
    public void testExecuteGet() throws IOException, InternalApiException {
        System.out.println("HttpUrlConnectionImplTest ---> testExecuteGet");

        when(apiCommandMock.getHttpMethod()).thenReturn(GET);

        ApiResponse apiResponse = httpUrlConnectionSpy.executeCommand(apiCommandMock);

        assertNotNull(apiResponse);
        assertEquals(200, apiResponse.getStatusCode());
        assertEquals(apiResponseBody, apiResponse.getBody());
    }

    @Test
    public void testExecutePost() throws IOException, InternalApiException {
        System.out.println("HttpUrlConnectionImplTest ---> testExecutePost");

        when(apiCommandMock.getHttpMethod()).thenReturn(POST);

        ApiResponse apiResponse = httpUrlConnectionSpy.executeCommand(apiCommandMock);

        assertNotNull(apiResponse);
        assertEquals(200, apiResponse.getStatusCode());
        assertEquals(apiResponseBody, apiResponse.getBody());
    }

}

