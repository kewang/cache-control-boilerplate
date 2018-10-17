package tw.kewang.caches;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public abstract class ETagCache {
    private static final Logger LOG = LoggerFactory.getLogger(ETagCache.class);

    private static MessageDigest MD = null;

    private String key;
    private String value;
    private String extra;

    static {
        try {
            MD = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Caught Exception:", e);
        }
    }

    public String getKey() {
        return key;
    }

    public ETagCache setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ETagCache setValue(String value) {
        this.value = value;
        return this;
    }

    public String getExtra() {
        return extra;
    }

    public ETagCache setExtra(String extra) {
        this.extra = extra;
        return this;
    }

    protected String generateMD5(String data) {
        return toMD5String(data);
    }

    private String toMD5String(String data) {
        byte[] digest = null;

        if (null != MD) {
            MD.update(data.getBytes(Charset.defaultCharset()));

            digest = MD.digest();
        }

        return Base64.getEncoder().encodeToString(digest).trim();
    }

    public abstract void invalidateAll();

    public abstract void invalidate(String key);

    public abstract Object getCache();

    public abstract void store();

    public abstract String toHash();

    public static class ETag {
        private String etagKey;
        private String etagValue;
        private String etagExtra;

        public ETag(String etagKey, String etagValue) {
            this.etagKey = etagKey;
            this.etagValue = etagValue;
        }

        public String getETagKey() {
            return etagKey;
        }

        public String getETagValue() {
            return etagValue;
        }

        public String getETagExtra() {
            return etagExtra;
        }

        public ETag setETagExtra(String etagExtra) {
            this.etagExtra = etagExtra;
            return this;
        }
    }
}