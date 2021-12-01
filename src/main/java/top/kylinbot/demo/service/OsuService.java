package top.kylinbot.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import top.kylinbot.demo.modle.osuUser;
import top.kylinbot.demo.util.MysqlUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Properties;

public class OsuService extends RestTemplate {
    public static osuUser client = new osuUser();
    private static String oauthId;
    private static String redirectUrl;
    private static String oauthToken;
    private static String URL;

    static {
        try {
            Properties props = new Properties();
            InputStream inputStream = MysqlUtil.class.getClassLoader().getResourceAsStream("KylinBot.properties");
            props.load(inputStream);
            oauthId = props.getProperty("oauthID");
            redirectUrl = props.getProperty("redirectUrl");
            oauthToken = props.getProperty("oauthToken");
            URL = props.getProperty("api");
//            System.out.println(password);
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
     * @return 返回得到的JSON以便下次处理
     */
    public osuUser getToken(osuUser user) {
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
        System.out.println(user);
        return user;
    }

    /***
     * 拿到机器人访客令牌
     * @return
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
        client.setAccessToken(s.getString("access_token"));
        client.setNextExpireTime(s.getLong("expires_in"));
        return client.getAccessToken();
    }

    /***
     * 刷新令牌
     * @param user 用户
     * @return 返回得到的JSON数据
     */
    public JSONObject refreshToken(osuUser user) {
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
        user.setAccessToken(s.getString("access_token"));
        user.setRefreshToken(s.getString("refresh_token"));
        user.setNextExpireTime(s.getLong("expires_in"));
        MysqlUtil.writeUser(user);
        return s;
    }

    public HttpHeaders setHttpHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        return headers;
    }

    /***
     * 使用用户refreshToken获取ID以及NickName
     * @param user
     * @return
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
        String url = this.URL + "me" + '/' + mode;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + user.getAccessToken(this));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<JSONObject> c = exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
        user.setOsuID(c.getBody().getString("username"));
//        user.setOsuName(c.getBody().getString("username"));
        return c.getBody();
    }

    /***
     * 使用client的refreshToken获取user信息
     * @param id
     * @return
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
        URI uri = UriComponentsBuilder.fromHttpUrl(this.URL + "users/" + id + '/' + mode)
                .queryParam("key", "id")
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + getToken());

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<JSONObject> c = exchange(uri, HttpMethod.GET, httpEntity, JSONObject.class);
        return c.getBody();
    }

    /***
     * 使用Client的refreshToken获取user信息
     * @param name
     * @return
     */
    public JSONObject getPlayerOsuInfo(String name) {
        return getPlayerInfo(name, "osu");
    }

    public JSONObject getPlayerInfo(String name, String mode) {
        URI uri = UriComponentsBuilder.fromHttpUrl(this.URL + "users/" + name + '/' + mode)
                .queryParam("key", "username")
                .build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + getToken());

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<JSONObject> c = exchange(uri, HttpMethod.GET, httpEntity, JSONObject.class);
        return c.getBody();
    }


}
