package tw.kewang.caches;

import com.google.common.cache.Cache;
import tw.kewang.resources.EncryptionUtils;

public class GetArticlesETagCache extends ETagCache {
    @Override
    public void invalidateAll() {
        ETagCacheManager.invalidateAll(getClass());
    }

    @Override
    public void invalidate(String key) {
        ETagCacheManager.invalidate(getClass(), key);
    }

    @Override
    public Cache<String, ETag> getCache() {
        return ETagCacheManager.getCache(getClass());
    }

    @Override
    public void store() {
        ETagCacheManager.store(getClass(), getKey(), new ETag(toHash(), getValue()));
    }

    @Override
    public String toHash() {
        return EncryptionUtils.toMD5String(getValue());
    }
}