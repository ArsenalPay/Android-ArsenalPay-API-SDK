package ru.arsenalpay.api.client.impl;

import ru.arsenalpay.api.client.ApiClient;
import ru.arsenalpay.api.client.ApiResponse;
import ru.arsenalpay.api.client.ApiResponseImpl;
import ru.arsenalpay.api.command.ApiCommand;
import ru.arsenalpay.api.exception.InternalApiException;
import ru.arsenalpay.api.util.HttpUrlConfiguration;
import ru.arsenalpay.api.util.RequestUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Implementation of ApiClient interface</p>
 * <p>
 * <p>You can use default configuration of ApiClient or create your configuration, using HttpUrlConfiguration class</p>
 *
 * @author dejibqp
 */
public class HttpUrlConnectionImpl implements ApiClient {

    private final static Logger log = Logger.getLogger(HttpUrlConnectionImpl.class.getName());

    /**
     * Connection configuration
     */
    private final HttpUrlConfiguration configuration;

    /**
     * Default connection configuration
     */
    public HttpUrlConnectionImpl() {
        configuration = HttpUrlConfiguration.getDefaultConfiguration();
    }

    /**
     * Custom configuration of connection
     */
    public HttpUrlConnectionImpl(HttpUrlConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public ApiResponse executeCommand(ApiCommand command) throws IOException, InternalApiException {

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

    public ApiResponse executeGet(ApiCommand command) throws IOException {

        HttpURLConnection connection = getConnection(command);
        try {
            return getResponse(connection);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public ApiResponse executePost(ApiCommand command) throws IOException {

        HttpURLConnection connection = getConnection(command);
        try {
            return getResponse(connection);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * generate ApiResponse from server response
     *
     * @param connection
     * @return apiResponse
     * @throws IOException
     */
    private ApiResponse getResponse(HttpURLConnection connection) throws IOException {

        InputStream inputStream = new BufferedInputStream(connection.getInputStream());
        int httpStatus = connection.getResponseCode();

        String data = readDataFromHTTP(inputStream);
        if (!data.isEmpty()) {
            log.info("ArsenalpayAPI-SDK HttpUrlConnectionImpl Response data: " + data);
            return new ApiResponseImpl(httpStatus, data);
        }
        log.info("ArsenalpayAPI-SDK Response data is empty");
        return ApiResponseImpl.createEmpty();
    }

    /**
     * Generate,open and return connection with defined params of connection, in case of POST request also fill request body
     * I made it public only for testing
     *
     * @param command
     * @return connection
     * @throws IOException
     */
    public HttpURLConnection getConnection(ApiCommand command) throws IOException {

        switch (command.getHttpMethod()) {
            case GET: {
                URL url = new URL(command.getFullUri());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                if (configuration != null) {
                    connection.setReadTimeout(configuration.getReadTimeout());
                    connection.setConnectTimeout(configuration.getConnectionTimeout());
                }
                return connection;
            }
            case POST: {
                PrintWriter printWriter = null;

                URL url = new URL(command.getBaseUri());
                String str = RequestUtils.mapToQueryString(command.getParams());
                String size = String.valueOf(str.length());
                log.info("ArsenalpayAPI-SDK POST params: "+str);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Length", size);
                if (configuration != null) {
                    connection.setReadTimeout(configuration.getReadTimeout());
                    connection.setConnectTimeout(configuration.getConnectionTimeout());
                }
                try {
                    printWriter = new PrintWriter(connection.getOutputStream());
                    printWriter.write(str);
                    printWriter.flush();
                } finally {
                    if (printWriter != null) {
                        try {
                            printWriter.close();
                        } catch (Exception e) {
                            log.log(Level.SEVERE, "ArsenalpayAPI-SDK HttpUrlConnectionImpl:doPost", e);
                        }
                    }
                }

                return connection;
            }
            default:
                return null;
        }
    }

    /**
     * Reading xml-formatted server response
     *
     * @param inputStream
     * @return response xml-formatted response as string
     * @throws IOException
     */
    public String readDataFromHTTP(InputStream inputStream) throws IOException {

        StringBuilder response = new StringBuilder();
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            if (!response.toString().isEmpty())
                return response.toString();
        } finally {
            try {
                bufferedReader.close();
            } catch (NullPointerException e) {
                log.log(Level.SEVERE, "ArsenalpayAPI-SDK", e);
            }
        }
        return null;
    }

}
