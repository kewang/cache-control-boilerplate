package tw.kewang.filters.annotations;

import tw.kewang.caches.ETagCache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheControl {
    Class<? extends ETagCache> cacheType();

    /**
     * order USER_ID, HEADER, PATH_PARAMETER, QUERY_PARAMETER
     */
    KeyType[] keyType();

    String[] headers() default "";

    String[] pathParameters() default "";

    String[] queryParameters() default "";

    KeyType extraType() default KeyType.UNDEFINED;

    String extraValue() default "";

    public enum KeyType {
        USER_ID,
        HEADER,
        PATH_PARAMETER,
        QUERY_PARAMETER,
        UNDEFINED
    }
}