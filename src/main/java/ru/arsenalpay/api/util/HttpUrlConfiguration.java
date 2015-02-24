package ru.arsenalpay.api.util;

public class HttpUrlConfiguration {

    private final static int DEFAULT_CONNECTION_TIMEOUT=20000;
    private final static int DEFAULT_READ_TIMEOUT=20000;

    private int readTimeout;
    private int connectionTimeout;

    public static HttpUrlConfiguration getDefaultConfiguration(){
        HttpUrlConfiguration configuration = new HttpUrlConfiguration();
        configuration.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        configuration.setReadTimeout(DEFAULT_READ_TIMEOUT);
        return configuration;
    }

    public int getReadTimeout(){
        return readTimeout;
    }
    public void setReadTimeout(int value){
        readTimeout = value;
    }
    public int getConnectionTimeout(){
        return connectionTimeout;
    }
    public void setConnectionTimeout(int value){
        connectionTimeout = value;
    }
}
