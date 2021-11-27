package top.kylinbot.demo.modle;

public class osuUser {
    private String code;
    private String qq;

    public osuUser() {

    }

    public osuUser(String code, String qq) {
        this.code = code;
        this.qq = qq;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setQQ(String qq) {
        this.qq = qq;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "key{" +
                "code:" + code +
                "qq:" + qq +
                "}";
    }


}
