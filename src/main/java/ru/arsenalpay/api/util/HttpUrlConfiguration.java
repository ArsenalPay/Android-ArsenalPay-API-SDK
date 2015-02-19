package ru.arsenalpay.api.util;

public class HttpUrlConfiguration {

    private int readTimeout;
    private int connectionTimeout;

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
