package tw.kewang.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Base64;

public class EncryptionUtils {
    private static final Logger LOG = LoggerFactory.getLogger(EncryptionUtils.class);

    private static byte[] toMD5(String data) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            LOG.error("Caught Exception:", e);
        }

        byte[] digest = null;

        if (null != md) {
            md.update(data.getBytes(Charset.defaultCharset()));
            digest = md.digest();
        }

        return digest;
    }

    public static String toMD5String(String data) {
        byte[] bytes = toMD5(data);
        return Base64.getEncoder().encodeToString(bytes).trim();
    }
}