package encryption.AES;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.io.Serializable;
import java.security.*;

public class Aes_gcm {

    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";

    private static final Key SECRET_KEY = generateKey();
    private static final int GCM_TAG_LENGTH = 128; // bit
    private static final int GCM_IV_LENGTH = 12;   // 推荐12字节

    private static Key generateKey() {
        try {
            return KeyGenerator.getInstance("AES").generateKey();
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static SealedObject seal(final Serializable value) {
        try {
            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, getIV());
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, spec);
            return new SealedObject(value, cipher);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object unseal(final SealedObject sealedObject) {
        try {
            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, getIV());
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, spec);
            return sealedObject.getObject(cipher);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }
}
