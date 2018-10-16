package tw.kewang.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.kewang.caches.ETagCache;

import javax.ws.rs.core.Response;

public class ResponseUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseUtils.class);

    private ResponseUtils() {
    }

    public static Response ok(BasicResponse response) {
        String json = response.toJson();

        Response.ResponseBuilder responseBuilder = Response.ok(json);

        ETagCache etagCache = response.getETagCache();

        if (etagCache != null) {
            return responseBuilder.tag(etagCache.toHash()).build();
        } else {
            return responseBuilder.build();
        }
    }

    public static Response ok(String json) {
        return Response.ok(json).build();
    }

    public static Response ok(String json, String etag) {
        return Response.ok(json).tag(etag).build();
    }

    public static Response notModified(String etag) {
        return Response.notModified(etag).build();
    }
}