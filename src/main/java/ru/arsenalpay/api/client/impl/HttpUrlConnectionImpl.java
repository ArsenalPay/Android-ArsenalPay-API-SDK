package ru.arsenalpay.api.client.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.arsenalpay.api.client.ApiClient;
import ru.arsenalpay.api.client.ApiResponse;
import ru.arsenalpay.api.client.ApiResponseImpl;
import ru.arsenalpay.api.command.ApiCommand;
import ru.arsenalpay.api.enums.HttpMethod;
import ru.arsenalpay.api.exception.InternalApiException;
import ru.arsenalpay.api.util.RequestUtils;

/**
 * Created by Ярослав on 04.02.2015.
 */
public class HttpUrlConnectionImpl implements ApiClient {

    private static Logger log = Logger.getLogger(HttpUrlConnectionImpl.class.getName());
    HttpURLConnection connection;

    @Override
    public ApiResponse executeCommand(ApiCommand command) throws IOException, InternalApiException {

        log.log(Level.INFO, "HTTPURLCONNECTION JAVA.UTIL.LOGGING");

            if (command.getHttpMethod() == HttpMethod.GET)
                return executeGet(command);
            else if(command.getHttpMethod()==HttpMethod.POST)
                return executePost(command);
            else{
                String message = String.format("Http method is not supported: [%s]", command.getHttpMethod());
                log.log(Level.SEVERE, message);
            }
            return ApiResponseImpl.createEmpty();
        }

        //Метод, отправляющий GET запрос и считывающий ответ от сервера
        private ApiResponse executeGet(ApiCommand command) {

            URL url = null;
            try {
                url = new URL(command.getFullUri());
            } catch (Exception e) {
                log.log(Level.SEVERE, "HttpUrlConnectionImpl:HttpUrlTask:doGet");
            }

            try {

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                //считывание ответа от сервера
                return getResponse(connection);

            } catch (Exception e) {
                log.log(Level.SEVERE, "HttpUrlConnectionImpl:HttpUrlTask:doGet");
            } finally {
                connection.disconnect();
            }
            return ApiResponseImpl.createEmpty();
        }

        //Метод, отправляющий POST запрос и считывающий ответ от сервера
        private ApiResponse executePost(ApiCommand command){

            URL url = null;
            OutputStream output = null;
            PrintWriter pw = null;
            String size = null;
            String str = null;

            //Создаем экземпляр класса URL, получаем строку с параметрами для передачи, получаем размер передаваемых данных
            try {
                url = new URL(command.getBaseUri());
                str = RequestUtils.mapToQueryString(command.getParams());
                size = String.valueOf(str.length());
                log.log(Level.SEVERE, "HttpUrlConnectionImpl URL:::"+url.toString());
                log.log(Level.SEVERE, "HttpUrlConnectionImpl Post Data: "+str);
            } catch (Exception e) {
                log.log(Level.SEVERE, "HttpUrlConnectionImpl:HttpUrlTask:doPost1111");
            }

            //Задаем параметры соединения
            try{
                connection = (HttpURLConnection) url.openConnection();
                log.log(Level.SEVERE, "HttpUrlConnectionImpl " + connection==null?"connection==null":"connection!=null");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Length", size);
                connection.connect();

            //Посылаем параметры запроса на сервер
                output = connection.getOutputStream();
                log.log(Level.SEVERE, "HttpUrlConnectionImpl" + output==null?"output==null":"output!=null");
                pw = new PrintWriter(output);
                pw.write(str);
                pw.flush();
            }catch(Exception e){
                log.log(Level.SEVERE,"HttpUrlConnectionImpl" + "HttpUrlConnectionImpl:HttpUrlTask:doPost22222");
            }
            finally {
                if(pw!=null){
                    try{
                        pw.close();
                    }catch(Exception e){
                        log.log(Level.SEVERE, "HttpUrlConnectionImpl:HttpUrlTask:doPost33333");
                        }
                }
            }

            //Получение ответа от сервера
            try {
                return getResponse(connection);
            } catch (Exception e) {
                log.log(Level.SEVERE, "HttpUrlConnectionImpl:HttpUrlTask:doPost44444");
            } finally {
                connection.disconnect();
            }

            return ApiResponseImpl.createEmpty();
        }


    private ApiResponse getResponse(HttpURLConnection connection) throws IOException {

        InputStream in = new BufferedInputStream(connection.getInputStream());
        int http_status = connection.getResponseCode();

        //Читаем данные
        String data = readData(in);
        if (!data.isEmpty()) {
            log.log(Level.SEVERE, "Response data: "+data);
            return new ApiResponseImpl(http_status, data);
        }
        log.log(Level.SEVERE, "Response data is empty");
        return ApiResponseImpl.createEmpty();

    }

    private String readData(InputStream in){
        StringBuffer response = new StringBuffer();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            if(!response.toString().isEmpty())
                return response.toString();
        } catch (IOException e) {
            log.log(Level.SEVERE, "HttpUrlConnectionImpl:HttpUrlTask:readData");
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.log(Level.SEVERE, "HttpUrlConnectionImpl:HttpUrlTask:readData");
                }
            }
        }
        return null;
    }
}