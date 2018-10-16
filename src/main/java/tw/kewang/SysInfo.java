package tw.kewang;

public class SysInfo {
    private String userId;
    private String accessToken;

    public SysInfo(String userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
    }

    public SysInfo setUserId(String userId) {
        this.userId = userId;

        return this;
    }

    public String getUserId() {
        return this.userId;
    }

    public SysInfo setAccessToken(String accessToken) {
        this.accessToken = accessToken;

        return this;
    }

    public String getAccessToken() {
        return this.accessToken;
    }
}