package ru.arsenalpay.api.util;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArsenalpayUtils {
    private final static Logger log = Logger.getLogger(ArsenalpayUtils.class.getName());

    private static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(String filePath) throws IOException {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }

    public static String readDataFromHTTP(InputStream inputStream) {
        StringBuffer response = new StringBuffer();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            if (!response.toString().isEmpty())
                return response.toString();
        } catch (IOException e) {
            log.log(Level.SEVERE, "ArsenalpayAPI-SDK HttpUrlConnectionImpl:readData", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.log(Level.SEVERE, "ArsenalpayAPI-SDK HttpUrlConnectionImpl:readData", e);
                }
            }
        }
        return null;
    }
}
