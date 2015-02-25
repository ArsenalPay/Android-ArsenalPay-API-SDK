package ru.arsenalpay.api.functional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.arsenalpay.api.client.ApiResponse;
import ru.arsenalpay.api.client.impl.HttpUrlConnectionImpl;
import ru.arsenalpay.api.command.ApiCommand;
import ru.arsenalpay.api.enums.OperationStatus;
import ru.arsenalpay.api.exception.InternalApiException;
import ru.arsenalpay.api.facade.ApiCommandsFacade;
import ru.arsenalpay.api.facade.impl.ApiCommandsFacadeImpl;
import ru.arsenalpay.api.request.PaymentRequest;
import ru.arsenalpay.api.request.PaymentStatusRequest;
import ru.arsenalpay.api.response.PaymentResponse;
import ru.arsenalpay.api.response.PaymentStatusResponse;
import ru.arsenalpay.api.util.Utils;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Ярослав on 13.02.2015.
 */
public class ApiCommandsFacadeImplTest {

    String apiResponseBody;

    @Mock
    HttpUrlConnectionImpl httpUrlConnectionMock;

    @Mock
    ApiResponse apiResponseMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(apiResponseMock.getStatusCode()).thenReturn(200);
    }

    @Test
    public void testSuccessProcessPayment() throws Exception {
        System.out.println("ApiCommandsFacadeImplTest ---> testSuccessProcessPayment");

        apiResponseBody = Utils.getStringFromFile("src/test/java/ru/arsenalpay/api/unit/support/api_ok_response.xml");

        when(apiResponseMock.getBody()).thenReturn(apiResponseBody);
        when(httpUrlConnectionMock.executeCommand(any(ApiCommand.class))).thenReturn(apiResponseMock);

        ApiCommandsFacade apiCommandsFacade = new ApiCommandsFacadeImpl(httpUrlConnectionMock);

        PaymentRequest paymentRequest = new PaymentRequest.MobileBuilder()
                .payerId(9140001111L)
                .recipientId(123456789L)
                .amount(1.25D)
                .currency("RUR")
                .comment("Java-SDK-Test")
                .setTestMode()
                .build();

        PaymentResponse paymentResponse = apiCommandsFacade.requestPayment(paymentRequest);

        assertNotNull(paymentResponse);
        assertEquals(567456755678L, paymentResponse.getTransactionId().longValue());
        assertEquals(9147894125L, paymentResponse.getPayerId().longValue());
        assertEquals(123456L, paymentResponse.getRecipientId().longValue());
        assertTrue(new Double(52.40).equals(paymentResponse.getAmount()));
        assertEquals("OK", paymentResponse.getMessage());

        System.out.println("Response: " + paymentResponse);
    }

    @Test(expected = InternalApiException.class)
    public void testErrorProcessPayment() throws Exception {
        System.out.println("ApiCommandsFacadeImplTest ---> testErrorProcessPayment");

        apiResponseBody = Utils.getStringFromFile("src/test/java/ru/arsenalpay/api/unit/support/api_error_unrecognized_status_response.xml");

        when(apiResponseMock.getBody()).thenReturn(apiResponseBody);
        when(httpUrlConnectionMock.executeCommand(any(ApiCommand.class))).thenReturn(apiResponseMock);

        ApiCommandsFacade apiCommandsFacade = new ApiCommandsFacadeImpl(httpUrlConnectionMock);

        PaymentRequest paymentRequest = new PaymentRequest.MobileBuilder()
                .payerId(9140001111L)
                .recipientId(123456789L)
                .amount(1.25D)
                .currency("RUR")
                .comment("Java-SDK-Test")
                .setTestMode()
                .build();

        PaymentResponse paymentResponse = apiCommandsFacade.requestPayment(paymentRequest);
        System.out.println("Response: " + paymentResponse);
    }

    @Test
    public void testSuccessCheckPaymentStatus() throws Exception {
        System.out.println("ApiCommandsFacadeImplTest ---> testSuccessCheckPaymentStatus");

        apiResponseBody = Utils.getStringFromFile("src/test/java/ru/arsenalpay/api/unit/support/api_ok_pay_check_payment_status_response.xml");

        when(apiResponseMock.getBody()).thenReturn(apiResponseBody);
        when(httpUrlConnectionMock.executeCommand(any(ApiCommand.class))).thenReturn(apiResponseMock);

        ApiCommandsFacade apiCommandsFacade = new ApiCommandsFacadeImpl(httpUrlConnectionMock);

        final PaymentStatusRequest paymentStatusRequest = new PaymentStatusRequest(0L);

        final PaymentStatusResponse paymentStatusResponse = apiCommandsFacade.checkPaymentStatus(paymentStatusRequest);

        assertNotNull(paymentStatusResponse);
        assertEquals(123456789L, paymentStatusResponse.getTransactionId().longValue());
        assertEquals(123456L, paymentStatusResponse.getRecipientId().longValue());
        assertTrue(new Double(52.40).equals(paymentStatusResponse.getAmount()));
        assertEquals(9645565854L, paymentStatusResponse.getPayerId().longValue());
        assertEquals(new Date(1349060062000L), paymentStatusResponse.getDate());
        assertEquals(OperationStatus.SUCCESS, paymentStatusResponse.getMessage());
    }

    @Test
    public void testInProgressCheckPaymentStatus() throws Exception {
        System.out.println("ApiCommandsFacadeImplTest ---> testInProgressCheckPaymentStatus");

        apiResponseBody = Utils.getStringFromFile("src/test/java/ru/arsenalpay/api/unit/support/api_in_progress_check_payment_status_response.xml");

        when(apiResponseMock.getBody()).thenReturn(apiResponseBody);
        when(httpUrlConnectionMock.executeCommand(any(ApiCommand.class))).thenReturn(apiResponseMock);

        ApiCommandsFacade apiCommandsFacade = new ApiCommandsFacadeImpl(httpUrlConnectionMock);

        final PaymentStatusRequest paymentStatusRequest = new PaymentStatusRequest(0L);

        final PaymentStatusResponse paymentStatusResponse = apiCommandsFacade.checkPaymentStatus(paymentStatusRequest);

        assertNotNull(paymentStatusResponse);
        assertEquals(123456789L, paymentStatusResponse.getTransactionId().longValue());
        assertEquals(123456L, paymentStatusResponse.getRecipientId().longValue());
        assertTrue(new Double(52.40).equals(paymentStatusResponse.getAmount()));
        assertEquals(9645565854L, paymentStatusResponse.getPayerId().longValue());
        assertEquals(new Date(1349060062000L), paymentStatusResponse.getDate());
        assertEquals(OperationStatus.IN_PROGRESS, paymentStatusResponse.getMessage());
    }

    @Test
    public void testNotRegisteredCheckPaymentStatus() throws Exception {
        System.out.println("ApiCommandsFacadeImplTest ---> testNotRegisteredCheckPaymentStatus");

        apiResponseBody = Utils.getStringFromFile("src/test/java/ru/arsenalpay/api/unit/support/api_not_registered_check_payment_status_response.xml");

        when(apiResponseMock.getBody()).thenReturn(apiResponseBody);
        when(httpUrlConnectionMock.executeCommand(any(ApiCommand.class))).thenReturn(apiResponseMock);

        ApiCommandsFacade apiCommandsFacade = new ApiCommandsFacadeImpl(httpUrlConnectionMock);

        final PaymentStatusRequest paymentStatusRequest = new PaymentStatusRequest(0L);

        final PaymentStatusResponse paymentStatusResponse = apiCommandsFacade.checkPaymentStatus(paymentStatusRequest);

        assertNotNull(paymentStatusResponse);
        assertEquals(123456789L, paymentStatusResponse.getTransactionId().longValue());
        assertEquals(123456L, paymentStatusResponse.getRecipientId().longValue());
        assertTrue(new Double(52.40).equals(paymentStatusResponse.getAmount()));
        assertEquals(9645565854L, paymentStatusResponse.getPayerId().longValue());
        assertEquals(new Date(1349060062000L), paymentStatusResponse.getDate());
        assertEquals(OperationStatus.NOT_REGISTERED, paymentStatusResponse.getMessage());
    }

    @Test
    public void testRefusedCheckPaymentStatus() throws Exception {
        System.out.println("ApiCommandsFacadeImplTest ---> testRefusedCheckPaymentStatus");

        apiResponseBody = Utils.getStringFromFile("src/test/java/ru/arsenalpay/api/unit/support/api_refused_check_payment_status_response.xml");

        when(apiResponseMock.getBody()).thenReturn(apiResponseBody);
        when(httpUrlConnectionMock.executeCommand(any(ApiCommand.class))).thenReturn(apiResponseMock);

        ApiCommandsFacade apiCommandsFacade = new ApiCommandsFacadeImpl(httpUrlConnectionMock);

        final PaymentStatusRequest paymentStatusRequest = new PaymentStatusRequest(0L);

        final PaymentStatusResponse paymentStatusResponse = apiCommandsFacade.checkPaymentStatus(paymentStatusRequest);

        assertNotNull(paymentStatusResponse);
        assertEquals(123456789L, paymentStatusResponse.getTransactionId().longValue());
        assertEquals(123456L, paymentStatusResponse.getRecipientId().longValue());
        assertTrue(new Double(52.40).equals(paymentStatusResponse.getAmount()));
        assertEquals(9645565854L, paymentStatusResponse.getPayerId().longValue());
        assertEquals(new Date(1349060062000L), paymentStatusResponse.getDate());
        assertEquals(OperationStatus.REFUSED, paymentStatusResponse.getMessage());
    }

    @Test
    public void testInProgress2CheckPaymentStatus() throws Exception {
        System.out.println("ApiCommandsFacadeImplTest ---> testInProgress2CheckPaymentStatus");

        apiResponseBody = Utils.getStringFromFile("src/test/java/ru/arsenalpay/api/unit/support/api_ok_init_check_payment_status.xml");

        when(apiResponseMock.getBody()).thenReturn(apiResponseBody);
        when(httpUrlConnectionMock.executeCommand(any(ApiCommand.class))).thenReturn(apiResponseMock);

        ApiCommandsFacade apiCommandsFacade = new ApiCommandsFacadeImpl(httpUrlConnectionMock);

        final PaymentStatusRequest paymentStatusRequest = new PaymentStatusRequest(0L);

        final PaymentStatusResponse paymentStatusResponse = apiCommandsFacade.checkPaymentStatus(paymentStatusRequest);

        assertNotNull(paymentStatusResponse);
        assertEquals(123456789L, paymentStatusResponse.getTransactionId().longValue());
        assertEquals(123456L, paymentStatusResponse.getRecipientId().longValue());
        assertTrue(new Double(52.40).equals(paymentStatusResponse.getAmount()));
        assertEquals(9645565854L, paymentStatusResponse.getPayerId().longValue());
        assertEquals(new Date(1349060062000L), paymentStatusResponse.getDate());
        assertEquals(OperationStatus.IN_PROGRESS, paymentStatusResponse.getMessage());
    }

    @Test
    public void testEmptyTagPaymentStatus() throws Exception {
        System.out.println("ApiCommandsFacadeImplTest ---> testEmptyTagPaymentStatus");

        apiResponseBody = Utils.getStringFromFile("src/test/java/ru/arsenalpay/api/unit/support/api_empty_field_payment_status_response.xml");

        when(apiResponseMock.getBody()).thenReturn(apiResponseBody);
        when(httpUrlConnectionMock.executeCommand(any(ApiCommand.class))).thenReturn(apiResponseMock);

        ApiCommandsFacade apiCommandsFacade = new ApiCommandsFacadeImpl(httpUrlConnectionMock);

        try {
            final PaymentStatusRequest request = new PaymentStatusRequest(0L);
            final PaymentStatusResponse response = apiCommandsFacade.checkPaymentStatus(request);

            assertNotNull(response);
            assertEquals(1159374L, response.getTransactionId().longValue());
            assertNull(response.getRecipientId());
            assertNull(response.getPayerId());
            assertNull(response.getAmount());
            assertNull(response.getDate());
            assertEquals(OperationStatus.REFUSED, response.getMessage());
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail(e.getMessage());
        }
    }
}
