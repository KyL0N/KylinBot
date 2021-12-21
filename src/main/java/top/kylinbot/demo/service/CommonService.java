package top.kylinbot.demo.service;

import catcode.CatCodeUtil;

public class CommonService {
    public static String getHelpString() {
        return "所有命令:\n" +
                "NSFW图片:\t" + ".uestc " + ".zju " + ".thu" + "\n" +
                "牌理:" + "!t " + "\n  示例:!t 123s1p9p" + "\n" +
                "osu:\n" +
                "  绑定:" + "!oauth\n" +
                "  查询最近打图:" + "!kypr" + "\n" +
                "  个人信息:" + "!info" +
                "  bp:" + "!kybp" + "后加数字可查询指定bp";
    }

    public static String getNudge(String qq) {
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        return util.getStringCodeBuilder("nudge", false)
                .key("target").value(qq)
                .build();
    }
}
