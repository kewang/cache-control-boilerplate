package tw.kewang.resources.responses;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.kewang.caches.ETagCache;

public abstract class BasicResponse {
    private static final Logger LOG = LoggerFactory.getLogger(BasicResponse.class);

    @SerializedName("result")
    protected boolean result = true;

    private transient ETagCache etagCache;

    public String toJson() {
        return new Gson().toJson(this);
    }

    public ETagCache getETagCache() {
        return etagCache;
    }

    public BasicResponse setETagCache(ETagCache etagCache) {
        this.etagCache = etagCache;

        return this;
    }

    public static class FailResponse extends BasicResponse {
        public FailResponse() {
            result = false;
        }
    }
}