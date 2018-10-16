package tw.kewang.resources;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.kewang.SysInfo;
import tw.kewang.SysInfoHolder;
import tw.kewang.caches.Cacheable;
import tw.kewang.caches.ETagCache;

import java.lang.reflect.ParameterizedType;

public abstract class BasicService<REQ extends BasicRequest> implements Service<REQ, BasicResponse> {
    protected static final String DIVIDER = "_";
    private static final Logger LOG = LoggerFactory.getLogger(BasicService.class);

    private String cacheKey;
    private String cacheValue;
    private String cacheExtra;

    public BasicResponse execute(REQ request) {
        try {
            long startTime = System.nanoTime();

            BasicResponse response = processing(request);

            buildCache(response);

            SysInfo sysInfo = SysInfoHolder.getSysInfo();

            LOG.info("UserId: {}, Request: {}, Spend Time: {}us",
                    (sysInfo == null) ? null : sysInfo.getUserId(),
                    (request == null) ? null : request.getClass().getSimpleName(),
                    (System.nanoTime() - startTime) / 1000);

            return response;
        } catch (Exception e) {
            LOG.error("Caught Exception: {}", e);

            return new SimpleResponse().fail(e);
        }
    }

    private void buildCache(BasicResponse response) throws InstantiationException, IllegalAccessException {
        if (StringUtils.isNotEmpty(cacheKey) && StringUtils.isNotEmpty(cacheValue) && this instanceof Cacheable) {
            Class<? extends ETagCache> clazz = (Class<? extends ETagCache>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];

            ETagCache cache = clazz.newInstance();

            cache.setKey(cacheKey);
            cache.setValue(cacheValue);
            cache.setExtra(cacheExtra);

            cache.store();

            response.setETagCache(cache);
        }
    }

    protected abstract BasicResponse processing(REQ request) throws Exception;

    protected void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    protected void setCacheValue(String cacheValue) {
        this.cacheValue = cacheValue;
    }

    protected void setCacheExtra(String cacheExtra) {
        this.cacheExtra = cacheExtra;
    }

    protected void invalidateCache(String cacheKey) {
        if (this instanceof Cacheable) {
            Class<? extends ETagCache> clazz = (Class<? extends ETagCache>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];

            try {
                ETagCache cache = clazz.newInstance();

                cache.invalidate(cacheKey);
            } catch (Exception e) {
                LOG.error("Caught Exception: {}", e);
            }
        }
    }

    protected void invalidateAllCache() {
        if (this instanceof Cacheable) {
            Class<? extends ETagCache> clazz = (Class<? extends ETagCache>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];

            try {
                ETagCache cache = clazz.newInstance();

                cache.invalidateAll();
            } catch (Exception e) {
                LOG.error("Caught Exception: {}", e);
            }
        }
    }
}