package top.kylinbot.demo.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

public class osuService extends RestTemplate {

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
//     * @param binUser
     * @return
     */
    public JSONObject getToken() {
        String url = "https://osu.ppy.sh/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        MultiValueMap body = new LinkedMultiValueMap();
        body.add("client_id", oauthId);
        body.add("client_secret", oauthToken);
        String code =  "def502004bfa0a28c581aec5210699e5595b8a54f69376abef04561adf33957f0542a25c73ec8057ee0f6c245d9bb803b99f4536ce1aa363115eab485bb6f90184a5a3413611e862dd82a374153b36157825ba501b42e14c3223bfb5438911558a72d723e082bf4d18823ad2947e886f0ce77b2d5613a49b404d65ffed651a5c487a75920160b3d77f0fdb15e554123ff3b10a1b170e6527287efff46dfbb026fda32382f895ccbb67dc4be22f574da0fd41cf480be0ec3acd1595259dd8da06a92366fbcafb16e409346ca58cc97186a1fadcd68854f168e426d19d3be92162c26c10fa22aff9bfcfe658f0537123e7d4d0f556c9cde5b9b16330cc9d64526e3a3fd1a2d1f7945b9adf8a7b3fad6f3caee5b562fa0b2f07bbee80478a482267c282520f4be10d514bacf64197e66d4d33ac1abff37f65002533a4cdb61af47a65d333c26a4ab8cb792c3f57c421145fb78117634841bb0954902b9bed137ce6cfc4c8947c2a10d029d414a65dcb00b48f107cc4dbaa5480529b2ece511c173432";
        body.add("code", code);
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", redirectUrl);

        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        JSONObject s = postForObject(url, httpEntity, JSONObject.class);
//        binUser.setAccessToken(s.getString("access_token"));
//        binUser.setRefreshToken(s.getString("refresh_token"));
//        binUser.nextTime(s.getLong("expires_in"));
//        try {
//            BindingUtil.writeUser(binUser);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return s;
    }
}
