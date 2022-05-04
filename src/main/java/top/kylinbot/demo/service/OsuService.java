package top.kylinbot.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import love.forte.simbot.core.configuration.ComponentBeans;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.tillerino.osuApiModel.types.BitwiseMods;
import top.kylinbot.demo.modle.PPPlusObject;
import top.kylinbot.demo.modle.osuUser;
import top.kylinbot.demo.util.JsonUtil;
import top.kylinbot.demo.util.MysqlUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Properties;


@ComponentBeans
public class OsuService extends RestTemplate {
    public static osuUser client = new osuUser();
    private static String oauthId;
    private static String redirectUrl;
    private static String oauthToken;
    private static String URL;
    private static String tillerinoURL;
    public static String tillerinoKey;

    static {
        try {
            Properties props = new Properties();
            InputStream inputStream = MysqlUtil.class.getClassLoader().getResourceAsStream("main/resources/KylinBot.properties");
            props.load(inputStream);
            oauthId = props.getProperty("oauthID");
            redirectUrl = props.getProperty("redirectUrl");
            oauthToken = props.getProperty("oauthToken");
            URL = props.getProperty("api");
            tillerinoURL = props.getProperty("tillerinoURL");
            tillerinoKey = props.getProperty("TillerinoBotKey");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 拼合授权链接
     * @param state qq[+群号]
     */
    public String getOauthUrl(String state) {
        return UriComponentsBuilder.fromHttpUrl("https://osu.ppy.sh/oauth/authorize")
                .queryParam("client_id", oauthId)
                .queryParam("redirect_uri", redirectUrl)
                .queryParam("response_type", "code")
                .queryParam("scope", "friends.read identify public")
                .queryParam("state", state)
                .build().encode().toUriString();
    }

    /***
     * 初次得到授权令牌
     * @param user 传入user信息
     */
    public void getToken(osuUser user) {
        String url = "https://osu.ppy.sh/oauth/token";
        HttpHeaders headers = setHttpHeader();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("client_id", oauthId);
        body.add("client_secret", oauthToken);
        body.add("code", user.getRefreshToken());
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", redirectUrl);

        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        JSONObject s = postForObject(url, httpEntity, JSONObject.class);
        if (s != null) {
            user.setAccessToken(s.getString("access_token"));
            user.setRefreshToken(s.getString("refresh_token"));
            user.setNextExpireTime(s.getLong("expires_in"));
            user.setOsuID("unknown ID");
        }
    }

    /***
     * 拿到机器人访客令牌
     * @return token
     */
    public String getToken() {
        if (!client.isExpire()) {
            return client.getAccessToken();
        }
        String url = "https://osu.ppy.sh/oauth/token";
        HttpHeaders headers = setHttpHeader();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("client_id", oauthId);
        body.add("client_secret", oauthToken);
        body.add("grant_type", "client_credentials");
        body.add("scope", "public");

        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        JSONObject s = postForObject(url, httpEntity, JSONObject.class);
        assert s != null;
        client.setAccessToken(s.getString("access_token"));
        client.setNextExpireTime(s.getLong("expires_in"));

        return client.getAccessToken();
    }

    /***
     * 刷新令牌
     * @param user 用户
     */
    public void refreshToken(osuUser user) {
        String url = "https://osu.ppy.sh/oauth/token";
        HttpHeaders headers = setHttpHeader();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("client_id", oauthId);
        body.add("client_secret", oauthToken);
        body.add("refresh_token", user.getRefreshToken());
        body.add("grant_type", "refresh_token");
        body.add("redirect_uri", redirectUrl);

        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        JSONObject s = postForObject(url, httpEntity, JSONObject.class);
        assert s != null;
        user.setAccessToken(s.getString("access_token"));
        user.setRefreshToken(s.getString("refresh_token"));
        user.setNextExpireTime(s.getLong("expires_in"));
        MysqlUtil.writeUser(user);
    }

    public HttpHeaders setHttpHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        return headers;
    }


    /***
     * 使用用户refreshToken获取ID以及NickName
     * @param user 需要查询的用户
     * @return 各个模式的JSON信息
     */
    public JSONObject getPlayerOsuInfo(osuUser user) {
        return getPlayerInfo(user, "osu");
    }

    public JSONObject getPlayerTaikoInfo(osuUser user) {
        return getPlayerInfo(user, "taiko");
    }

    public JSONObject getPlayerCatchInfo(osuUser user) {
        return getPlayerInfo(user, "fruits");
    }

    public JSONObject getPlayerManiaInfo(osuUser user) {
        return getPlayerInfo(user, "mania");
    }

    public JSONObject getPlayerInfo(osuUser user, String mode) {
        String url = URL + "me" + '/' + mode;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + user.getAccessToken(this));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> c = exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
        JSONObject key = c.getBody();
        assert key != null;
        user.setOsuID(key.getString("username"));
        user.setId(key.getIntValue("id"));
        return c.getBody();
    }

    /***
     * 使用client的refreshToken获取user信息
     * @param id 用户ID数值
     * @return 返回用户信息
     */
    public JSONObject getPlayerOsuInfo(int id) {
        return getPlayerInfo(id, "osu");
    }

    public JSONObject getPlayerTaikoInfo(int id) {
        return getPlayerInfo(id, "taiko");
    }

    public JSONObject getPlayerCatchInfo(int id) {
        return getPlayerInfo(id, "fruits");
    }

    public JSONObject getPlayerManiaInfo(int id) {
        return getPlayerInfo(id, "mania");
    }

    public JSONObject getPlayerInfo(int id, String mode) {
        URI uri = UriComponentsBuilder.fromHttpUrl(URL + "users/" + id + '/' + mode)
                .queryParam("key", "id")
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + getToken());

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> c = exchange(uri, HttpMethod.GET, httpEntity, JSONObject.class);
        return c.getBody();
    }

    /***
     * 使用Client的refreshToken获取user信息
     * @param name osu用户名, 用于查询未绑定用户信息
     * @return 返回用户信息
     */
    public JSONObject getPlayerOsuInfo(String name) {
        return getPlayerInfo(name, "osu");
    }

    public JSONObject getPlayerInfo(String name, String mode) {
        URI uri = UriComponentsBuilder.fromHttpUrl(URL + "users/" + name + '/' + mode)
                .queryParam("key", "username")
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + getToken());

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> c;
        try {
            c = exchange(uri, HttpMethod.GET, httpEntity, JSONObject.class);
        } catch (HttpClientErrorException e) {
            // 默认使用osu id查询，查询失败换用id查询
            return getPlayerOsuInfo(Integer.parseInt(name));
        }
        return c.getBody();
    }


    /***
     * 获得score(最近游玩列表
     * @param user user
     * @param s s
     * @param e e
     * @return JSON数据
     */
    public JSONArray getOsuRecent(osuUser user, int s, int e) {
        return getRecent(user, "osu", s, e);
    }

    public JSONArray getOsuRecent(int id, int s, int e) {
        return getRecent(id, "osu", s, e);
    }

    public JSONArray getOsuAllRecent(osuUser user, int s, int e) {
        return getAllRecent(user, "osu", s, e);
    }

    public JSONArray getOsuAllRecent(int id, int s, int e) {
        return getAllRecent(id, "osu", s, e);
    }

    public JSONArray getRecent(osuUser user, String mode, int s, int e) {
        URI uri = UriComponentsBuilder.fromHttpUrl(URL + "users/" + user.getOsuID() + "/scores/recent")
                .queryParam("mode", mode)
                .queryParam("limit", e)
                .queryParam("offset", s)
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + user.getAccessToken(this));

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONArray> c = exchange(uri, HttpMethod.GET, httpEntity, JSONArray.class);
        return c.getBody();
    }

    /***
     * 包含fail的
     * @param user user
     * @param mode mode
     * @param s s
     * @param e e
     * @return JSON数据
     */
    public JSONArray getAllRecent(osuUser user, String mode, int s, int e) {
        URI uri = UriComponentsBuilder.fromHttpUrl(URL + "users/" + user.getOsuID() + "/scores/recent")
                .queryParam("mode", mode)
                .queryParam("include_fails", 1)
                .queryParam("limit", e)
                .queryParam("offset", s)
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + user.getAccessToken(this));

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONArray> c = exchange(uri, HttpMethod.GET, httpEntity, JSONArray.class);
        return c.getBody();
    }

    public JSONArray getRecent(int id, String mode, int s, int e) {
        URI uri = UriComponentsBuilder.fromHttpUrl(URL + "users/" + id + "/scores/recent")
                .queryParam("mode", mode)
                .queryParam("limit", e)
                .queryParam("offset", s)
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + getToken());

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONArray> c;
        try {
            c = exchange(uri, HttpMethod.GET, httpEntity, JSONArray.class);
        } catch (RestClientException restClientException) {
            restClientException.printStackTrace();
            return null;
        }
        return c.getBody();
    }

    public JSONArray getAllRecent(int id, String mode, int s, int e) {
        URI uri = UriComponentsBuilder.fromHttpUrl(URL + "users/" + id + "/scores/recent")
                .queryParam("mode", mode)
                .queryParam("include_fails", 1)
                .queryParam("limit", e)
                .queryParam("offset", s)
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + getToken());

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONArray> c = exchange(uri, HttpMethod.GET, httpEntity, JSONArray.class);
        return c.getBody();
    }

    public JSONObject getMapPerformancePoint(long beatmapid, @BitwiseMods long mods) {
        URI uri = UriComponentsBuilder.fromHttpUrl(tillerinoURL + "/beatmapinfo")
                .queryParam("beatmapid", beatmapid)
                .queryParam("mods", mods)
                .queryParam("wait", 1000)
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("api-key", tillerinoKey);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> c = exchange(uri, HttpMethod.GET, httpEntity, JSONObject.class);
        return c.getBody();
    }

    /***
     * 获得个人bp
     * @param user
     * @param s 开始坐标，最小为0
     * @param e 列表长度 1-100之间
     * @return
     */
    public JSONArray getOsuBestMap(osuUser user, int s, int e) {
        return getBestMap(user, "osu", s, e);
    }

    public JSONArray getTaikoBestMap(osuUser user, int s, int e) {
        return getBestMap(user, "taiko", s, e);
    }

    public JSONArray getCatchBestMap(osuUser user, int s, int e) {
        return getBestMap(user, "fruits", s, e);
    }

    public JSONArray getManiaBestMap(osuUser user, int s, int e) {
        return getBestMap(user, "mania", s, e);
    }

    public JSONArray getOsuBestMap(int id, int s, int e) {
        return getBestMap(id, "osu", s, e);
    }

    public JSONArray getTaikoBestMap(int id, int s, int e) {
        return getBestMap(id, "taiko", s, e);
    }

    public JSONArray getCatchBestMap(int id, int s, int e) {
        return getBestMap(id, "fruits", s, e);
    }

    public JSONArray getManiaBestMap(int id, int s, int e) {
        return getBestMap(id, "mania", s, e);
    }

    /***
     * 获得某个模式的bp表
     * @param user user
     * @param mode 模式
     * @param s 起始坐标
     * @param e 长度
     * @return
     */
    public JSONArray getBestMap(osuUser user, String mode, int s, int e) {
        URI uri = UriComponentsBuilder.fromHttpUrl(URL + "users/" + user.getOsuID() + "/scores/best")
                .queryParam("mode", mode)
                .queryParam("limit", e)
                .queryParam("offset", s)
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + user.getAccessToken(this));

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONArray> c = exchange(uri, HttpMethod.GET, httpEntity, JSONArray.class);
        return c.getBody();
    }

    public JSONArray getBestMap(int id, String mode, int s, int e) {
        URI uri = UriComponentsBuilder.fromHttpUrl(URL + "users/" + id + "/scores/best")
                .queryParam("mode", mode)
                .queryParam("limit", e)
                .queryParam("offset", s)
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + getToken());

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONArray> c = exchange(uri, HttpMethod.GET, httpEntity, JSONArray.class);
        return c.getBody();
    }

    /**
     * 获取用户特定谱面成绩
     *
     * @param bid  beatmap id
     * @param user user
     * @return 返回get到的数据
     */
    public JSONObject getScore(int bid, osuUser user) {
        URI uri = UriComponentsBuilder.fromHttpUrl(URL + "beatmaps/" + bid + "/scores/users/" + user.getOsuID())
                .queryParam("mode", "osu")
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + user.getAccessToken(this));

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> c = null;
        try {
            c = exchange(uri, HttpMethod.GET, httpEntity, JSONObject.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
        return c.getBody();
    }

    public JSONObject getScore(int bid, int uid) {
        URI uri = UriComponentsBuilder.fromHttpUrl(URL + "beatmaps/" + bid + "/scores/users/" + uid)
                .queryParam("mode", "osu")
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + getToken());

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> c = null;
        try {
            c = exchange(uri, HttpMethod.GET, httpEntity, JSONObject.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
        return c.getBody();
    }

    /***
     * PP+获取
     * @param name osuID
     * @return pp+数据
     */
    public PPPlusObject getppPlus(String name) {
        URI uri = UriComponentsBuilder.fromHttpUrl("https://syrin.me/pp+/api/user/" + name)
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<JsonNode> c = null;
        try {
            c = exchange(uri, HttpMethod.GET, httpEntity, JsonNode.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return JsonUtil.parseObject(c.getBody().get("user_data"), PPPlusObject.class);

//        System.out.println(c);
    }
}
