package id.winpay.winpaysdk.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Reza Ishaq Maulana <es.mgr@bm.co.id>
 */
public final class Encrypt {

    public static String openssl_encrypt(String data, String strKey) throws Exception {
        String ivs = hash(strKey).substring(0, 16);
        String keyhash = hash(strKey).substring(0, 32);
        Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(keyhash.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(ivs.getBytes(), 0, ciper.getBlockSize());

        // Encrypt
        ciper.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encryptedCiperBytes = Base64.encode(ciper.doFinal(data.getBytes()), Base64.DEFAULT);

//        String s = new String(encryptedCiperBytes);
        return Base64.encodeToString(encryptedCiperBytes, Base64.DEFAULT);
    }

    public static String sha1(String text) {
        String res = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] textBytes = text.getBytes("UTF-8");
            md.update(textBytes, 0, textBytes.length);
            byte[] sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (UnsupportedEncodingException ignored) {
        } catch (NoSuchAlgorithmException ignored) {
        }
        return res;
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    private static String hash(String iv) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(iv.getBytes(Charset.forName("UTF-8")));
        return bytesToHex(encodedhash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte aHash : hash) {
            String hex = Integer.toHexString(0xff & aHash);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}