package com.nyx.bot.core;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.nyx.bot.res.ArbitrationPre;
import com.nyx.bot.res.GlobalStates;
import com.nyx.bot.utils.DateUtils;
import com.nyx.bot.utils.http.HttpUtils;
import okhttp3.Headers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ApiUrl {


    /**
     * 中文
     */
    public static final Headers LANGUAGE_ZH_HANS = Headers.of("language", "zh-hans");

    public static final String WARFRAME_SOCKET = "ws://api.warframestat.us/socket";

    public static final String WARFRAME_STATUS = "https://api.warframestat.us/";

    /**
     * 战甲数据源 git push pull等操作链接
     */
    public static final List<String> DATA_SOURCE_GIT = List.of(
            "https://github.com/KingPrimes/DataSource",
            "https://gitcode.com/KingPrimes/DataSource",
            "https://gitlab.com/KingPrimes/DataSource",
            "https://gitee.com/KingPrime/DataSource"
    );


    /**
     * 国内 GIT HUB　加速
     */
    public static final List<String> GIT_HUB_SPEED = List.of(
            "https://gh.api.99988866.xyz/",
            "https://ghproxy.github.io/",
            "https://gh.jasonzeng.dev/",
            "https://github.moeyy.xyz/",
            "https://github.geekery.cn/"
    );


    /**
     * 赤毒幻纹
     */
    public static final String WARFRAME_MARKET_LICH_EPHEMERAS = "https://api.warframe.market/v1/lich/ephemeras";

    /**
     * 信条幻纹
     */
    public static final String WARFRAME_MARKET_SISTER_EPHEMERAS = "https://api.warframe.market/v1/sister/ephemeras";

    /**
     * Market 物品
     */
    public static final String WARFRAME_MARKET_ITEMS = "https://api.warframe.market/v1/items";

    /**
     * Market 紫卡武器
     */
    public static final String WARFRAME_MARKET_RIVEN_ITEMS = "https://api.warframe.market/v1/riven/items";

    /**
     * 赤毒武器
     */
    public static final String WARFRAME_MARKET_LICH_WEAPONS = "https://api.warframe.market/v1/lich/weapons";

    /**
     * 信条武器
     */
    public static final String WARFRAME_MARKET_SISTER_WEAPONS = "https://api.warframe.market/v1/sister/weapons";

    public static final String WARFRAME_ARBITRATION = "https://wf.555590.xyz/api/arbys";


    /**
     * Market 物品查询
     *
     * @param key  物品名称
     * @param from 平台
     * @return 返回信息
     */
    public static HttpUtils.Body marketOrders(String key, String from) {
        String url = "https://api.warframe.market/v1/items/" + key + "/orders?include=item";
        return HttpUtils.sendGet(url, Headers.of("platform", from));
    }

    /**
     * 获取仲裁信息
     *
     * @return 仲裁
     */
    public static GlobalStates.Arbitration arbitrationPre() {
        List<ArbitrationPre> arbitrationPres = JSON.parseArray(HttpUtils.sendGet(WARFRAME_ARBITRATION).getBody(), ArbitrationPre.class, JSONReader.Feature.SupportSmartMatch);
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(8));
        String format = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00"));
        ArbitrationPre arbitrationPre = arbitrationPres.stream()
                .filter(i -> DateUtils.format(i.getActivation(), "yyyy-MM-dd HH:mm:ss")
                        .equalsIgnoreCase(format))
                .findFirst().orElse(null);
        if (arbitrationPre == null) return null;
        GlobalStates.Arbitration arbitration = new GlobalStates.Arbitration();
        arbitration.setActivation(arbitrationPre.getActivation());
        arbitration.setExpiry(arbitrationPre.getExpiry());
        arbitration.setNode(arbitrationPre.getNode());
        arbitration.setType(arbitrationPre.getType());
        arbitration.setEnemy(arbitrationPre.getEnemy());
        return arbitration;
    }

}
