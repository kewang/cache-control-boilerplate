package tw.kewang.resources;

public interface Service<REQUEST, RESPONSE> {
    RESPONSE execute(REQUEST request) throws Exception;
}