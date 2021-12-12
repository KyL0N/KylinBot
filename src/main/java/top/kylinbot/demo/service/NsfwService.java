package top.kylinbot.demo.service;

import catcode.CatCodeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static top.kylinbot.demo.listener.NsfwPhotoListener.getR18;


public class NsfwService {
    private static final String iw233 = "https://iw233.cn/API/Random.php";
    private static final String toubiec = "https://acg.toubiec.cn/random.php?ret=json";//    original
//    regular
//    small
//    thumb
//    mini
    private final String size = "regular";

    public String getCatCodeFromApi(Boolean isPri) throws IOException {
        HttpClient client = HttpClients.createDefault();
//        String url = "https://api.lolicon.app/setu/v2?size=regular";
        String url = "https://api.lolicon.app/setu/v2?size=" + size;
        if (isPri) {
            if (getR18() == 1) {
                url = url + "&r18=1";
            }
        }
        url = url + "&r18=0";
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String string = EntityUtils.toString(entity);
        String urls = parseJson(string);
//        String urlEncoded = CatEncoder.getInstance().encodeParams(urls);
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        if (isPri) {
            if (getR18() == 1) {
                return "由于腾讯风险控制, 链接请自行查看: \n" + urls;
            }
        }

        return util.getStringTemplate().image(urls);
    }


    public String getPic(Boolean isPri) throws IOException {
        HttpClient client = HttpClients.createDefault();
        String url = toubiec;
        if (isPri) {
            if (getR18() == 1) {
                url = url + "&r18=1";
            }
        }
        url = url + "&r18=0";
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String string = EntityUtils.toString(entity);
        String urls = parseToubiecJson(string);
//        String urlEncoded = CatEncoder.getInstance().encodeParams(urls);
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        if (isPri) {
            if (getR18() == 1) {
                return "由于腾讯风险控制, 链接请自行查看: \n" + urls;
            }
        }
        return util.getStringTemplate().image(urls);
    }

    /**
     * @param string 传入get到的Json信息
     * @return 返回解析到的图片url信息
     */
    public String parseToubiecJson(String string) {
        JSONArray data = JSON.parseArray(string);
//        JSONArray data = jsonObject.getJSONArray(0);
//        String r18 = data.getJSONObject(0).getString("r18");
//        String uid = data.getJSONObject(0).getString("uid");
        String url = data.getJSONObject(0).getString("imgurl");

        System.out.println("url:" + url);
        return url;
    }

    /**
     * @param string 传入get到的Json信息
     * @return 返回解析到的图片url信息
     */
    public String parseMirlKoiJson(String string) {
        JSONObject data = JSON.parseObject(string);
//        JSONArray data = jsonObject.getJSONArray(0);
//        String r18 = data.getJSONObject(0).getString("r18");
//        String uid = data.getJSONObject(0).getString("uid");
        String url = data.getString("pic");
        System.out.println("url:" + url);
        return url;
    }



    public String parseJson(String string) {
        JSONObject jsonObject = JSON.parseObject(string);
        JSONArray data = jsonObject.getJSONArray("data");
        String r18 = data.getJSONObject(0).getString("r18");
        String uid = data.getJSONObject(0).getString("uid");
        String url = data.getJSONObject(0).getJSONObject("urls").getString(size);
//        String pid = data.getJSONObject(0).getString("pid");
//        String title = data.getJSONObject(0).getString("title");
//        String author = data.getJSONObject(0).getString("author");
//        String width = data.getJSONObject(0).getString("width");
//        String height = data.getJSONObject(0).getString("height");
//        String ext = data.getJSONObject(0).getString("ext");
//        String uploadDate = data.getJSONObject(0).getString("uploadDate");
//        String url = urls.getString("original");
        //TODO:TAGS
        System.out.println("uid:" + uid);
        System.out.println("url:" + url);
        System.out.println("r18:" + r18);
        //i.pixiv.cat现以被墙, i.pixiv.re可正常使用
        return url.replace(".cat", ".re");
    }


}
