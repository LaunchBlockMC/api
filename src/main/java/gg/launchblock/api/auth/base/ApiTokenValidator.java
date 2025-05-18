package gg.launchblock.api.auth.base;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.util.Base64;

@Slf4j
@UtilityClass
public class ApiTokenValidator {

    private static final int IV_LENGTH = 12;
    private static final byte[] FIXED_IV = new byte[IV_LENGTH]; // All zeros IV

    private static final String secretKeyString;

    static {
        secretKeyString = System.getenv("API_TOKEN_SECRET_KEY");
        if (ApiTokenValidator.secretKeyString == null || ApiTokenValidator.secretKeyString.length() < 16) {
            throw new IllegalStateException("API_TOKEN_SECRET_KEY is not set or too short");
        }
    }

    public static String encrypt(final String plaintext) throws Exception {
        return ApiTokenValidator.encryptAES(plaintext);
    }

    public static String decrypt(final String encryptedData) throws Exception {
        return ApiTokenValidator.decryptAES(encryptedData);
    }

    private static String encryptAES(final String plaintext) throws Exception {
        final SecretKey secretKey = ApiTokenValidator.getSecretKey();
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, FIXED_IV));

        final byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

        final ByteBuffer byteBuffer = ByteBuffer.allocate(IV_LENGTH + ciphertext.length);
        byteBuffer.put(FIXED_IV);
        byteBuffer.put(ciphertext);
        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }

    private static String decryptAES(final String encryptedData) throws Exception {
        final byte[] decodedBytes = Base64.getDecoder().decode(encryptedData.replaceAll(",", ""));

        final ByteBuffer byteBuffer = ByteBuffer.wrap(decodedBytes);
        final byte[] iv = new byte[IV_LENGTH];
        byteBuffer.get(iv);
        final byte[] ciphertext = new byte[byteBuffer.remaining()];
        byteBuffer.get(ciphertext);

        final SecretKey secretKey = ApiTokenValidator.getSecretKey();
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));

        return new String(cipher.doFinal(ciphertext));
    }

    private static SecretKey getSecretKey() throws Exception {
        return new SecretKeySpec(ApiTokenValidator.secretKeyString.getBytes(), "AES");
    }
} 