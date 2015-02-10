package ru.arsenalpay.api.exception;

/**
 * <p>HttpClientInitializingException</p>
 *
 * Exceptional situation caused by the fact that there was no right or configured in the process
 * of its construction unrecoverable error occured.
 *
 * @author adamether
 */
public class HttpClientInitializingException extends ArsenalPayApiRuntimeException {

    public HttpClientInitializingException() {
    }

    public HttpClientInitializingException(String s) {
        super(s);
    }

    public HttpClientInitializingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public HttpClientInitializingException(Throwable throwable) {
        super(throwable);
    }

}
