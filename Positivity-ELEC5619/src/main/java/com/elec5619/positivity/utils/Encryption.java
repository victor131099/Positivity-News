package com.elec5619.positivity.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
    public static String encryptSHA256(String text) {
        final byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        try {
            final MessageDigest sha256MD = MessageDigest.getInstance("SHA-256");
            sha256MD.reset();
            sha256MD.update(bytes);
            final byte messageDigest[] = sha256MD.digest();

            final StringBuffer hexString = new StringBuffer();
            for (final byte element : messageDigest) {
                final String hex = Integer.toHexString(0xFF & element);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            text = hexString + "";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return text;
    }


}
