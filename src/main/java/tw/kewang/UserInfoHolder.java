package tw.kewang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInfoHolder {
    private static final Logger LOG = LoggerFactory.getLogger(UserInfoHolder.class);
    private static final ThreadLocal<UserInfo> USER_INFO_HOLDER = new ThreadLocal<>();

    public static void setUserInfo(UserInfo userInfo) {
        USER_INFO_HOLDER.set(userInfo);
    }

    public static UserInfo getUserInfo() {
        return USER_INFO_HOLDER.get();
    }

    public static void clear() {
        USER_INFO_HOLDER.set(null);
    }
}