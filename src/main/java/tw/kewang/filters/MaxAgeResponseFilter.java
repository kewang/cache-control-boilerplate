package tw.kewang.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import java.util.concurrent.TimeUnit;

public class MaxAgeResponseFilter implements ContainerResponseFilter {
    private long seconds;

    public MaxAgeResponseFilter(long value, TimeUnit unit) {
        this.seconds = TimeUnit.SECONDS.convert(value, unit);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        if (responseContext.getEntityTag() != null) {
            responseContext.getHeaders().putSingle(HttpHeaders.CACHE_CONTROL, "max-age=" + seconds);
        }
    }
}