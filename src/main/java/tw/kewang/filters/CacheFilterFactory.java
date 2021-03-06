package tw.kewang.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.kewang.filters.annotations.CacheControl;
import tw.kewang.filters.annotations.MaxAge;

import javax.annotation.Priority;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

@Priority(20)
public class CacheFilterFactory implements DynamicFeature {
    private static final Logger LOG = LoggerFactory.getLogger(CacheFilterFactory.class);

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        MaxAge maxAge = resourceInfo.getResourceMethod().getAnnotation(MaxAge.class);

        if (maxAge != null) {
            context.register(new MaxAgeResponseFilter(maxAge.value(), maxAge.unit()));
        }

        CacheControl cacheControl = resourceInfo.getResourceMethod().getAnnotation(CacheControl.class);

        if (cacheControl != null) {
            context.register(new ETagRequestFilter(cacheControl));
        }
    }
}