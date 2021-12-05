package top.kylinbot.demo.util;


import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;


public class HtmlUtil {
    public static String getTenHouResult(String tiles) throws Exception {
        String url = "https://tenhou.net/2/?q=" + tiles;

        WebClient wc = new WebClient(BrowserVersion.CHROME);
        wc.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true
        wc.getOptions().setUseInsecureSSL(false);
        wc.getOptions().setCssEnabled(false); //禁用css支持
        //js运行错误时，是否抛出异常
        wc.getOptions().setThrowExceptionOnScriptError(false);

        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        //是否允许使用ActiveX
        wc.getOptions().setActiveXNative(false);
        //等待js时间
        wc.waitForBackgroundJavaScript(600 * 1000);
        wc.setAjaxController(new NicelyResynchronizingAjaxController());
        wc.getOptions().setTimeout(1000000);
        wc.getOptions().setDoNotTrackEnabled(false);
        HtmlPage page = wc.getPage(url);
        String pageXml = page.asXml(); //以xml的形式获取响应文本
        Element html = Jsoup.parse(pageXml).body();
        Elements table = html.select("body").select("center")
                .select("table").select("tbody").select("tr").select("td")
                .select("div").select("textarea");
        String result = table.toString().replace("<textarea rows=\"10\" style=\"width:100%;font-size:75%;\">", "");
        result = result.replace("</textarea>", "Data From tenhou.net/2/");
        System.out.println(result);
        return result;
    }
}
