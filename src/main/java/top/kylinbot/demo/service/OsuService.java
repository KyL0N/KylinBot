package top.kylinbot.demo.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import top.kylinbot.demo.modle.osuUser;

import java.util.Collections;

public class OsuService extends RestTemplate {

    private int oauthId = 11065;
    private String redirectUrl = "http://kyl1n.top:8888";
    private String oauthToken = "8AqhvAXdFarhQUUmJYkx7tYAk23Z1IB54AJi2Adm";
    private String URL = "https://osu.ppy.sh/api/v2/";

//    @Autowired
//    RestTemplate template;

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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        MultiValueMap<String,Object> body = new LinkedMultiValueMap();
        body.add("client_id", oauthId);
        body.add("client_secret", oauthToken);
        body.add("code", user.getCode());
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", redirectUrl);

        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        JSONObject s = postForObject(url, httpEntity, JSONObject.class);

        if (s != null) {
            user.setAccessToken(s.getString("access_token"));
            user.setRefreshToken(s.getString("refresh_token"));
            user.setExpire(s.getLong("expires_in"));
            user.setOsuID("unknown ID");
        }


        System.out.println(user.toString());
//        System.out.println(query);
        return user;
    }


}
