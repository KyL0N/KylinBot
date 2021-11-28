package top.kylinbot.demo.modle;

public class osuUser {
    private String code;
    private String qq;
    private String osuID;

    public osuUser() {

    }

    public osuUser(String qq, String osuID, String code) {
        this.code = code;
        this.qq = qq;
        this.osuID = osuID;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setQQ(String qq) {
        this.qq = qq;
    }

    public void setOsuID(String osuID) {
        this.osuID = osuID;
    }

    public String getCode() {
        return code;
    }

    public String getQQ() {
        return qq;
    }

    public String getOsuID() {
        return osuID;
    }

    @Override
    public String toString() {
        return "key{" +
                "code:" + code +
                "qq:" + qq +
                "osuID:" + osuID +
                "}";
    }


}
