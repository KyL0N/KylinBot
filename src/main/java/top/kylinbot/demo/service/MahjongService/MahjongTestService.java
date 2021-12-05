package top.kylinbot.demo.service.MahjongService;

import javax.script.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;
import org.graalvm.polyglot.*;

public class MahjongTestService {
    public static void mahjongTest() throws ScriptException {
//        ScriptEngine js = new ScriptEngineManager().getEngineByName("nashorn");
//        Bindings bindings = js.getBindings(ScriptContext.ENGINE_SCOPE);
//        bindings.put("stdout", System.out);
//        js.eval("stdout.println(Math.cos(Math.PI));");
//        // Prints "-1.0" to the standard output stream.
        Context context = Context.create();
        context.eval("js","src/main/java/top/kylinbot/demo/service/MahjongService/judgement.js");
//        context.eval("js", "");

    }
    public static void hello(String what) {
        System.out.println("Hello "+what);
    }
}
