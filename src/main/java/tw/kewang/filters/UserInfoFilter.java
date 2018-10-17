package tw.kewang.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.kewang.UserInfo;
import tw.kewang.UserInfoHolder;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

@Priority(10)
public class UserInfoFilter implements ContainerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(UserInfoFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String userId = requestContext.getHeaderString("uid");
        String accessToken = requestContext.getHeaderString("token");

        UserInfoHolder.setUserInfo(new UserInfo(userId, accessToken));
    }
}