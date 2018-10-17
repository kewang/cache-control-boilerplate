package tw.kewang.resources.services;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.kewang.UserInfo;
import tw.kewang.UserInfoHolder;
import tw.kewang.caches.Cacheable;
import tw.kewang.caches.ETagCache;
import tw.kewang.resources.requests.BasicRequest;
import tw.kewang.resources.responses.BasicResponse;
import tw.kewang.resources.responses.BasicResponse.FailResponse;

import java.lang.reflect.ParameterizedType;

public abstract class BasicService<REQ extends BasicRequest, RES extends BasicResponse> {
    protected static final Logger LOG = LoggerFactory.getLogger(BasicService.class);

    private String cacheKey;
    private String cacheValue;
    private String cacheExtra;

    public RES execute(REQ request) {
        try {
            long startTime = System.nanoTime();

            BasicResponse response = processing(request);

            buildCache(response);

            UserInfo userInfo = UserInfoHolder.getUserInfo();

            LOG.info("UserId: {}, Request: {}, Spend Time: {}us", (userInfo == null) ? null : userInfo.userId, (request == null) ? null : request.getClass().getSimpleName(), (System.nanoTime() - startTime) / 1000);

            return (RES) response;
        } catch (Exception e) {
            LOG.error("Caught Exception:", e);

            return (RES) new FailResponse();
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
                LOG.error("Caught Exception:", e);
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
                LOG.error("Caught Exception:", e);
            }
        }
    }
}