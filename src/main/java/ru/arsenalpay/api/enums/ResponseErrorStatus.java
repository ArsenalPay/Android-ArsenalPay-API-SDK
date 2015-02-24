package ru.arsenalpay.api.enums;

import ru.arsenalpay.api.exception.ArsenalPayApiException;
import ru.arsenalpay.api.exception.InternalApiException;
import ru.arsenalpay.api.exception.PaymentException;

/**
 * Created by Ярослав on 24.02.2015.
 */
public enum ResponseErrorStatus {

    NULL(null, new InternalApiException("Api server status is null.")),
    EMPTY("", new InternalApiException("Api server status is empty.")),
    ERR_AMOUNT("ERR_AMOUNT", new PaymentException("Invalid amount value.")),
    ERR_SIGN("ERR_SIGN", new PaymentException("Invalid sign(hashcode).")),
    ERR_PHONE("ERR_PHONE", new PaymentException("Invalid payerId or value doesn't exist.")),
    ERR_CURRENCY("ERR_CURRENCY", new PaymentException("Invalid currency value or service doesn't support it.")),
    ERR_DATEFORMAT("ERR_DATEFORMAT", new PaymentException("Invalid date format.")),
    ERROR("ERROR", new InternalApiException("Unknown api server error.")),
    ERR_ACCESS("ERR_ACCESS", new InternalApiException("Unknown api server error.")),
    ERR_NODB("ERR_NODB", new InternalApiException("Unknown api server error.")),
    ERR_FUNCTION("ERR_FUNCTION", new InternalApiException("Unknown function or request body is empty"));

    private final String status;
    private final ArsenalPayApiException exception;

    ResponseErrorStatus(String status, ArsenalPayApiException exception) {
        this.status = status;
        this.exception = exception;
    }

    public String getStatus() {
        return status;
    }

    public ArsenalPayApiException getException() {
        return exception;
    }
}
