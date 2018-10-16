package tw.kewang.caches;

public abstract class ETagCache {
    private String key;
    private String value;
    private String extra;

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