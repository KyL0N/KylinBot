package top.kylinbot.demo.service;

import top.kylinbot.demo.controller.HttpServer;
import top.kylinbot.demo.modle.osuUser;

import java.io.IOException;

public class OsuBindService {
    public static int bindServer(Integer port, osuUser user) {
        try {
            HttpServer t = new HttpServer(port);
            int status = t.Run(user);
            t.stop();
            return status;
        } catch (IOException e){
            e.printStackTrace();
        }
        return 1;
    }
}
