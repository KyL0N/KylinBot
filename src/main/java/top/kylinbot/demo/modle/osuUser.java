package top.kylinbot.demo.modle;

public class osuUser {
    private String code;
    private int qq;
    private String osuID;
    private String accessToken;
    private String refreshToken;
    private long expire;

    public osuUser(int qq, String osuID, String code, String accessToken, String refreshToken, long expire) {
        this.code = code;
        this.qq = qq;
        this.osuID = osuID;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expire = expire;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setQQ(int qq) {
        this.qq = qq;
    }

    public void setOsuID(String osuID) {
        this.osuID = osuID;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCode() {
        return code;
    }

    public int getQQ() {
        return qq;
    }

    public String getOsuID() {
        return osuID;
    }


    public String getAccessToken() {
        return accessToken;
    }


    public String getRefreshToken() {
        return refreshToken;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "key{" +
                "qq:" + qq +
                "osuID:" + osuID +
                "code:" + code +
                "}";
    }

}
