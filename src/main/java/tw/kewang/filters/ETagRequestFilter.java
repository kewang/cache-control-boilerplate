package tw.kewang.filters;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheStats;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.kewang.SysInfoHolder;
import tw.kewang.caches.ETagCache;
import tw.kewang.caches.ETagCache.ETag;
import tw.kewang.filters.annotations.Cache.KeyType;
import tw.kewang.resources.ResponseUtils;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;

public class ETagRequestFilter implements ContainerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(ETagRequestFilter.class);

    private Class<? extends ETagCache> cacheType;
    private KeyType[] keyType;
    private String[] cacheHeaders;
    private String[] cachePathParameters;
    private String[] cacheQueryParameters;
    private KeyType cacheExtraType;
    private String cacheExtraValue;

    public ETagRequestFilter(tw.kewang.filters.annotations.Cache cache) {
        this.cacheType = cache.cacheType();
        this.keyType = cache.keyType();
        this.cacheHeaders = cache.headers();
        this.cachePathParameters = cache.pathParameters();
        this.cacheQueryParameters = cache.queryParameters();
        this.cacheExtraType = cache.extraType();
        this.cacheExtraValue = cache.extraValue();
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Cache cache = null;

        try {
            cache = (Cache) cacheType.newInstance().getCache();
        } catch (Exception e) {
            LOG.error("Caught Exception: {}", e);
        }

        if (cache == null) {
            return;
        }

        ETag etagObj = retrieveETagObj(requestContext, cache);

        if (etagObj == null) {
            return;
        }

        String etagKey = etagObj.getETagKey();

        String ifNoneMatch = requestContext.getHeaderString(HttpHeaders.IF_NONE_MATCH);

        if (StringUtils.isEmpty(ifNoneMatch)) {
            // if no exists If-None-Match header, return from cache directly
            requestContext.abortWith(ResponseUtils.ok(etagObj.getETagValue(), etagKey));
        } else if (etagKey.equals(ifNoneMatch)) {
            // if exists If-None-Match header, check it if equals cache key, return 304 only
            requestContext.abortWith(ResponseUtils.notModified(ifNoneMatch));
        }

        printCacheStats(cache);
    }

    private String extractExtraValue(ContainerRequestContext requestContext) {
        switch (cacheExtraType) {
            case USER_ID:
                return SysInfoHolder.getSysInfo().getUserId();
            case HEADER:
                return requestContext.getHeaderString(cacheExtraValue);
            case PATH_PARAMETER:
                return requestContext.getUriInfo().getPathParameters().getFirst(cacheExtraValue);
            case QUERY_PARAMETER:
                return requestContext.getUriInfo().getQueryParameters().getFirst(cacheExtraValue);
            case UNDEFINED:
            default:
                return null;
        }
    }

    private ETag retrieveETagObj(ContainerRequestContext requestContext, Cache cache) {
        String userId = null;
        List<String> headers = new ArrayList<>();
        List<String> pathParameters = new ArrayList<>();
        List<String> queryParameters = new ArrayList<>();

        for (KeyType type : keyType) {
            switch (type) {
                case USER_ID:
                    userId = SysInfoHolder.getSysInfo().getUserId();

                    break;
                case HEADER:
                    headers = retrieveKey(requestContext.getHeaders(), cacheHeaders);

                    break;
                case PATH_PARAMETER:
                    pathParameters = retrieveKey(requestContext.getUriInfo().getPathParameters(), cachePathParameters);

                    break;
                case QUERY_PARAMETER:
                    queryParameters = retrieveKey(requestContext.getUriInfo().getQueryParameters(), cacheQueryParameters);

                    break;
            }
        }

        ETag etag = (ETag) cache.getIfPresent(combineKey(userId, headers, pathParameters, queryParameters));

        if (etag == null) {
            return null;
        }

        String extra = extractExtraValue(requestContext);

        if (StringUtils.isEmpty(extra)) {
            return etag;
        }

        if (extra.equals(etag.getETagExtra())) {
            return etag;
        }

        return null;
    }

    private List<String> retrieveKey(MultivaluedMap<String, String> map, String[] cacheKey) {
        List<String> list = new ArrayList<>();

        for (String k : cacheKey) {
            list.add(map.getFirst(k));
        }

        return list;
    }

    private String combineKey(String userId, List<String> headers, List<String> pathParameters, List<String> queryParameters) {
        List<String> key = new ArrayList<>();

        if (StringUtils.isNotEmpty(userId)) {
            key.add(userId);
        }

        key = combineKey(headers, key);
        key = combineKey(pathParameters, key);
        key = combineKey(queryParameters, key);

        return StringUtils.join(key, "_");
    }

    private List<String> combineKey(List<String> list, List<String> key) {
        if (!list.isEmpty()) {
            key.add(StringUtils.join(list, "_"));
        }

        return key;
    }

    private void printCacheStats(Cache cache) {
        CacheStats stats = cache.stats();

        LOG.info("{}: hitRate: {}, hitCount: {}, missCount: {}", cacheType.getSimpleName(), stats.hitRate(), stats.hitCount(), stats.missCount());
    }
}