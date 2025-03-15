package gg.launchblock.api.auth.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gg.launchblock.api.exception.base.LaunchBlockException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import static gg.launchblock.api.exception.base.BuiltInExceptions.EXPIRED_INVALID_JWT;

@Slf4j
@UtilityClass
public class TokenGenerator {
    private static final int IV_LENGTH = 12; // AES-GCM standard IV length
    private static final int KEY_LENGTH = 256; // AES key length in bits
    private static final int PBKDF2_ITERATIONS = 65536;

    public static String encrypt(final String password) throws Exception {
        return TokenGenerator.encryptAES(password);
    }

    public static String decrypt(final String encryptedData) throws Exception {
        return TokenGenerator.decryptAES(encryptedData);
    }

    public static SessionEntity tokenToSession(final String input) {
        try {
            final String decrypted = TokenGenerator.decrypt(input);
            return new ObjectMapper().readValue(decrypted, SessionEntity.class);
        } catch (final Exception e) {
            throw new LaunchBlockException(EXPIRED_INVALID_JWT);
        }
    }

    public static void verifyToken(final SessionEntity session, final String input) {
        final String decrypted;
        try {
            decrypted = TokenGenerator.decryptAES(input);
        } catch (final Exception e) {
            throw new LaunchBlockException(EXPIRED_INVALID_JWT, e);
        }

        final SessionEntity decodedSession;
        try {
            decodedSession = new ObjectMapper().readValue(decrypted, SessionEntity.class);
        } catch (final JsonProcessingException e) {
            TokenGenerator.log.error("Error deserializing provided input", e);
            throw new LaunchBlockException(EXPIRED_INVALID_JWT, e);
        }

        if (!session.equals(decodedSession) || session.isExpired()) {
            TokenGenerator.log.warn("Token data mismatch or expired");
            throw new LaunchBlockException(EXPIRED_INVALID_JWT);
        }
    }

    private static String encryptAES(final String plaintext) throws Exception {
        final SecureRandom secureRandom = new SecureRandom();
        final byte[] iv = new byte[TokenGenerator.IV_LENGTH];
        secureRandom.nextBytes(iv); // Generate a secure random IV

        final SecretKey secretKey = TokenGenerator.getSecretKey();
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));

        final byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

        // Concatenate IV and ciphertext, then encode as Base64
        final ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + ciphertext.length);
        byteBuffer.put(iv);
        byteBuffer.put(ciphertext);
        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }

    private static String decryptAES(final String encryptedData) throws Exception {
        final byte[] decodedBytes = Base64.getDecoder().decode(encryptedData.replaceAll(",", ""));

        final ByteBuffer byteBuffer = ByteBuffer.wrap(decodedBytes);
        final byte[] iv = new byte[TokenGenerator.IV_LENGTH];
        byteBuffer.get(iv);
        final byte[] ciphertext = new byte[byteBuffer.remaining()];
        byteBuffer.get(ciphertext);

        final SecretKey secretKey = TokenGenerator.getSecretKey();
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));

        return new String(cipher.doFinal(ciphertext));
    }

    private static SecretKey getSecretKey() throws Exception {
        final String secretKeyString = System.getenv("PASSWORD_SECRET_KEY");

        if (secretKeyString == null || secretKeyString.length() < 16) {
            throw new IllegalStateException("Secret key must be set in environment variables.");
        }

        final byte[] salt = TokenGenerator.getSalt();
        final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        final KeySpec spec = new PBEKeySpec(secretKeyString.toCharArray(), salt, TokenGenerator.PBKDF2_ITERATIONS, TokenGenerator.KEY_LENGTH);
        final SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    private static byte[] getSalt() {
        final String saltEnv = System.getenv("TOKEN_SALT");
        if (saltEnv == null || saltEnv.length() < 16) {
            throw new IllegalStateException("TOKEN_SALT must be set and at least 16 characters long.");
        }
        return saltEnv.getBytes();
    }

}
