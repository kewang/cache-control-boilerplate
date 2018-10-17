package tw.kewang.caches;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.kewang.caches.ETagCache.ETag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ETagCacheManager {
    private static final Logger LOG = LoggerFactory.getLogger(ETagCacheManager.class);
    private static final String NAMESPACE = "ns";
    private static final String PATH_INVALIDATION = "/caches/invalidation";
    private static final Map<Class<? extends ETagCache>, Cache<String, ETag>> CLASSES = new HashMap<>();

    private static CuratorFramework zkClient;

    public static void init() {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder().recordStats();

        register(GetArticlesETagCache.class, builder.build());

        connectZookeeper();
    }

    private static void connectZookeeper() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        zkClient = CuratorFrameworkFactory.builder().connectString("localhost")
                .sessionTimeoutMs(5000).connectionTimeoutMs(5000).retryPolicy(retryPolicy)
                .namespace(NAMESPACE).build();

        zkClient.start();

        NodeCache cache = new NodeCache(zkClient, PATH_INVALIDATION);

        cache.getListenable().addListener(new InvalidateOtherCacheListener(cache));

        try {
            cache.start();
        } catch (Exception e) {
            LOG.error("Caught Exception:", e);
        }
    }

    private static void register(Class<? extends ETagCache> clazz, Cache<String, ETag> cache) {
        if (CLASSES.containsKey(clazz)) {
            LOG.warn("Register duplicate {}", clazz.getSimpleName());

            return;
        }

        CLASSES.put(clazz, cache);

        LOG.info("Register {}", clazz.getSimpleName());
    }

    public static Cache<String, ETag> getCache(Class<? extends ETagCache> clazz) {
        return CLASSES.get(clazz);
    }

    public static void invalidateAll(Class<? extends ETagCache> clazz) {
        invalidate(clazz, null, true);
    }

    public static void invalidateAll(Class<? extends ETagCache> clazz, boolean syncCache) {
        invalidate(clazz, null, syncCache);
    }

    public static void invalidate(Class<? extends ETagCache> clazz, String key) {
        invalidate(clazz, key, true);
    }

    public static void invalidate(Class<? extends ETagCache> clazz, String key, boolean syncCache) {
        LOG.info("Invalidate: <{}, {}, {}>", clazz.getSimpleName(), key, syncCache);

        if (syncCache) {
            invalidateOtherCache(clazz, key);
        } else {
            if (StringUtils.isEmpty(key)) {
                getCache(clazz).invalidateAll();
            } else {
                getCache(clazz).invalidate(key);
            }
        }
    }

    public static void store(Class<? extends ETagCache> clazz, String key, ETag etag) {
        getCache(clazz).put(key, etag);
    }

    private static void invalidateOtherCache(Class<? extends ETagCache> clazz, String key) {
        String data = "/" + UUID.randomUUID().toString() + "/caches/" + clazz.getSimpleName();

        if (StringUtils.isNotEmpty(key)) {
            data += "/" + key;
        }

        try {
            // check znode if exists
            Stat stat = zkClient.checkExists().forPath(PATH_INVALIDATION);

            if (stat == null) {
                zkClient.create().inBackground().forPath(PATH_INVALIDATION, data.getBytes());
            } else {
                zkClient.setData().inBackground().forPath(PATH_INVALIDATION, data.getBytes());
            }
        } catch (Exception e) {
            LOG.error("Caught Exception:", e);
        }
    }

    private static class InvalidateOtherCacheListener extends LeaderSelectorListenerAdapter implements NodeCacheListener {
        private LeaderSelector selector;
        private NodeCache cache;
        private String nodeCachePath;

        public InvalidateOtherCacheListener(NodeCache cache) {
            this.cache = cache;
        }

        @Override
        public void nodeChanged() throws Exception {
            ChildData data = cache.getCurrentData();

            if (data == null) {
                return;
            }

            String nodePath = data.getPath();

            LOG.debug("nodePath: {}", nodePath);

            String[] pathParts = StringUtils.split(nodePath, "/");

            if ("invalidation".equals(pathParts[1])) {
                nodeCachePath = new String(data.getData());

                notifyNode();
            }
        }

        @Override
        public void takeLeadership(CuratorFramework client) throws Exception {
            // invalidate cache
            processing();

            // give up leadership, then elect other leader
            selector.close();
        }

        private void notifyNode() {
            LOG.debug("notifyNode: {}", nodeCachePath);

            nodeCachePath = StringUtils.substring(nodeCachePath, 37);

            selector = new LeaderSelector(zkClient, nodeCachePath, this);

            selector.start();
        }

        private void processing() {
            String[] parts = StringUtils.split(nodeCachePath, "/");

            String testClass = parts[parts.length - 1];

            Class<? extends ETagCache> invalidateClass = getInvalidateClass(testClass);

            if (invalidateClass != null) {
                // invalidateAll
                invalidateAll(invalidateClass, false);
            } else {
                testClass = parts[parts.length - 2];

                invalidateClass = getInvalidateClass(testClass);

                if (invalidateClass != null) {
                    // invalidate by key
                    invalidate(invalidateClass, parts[parts.length - 1], false);
                }
            }
        }

        private Class<? extends ETagCache> getInvalidateClass(String testClass) {
            for (Class<? extends ETagCache> clazz : CLASSES.keySet()) {
                if (testClass.equals(clazz.getSimpleName())) {
                    return clazz;
                }
            }

            return null;
        }
    }
}