package gg.launchblock.template.auth.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import gg.launchblock.template.exception.base.BuiltInExceptions;
import gg.launchblock.template.exception.base.LaunchBlockException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.spec.KeySpec;
import java.util.Base64;

@Slf4j
@UtilityClass
public class TokenValidator {

    private static final int IV_LENGTH = 12;
    private static final int KEY_LENGTH = 256;
    private static final int PBKDF2_ITERATIONS = 65536;

    private static final String secretKeyString;
    private static final String saltEnv;

    static {
        secretKeyString = System.getenv("PASSWORD_SECRET_KEY");
        saltEnv = System.getenv("TOKEN_SALT");
        if (TokenValidator.secretKeyString == null || TokenValidator.secretKeyString.length() < 16) {
            throw new IllegalStateException("PASSWORD_SECRET_KEY is not set");
        }
        if (TokenValidator.saltEnv == null || TokenValidator.saltEnv.length() < 16) {
            throw new IllegalStateException("TOKEN_SALT must be set and at least 16 characters long.");
        }
    }

    public static String decrypt(final String encryptedData) throws Exception {
        return TokenValidator.decryptAES(encryptedData);
    }

    public static SessionEntity tokenToSession(final String input) {
        try {
            final String decrypted = TokenValidator.decrypt(input);
            return new ObjectMapper().readValue(decrypted, SessionEntity.class);
        } catch (final Exception e) {
            throw new LaunchBlockException(BuiltInExceptions.EXPIRED_INVALID_JWT);
        }
    }

    private static String decryptAES(final String encryptedData) throws Exception {
        final byte[] decodedBytes = Base64.getDecoder().decode(encryptedData.replaceAll(",", ""));

        final ByteBuffer byteBuffer = ByteBuffer.wrap(decodedBytes);
        final byte[] iv = new byte[TokenValidator.IV_LENGTH];
        byteBuffer.get(iv);
        final byte[] ciphertext = new byte[byteBuffer.remaining()];
        byteBuffer.get(ciphertext);

        final SecretKey secretKey = TokenValidator.getSecretKey();
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));

        return new String(cipher.doFinal(ciphertext));
    }

    private static SecretKey getSecretKey() throws Exception {
        final byte[] salt = TokenValidator.getSalt();
        final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        final KeySpec spec = new PBEKeySpec(TokenValidator.secretKeyString.toCharArray(), salt, TokenValidator.PBKDF2_ITERATIONS, TokenValidator.KEY_LENGTH);
        final SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    private static byte[] getSalt() {
        return TokenValidator.saltEnv.getBytes();
    }

}