package tw.kewang.resources;

public interface Service<REQUEST extends Request, RESPONSE extends Response> {
    RESPONSE execute(REQUEST request) throws Exception;
}