package ru.arsenalpay.api.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * <p>Simple SecurityUtils class for ArsenalPay API SDK.</p>
 *
 * @author adamether
 */
public class SecurityUtils {

    public static String md5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }

    /**
     * Create signature for request values
     *
     * @param secret secret word
     * @param values values in strict order as described in PS API
     * @return signature
     */
    public static String getSignature(String secret, String... values) {
        if (isBlank(secret)) {
            throw new IllegalArgumentException("The value of 'secret' can't be blank.");
        }
        if (values == null || values.length < 1) {
            throw new IllegalArgumentException("The 'values' can't be null or empty.");
        }

        final StringBuilder sb = new StringBuilder();
        for (String value : values) {
            sb.append(md5(value));
        }
        sb.append(md5(secret));
        String testHash = md5(sb.toString());

        return testHash;
    }

}
