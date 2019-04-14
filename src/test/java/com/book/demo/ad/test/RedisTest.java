package com.book.demo.ad.test;

import com.book.demo.ad.model.*;
import com.book.demo.ad.redis.RedisUtil;
import com.book.demo.ad.util.JsonUtil;
import com.book.demo.ad.util.TranscoderUtils;
import com.book.demo.ad.util.UUIDUtil;
import org.junit.After;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import org.junit.Before;
import redis.clients.jedis.Pipeline;
import redis.clients.util.SafeEncoder;

import java.io.IOException;
import java.util.*;

public class RedisTest {

    private Jedis jedis;

    @Before
    public void initJedis() throws IOException {
        jedis = RedisUtil.initPool().getResource();
    }
    @Test(timeout = 1000)

    public void saveAdTest() {
        Pipeline pipeline = jedis.pipelined();
        pipeline.setex(SafeEncoder.encode("test"), 10 * 60,
                TranscoderUtils.encodeObject(this.initAdvertisement()));
        System.out.println(((Pipeline) pipeline).syncAndReturnAll());
    }

    @Test(timeout = 1000)
    public void queryAdTest() {
        Advertisement advertisement = (Advertisement) TranscoderUtils
                .decodeObject(jedis.get(SafeEncoder.encode("test")));
        System.out.println(JsonUtil.toJson(advertisement));
    }

    @Test(timeout = 1000)
    public void saveGoodsRecommendTest() {
        Pipeline pipeline = jedis.pipelined();
        pipeline.lpush("goods-recommend", this.initGoods());
        pipeline.expire("goods-recommend", 10 * 60);
        System.out.println(pipeline.syncAndReturnAll());
    }

    @Test(timeout = 1000)
    public void queryGoodsRecommendTest() {
        System.out.println(jedis.lrange("goods-recommend", 0, 10));
    }

    @Test
    public void saveCarTest() {
        Pipeline pipeline = jedis.pipelined();
        pipeline.hmset("car", this.initGoodDetail());
        pipeline.expire("car", 10 * 60);
        System.out.println(pipeline.syncAndReturnAll());
    }

    @Test
    public void queryCarTest() {
        System.out.println(jedis.hgetAll("car"));
    }

    @Test
    public void saveGoodLogTest() {
        Pipeline pipeline = jedis.pipelined();
        pipeline.sadd("goods-log", this.initGoodsLog());
        pipeline.expire("goods-log", 10 * 60);
        System.out.println(pipeline.syncAndReturnAll());
    }

    @Test
    public void queryGoodLogTest() {
        System.out.println(jedis.scard("goods-log"));
    }

    @Test
    public void saveSessionTest() {
        RedisSession session = new RedisSession();
        session.setAttribute("map", new HashMap<String, String>());
        session.setAttribute("list", new ArrayList<Object>());
        session.setAttribute("date", new Date());

        Pipeline pipeline = jedis.pipelined();
        // 采用管道形式提交命令,setex方法可设置redis中key的生命周期
        // SafeEncoder redis提供的解码器
        // TranscoderUtils 对java对象解压缩工具类
        pipeline.setex(SafeEncoder.encode("session"), 10 * 16,
                TranscoderUtils.encodeObject(session));
        System.out.println(pipeline.syncAndReturnAll());
    }

    @Test
    public void querySessionTest() {
        RedisSession session = (RedisSession) TranscoderUtils
                .decodeObject(jedis.get(SafeEncoder.encode("session")));
        System.out.println(JsonUtil.toJson(session));
    }

    @Test
    public void saveGoodsRecommendTest1() {
        Pipeline pipeline = jedis.pipelined();
        pipeline.lpush("goods-1", this.initGoods());
        pipeline.expire("goods-1", 10 * 60);
        System.out.println(pipeline.syncAndReturnAll());
    }

    @Test
    public void queryGoodsRecommendTest1() {
        System.out.println(jedis.lrange("goods-1", 0, 9));
        System.out.println(jedis.lrange("goods-1", 10, 19));
    }

    @After
    public void closeJedis() {
        jedis.close();
    }

    private Advertisement initAdvertisement() {
        Template template = new Template();
        template.setId(20);
        template.setName("轮播模版");
        template.setScript("alert('轮播')");

        AdContent adContent1 = new AdContent();
        adContent1.setId(1);
        adContent1.setName("新年图书忒大促.");
        adContent1.setSequence(1);
        adContent1.setUrl("https://book.jd.com/");
        adContent1.setImageUrl("http://book.image.com/test.jpg");

        AdContent adContent2 = new AdContent();
        adContent2.setId(2);
        adContent2.setName("手机专场，满1000返50.");
        adContent2.setSequence(2);
        adContent2.setUrl("https://shouji.jd.com/");
        adContent2.setImageUrl("http://book.image.com/test.jpg");

        List<AdContent> adContents = new ArrayList<AdContent>();
        adContents.add(adContent1);
        adContents.add(adContent2);

        Advertisement advertisement = new Advertisement();
        advertisement.setId(10001);
        advertisement.setPositionCode("home-01");
        advertisement.setTid(template.getId());
        advertisement.setAdContents(adContents);
        return advertisement;
    }

    private String[] initGoods() {
        Goods goods = new Goods();
        goods.setAdInfo("<html></html>");
        goods.setGoodsInfo("商品名称：华硕FX53VD商品编号：4380878商品毛重：4.19kg商品产地：中国大陆");
        goods.setSpecificationsInfo("主体系列飞行堡垒型号FX53VD颜色红黑平台Intel操作系统操作系统Windows 10家庭版处理器CPU类型Intel 第7代 酷睿CPU速度2.5GHz三级缓存6M其它说明I5-7300HQ芯片组芯片组其它　");
        String[] goodsArray = new String[20];
        for (int i = 0; i < 20; i++) {
            goods.setId(200000+i);
            goodsArray[i] = JsonUtil.toJson(goods);
        }
        return goodsArray;
    }

    private Map<String, String> initGoodDetail() {
        Map<String, String> map = new HashMap<String, String>();
        Goods goods1 = new Goods();
        goods1.setAdInfo("<html></html>");
        goods1.setGoodsInfo("商品名称：华硕FX53VD商品编号：4380878商品毛重：4.19kg商品产地：中国大陆");
        goods1.setId(4380877);
        goods1.setSpecificationsInfo("主体系列飞行堡垒型号FX53VD颜色红黑平台Intel操作系统操作系统Windows 10家庭版处理器CPU类型Intel 第7代 酷睿CPU速度2.5GHz三级缓存6M其它说明I5-7300HQ芯片组芯片组其它　");
        Goods goods2 = new Goods();
        goods2.setAdInfo("<html></html>");
        goods2.setGoodsInfo("商品名称：华硕FX53VD商品编号：4380878商品毛重：4.19kg商品产地：中国大陆");
        goods2.setId(4380878);
        goods2.setSpecificationsInfo("主体系列飞行堡垒型号FX53VD颜色红黑平台Intel操作系统操作系统Windows 10家庭版处理器CPU类型Intel 第7代 酷睿CPU速度2.5GHz三级缓存6M其它说明I5-7300HQ芯片组芯片组其它　");
        map.put(goods1.getId() + "", JsonUtil.toJson(goods1));
        map.put(goods2.getId() + "", JsonUtil.toJson(goods2));
        return map;
    }

    private String[] initGoodsLog() {
        GoodsLog goodsLog1 = new GoodsLog();
        goodsLog1.setClickDate(new Date());
        goodsLog1.setId(7768);
        goodsLog1.setIp("172.54.87.9");
        goodsLog1.setUuid(UUIDUtil.upperUUID());
        GoodsLog goodsLog2 = new GoodsLog();
        goodsLog2.setClickDate(new Date());
        goodsLog2.setId(7769);
        goodsLog2.setIp("172.54.87.9");
        goodsLog2.setUuid(UUIDUtil.upperUUID());
        String[] goodsLogs = new String[2];
        goodsLogs[0] = JsonUtil.toJson(goodsLog1);
        goodsLogs[1] = JsonUtil.toJson(goodsLog2);
        return goodsLogs;
    }
}
