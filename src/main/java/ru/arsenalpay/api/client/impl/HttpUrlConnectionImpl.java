package ru.arsenalpay.api.client.impl;

import ru.arsenalpay.api.client.ApiClient;
import ru.arsenalpay.api.client.ApiResponse;
import ru.arsenalpay.api.client.ApiResponseImpl;
import ru.arsenalpay.api.command.ApiCommand;
import ru.arsenalpay.api.exception.InternalApiException;
import ru.arsenalpay.api.util.ArsenalpayUtils;
import ru.arsenalpay.api.util.HttpUrlConfiguration;
import ru.arsenalpay.api.util.RequestUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HttpUrlConnectionImpl implements ApiClient {

    private final static Logger log = Logger.getLogger(HttpUrlConnectionImpl.class.getName());
    private HttpUrlConfiguration configuration;

    public HttpUrlConnectionImpl() {

    }

    public HttpUrlConnectionImpl(HttpUrlConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public ApiResponse executeCommand(ApiCommand command) throws IOException, InternalApiException {

        log.info("ArsenalpayAPI-SDK HttpUrlConnectionImpl JAVA.UTIL.LOGGING");

        switch (command.getHttpMethod()) {
            case GET: {
                return executeGet(command);
            }
            case POST: {
                return executePost(command);
            }
            default: {
                String message = String.format("Http method is not supported: [%s]", command.getHttpMethod());
                log.info("ArsenalpayAPI-SDK " + message);
                throw new InternalApiException(message);
            }
        }
    }

    /**
     * Simply execute HTTP GET request
     * @param command -- api command
     * @return apiResponse -- apiResponse
     */
    private ApiResponse executeGet(ApiCommand command) {

        HttpURLConnection connection = null;
        URL url = null;
        try {
            url = new URL(command.getFullUri());
        } catch (Exception e) {
            log.log(Level.SEVERE, "ArsenalpayAPI-SDK HttpUrlConnectionImpl:doGet", e);
        }

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (configuration != null) {
                connection.setReadTimeout(configuration.getReadTimeout());
                connection.setConnectTimeout(configuration.getConnectionTimeout());
            }
            return getResponse(connection);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ArsenalpayAPI-SDK HttpUrlConnectionImpl:doGet", e);
        } finally {
            connection.disconnect();
        }
        return ApiResponseImpl.createEmpty();
    }
    /**
     * Simply execute HTTP POST request
     * @param command -- api command
     * @return apiResponse -- apiResponse
     */
    private ApiResponse executePost(ApiCommand command) {

        HttpURLConnection connection = null;
        URL url = null;
        OutputStream output;
        PrintWriter printWriter = null;
        String size = null;
        String str = null;

        try {
            url = new URL(command.getBaseUri());
            str = RequestUtils.mapToQueryString(command.getParams());
            size = String.valueOf(str.length());
            log.info("ArsenalpayAPI-SDK HttpUrlConnectionImpl URL:" + url.toString());
            log.info("ArsenalpayAPI-SDK HttpUrlConnectionImpl Post Data: " + str);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ArsenalpayAPI-SDK HttpUrlConnectionImpl:doPost", e);
        }

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", size);
            if (configuration != null) {
                connection.setReadTimeout(configuration.getReadTimeout());
                connection.setConnectTimeout(configuration.getConnectionTimeout());
            }

            output = connection.getOutputStream();
            printWriter = new PrintWriter(output);
            printWriter.write(str);
            printWriter.flush();
        } catch (Exception e) {
            log.log(Level.SEVERE, "ArsenalpayAPI-SDK HttpUrlConnectionImpl:doPost", e);
        } finally {
            if (printWriter != null) {
                try {
                    printWriter.close();
                } catch (Exception e) {
                    log.log(Level.SEVERE, "ArsenalpayAPI-SDK HttpUrlConnectionImpl:doPost", e);
                }
            }
        }

        try {
            return getResponse(connection);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ArsenalpayAPI-SDK HttpUrlConnectionImpl:doPost", e);
        } finally {
            connection.disconnect();
        }

        return ApiResponseImpl.createEmpty();
    }


    private ApiResponse getResponse(HttpURLConnection connection) throws IOException {

        InputStream inputStream = new BufferedInputStream(connection.getInputStream());
        int httpStatus = connection.getResponseCode();

        String data = ArsenalpayUtils.readDataFromHTTP(inputStream);
        if (!data.isEmpty()) {
            log.info("ArsenalpayAPI-SDK HttpUrlConnectionImpl Response data: " + data);
            return new ApiResponseImpl(httpStatus, data);
        }
        log.log(Level.INFO, "ArsenalpayAPI-SDK Response data is empty");
        return ApiResponseImpl.createEmpty();

    }

}
