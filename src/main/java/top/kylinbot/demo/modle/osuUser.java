package top.kylinbot.demo.modle;

import top.kylinbot.demo.service.OsuService;

public class osuUser {
    private int qq;
    private String osuID;
    private String accessToken;
    private String refreshToken;
    private long expire;


    public osuUser() {
        expire = System.currentTimeMillis();
    }

    public osuUser(int qq, String refreshToken) {
        this.qq = qq;
        this.refreshToken = refreshToken;
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

    public int getQQ() {
        return qq;
    }

    public String getOsuID() {
        return osuID;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public String getAccessToken(OsuService service) {
        if(isExpire())
            service.refreshToken(this);
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

    public void setNextExpireTime(long validTime) {
        expire = System.currentTimeMillis() + validTime * 1000;
    }

    public Boolean isExpire() {
        return System.currentTimeMillis() > expire;
    }

    @Override
    public String toString() {
        return "key{" +
                "qq:" + qq +
                "osuID:" + osuID +
                "}";
    }

}
