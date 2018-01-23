package com.chinalife.enterprise.util;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class Md5SaltTool {
    private static final String HEX_NUMS_STR = "0123456789ABCDEF";
    private static final Integer SALT_LENGTH = Integer.valueOf(12);

    public static byte[] hexStringToByte(String hex) {
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] hexChars = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] =
                    ((byte) ("0123456789ABCDEF".indexOf(hexChars[pos]) << 4 | "0123456789ABCDEF".indexOf(hexChars[(pos + 1)])));
        }
        return result;
    }

    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    public static boolean validPassword(String password, String passwordInDb)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] pwdInDb = hexStringToByte(passwordInDb);

        byte[] salt = new byte[SALT_LENGTH.intValue()];

        System.arraycopy(pwdInDb, 0, salt, 0, SALT_LENGTH.intValue());

        MessageDigest md = MessageDigest.getInstance("MD5");

        md.update(salt);

        md.update(password.getBytes("UTF-8"));

        byte[] digest = md.digest();

        byte[] digestInDb = new byte[pwdInDb.length - SALT_LENGTH.intValue()];

        System.arraycopy(pwdInDb, SALT_LENGTH.intValue(), digestInDb, 0, digestInDb.length);
        if (Arrays.equals(digest, digestInDb)) {
            return true;
        }
        return false;
    }

    public static String getEncryptedPwd(String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] pwd = null;

        SecureRandom random = new SecureRandom();

        byte[] salt = new byte[SALT_LENGTH.intValue()];

        random.nextBytes(salt);


        MessageDigest md = null;

        md = MessageDigest.getInstance("MD5");

        md.update(salt);

        md.update(password.getBytes("UTF-8"));

        byte[] digest = md.digest();


        pwd = new byte[digest.length + SALT_LENGTH.intValue()];

        System.arraycopy(salt, 0, pwd, 0, SALT_LENGTH.intValue());

        System.arraycopy(digest, 0, pwd, SALT_LENGTH.intValue(), digest.length);
        for (int i = 0; i < pwd.length; i++) {
           // System.out.print(pwd[i]);
        }
        return byteToHexString(pwd);
    }
}
