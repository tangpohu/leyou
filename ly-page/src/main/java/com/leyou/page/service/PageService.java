package com.leyou.page.service;

import com.leyou.item.pojo.*;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.leyou.page.client.SpecificationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

@Slf4j
@Service
public class PageService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specClient;

    @Autowired
    private TemplateEngine templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(PageService.class);


    /*public Map<String, Object> loadModel(Long spuId) {
        Map<String, Object> model=new HashMap<>();
        //查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //查询skus
        List<Sku> skus = spu.getSkus();
        //查询详情
        SpuDetail detail= spu.getSpuDetail();
        //查询brand
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        //查询商品分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询规格参数
        List<SpecGroup> specs = specClient.queryGroupByCid(spu.getCid3());

        model.put("title",spu.getTitle());
        model.put("subTitle",spu.getSubTitle());
        model.put("skus",skus);
        model.put("detail",detail);
        model.put("brand",brand);
        model.put("categories",categories);
        model.put("specs",specs);
        return model;
    }*/

    public Map<String, Object> loadModel(Long id) {
    try {
        // 模型数据
        Map<String, Object> modelMap = new HashMap<>();
        // 查询spu
        Spu spu = this.goodsClient.querySpuById(id);
        // 查询spuDetail
        SpuDetail detail = this.goodsClient.queryDetailById(id);
        // 查询sku
        List<Sku> skus = this.goodsClient.querySkuBySpuId(id);
        // 装填模型数
        modelMap.put("spu", spu);
        modelMap.put("spuDetail", detail);
        modelMap.put("skus", skus);
        // 准备商品分类
        List<Category> categories = getCategories(spu);
        if (categories != null) {
            modelMap.put("categories", categories);
        }
        // 准备品牌数据
        List<Brand> brands = this.brandClient.queryBrandByIds(
                Arrays.asList(spu.getBrandId()));
        modelMap.put("brand", brands.get(0));
        // 查询规格组及组内参数
        List<SpecGroup> groups = this.specClient.queryGroupByCid(spu.getCid3());
        modelMap.put("groups", groups);
        // 查询商品分类下的特有规格参数
        List<SpecGroup> specs = specClient.queryGroupByCid(spu.getCid3());
        // 处理成id:name格式的键值对
        Map<Long,String> paramMap = new HashMap<>();
        for (SpecGroup group : groups) {
            paramMap.put(group.getId(), group.getName());
        }
        modelMap.put("paramMap", paramMap);
        return modelMap;

    } catch (Exception e) {
        logger.error("加载商品数据出错,spuId:{}", id, e);
    }
        return null;
}
    private List<Category> getCategories(Spu spu) {
        try {
            List<Category> categories = this.categoryClient.queryCategoryByIds(
                    Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            return categories;
        } catch (Exception e) {
            logger.error("查询商品分类出错，spuId：{}", spu.getId(), e);
        }
        return null;
    }

    public void createHtml(Long spuId){
        //上下文
        Context context=new Context();
        context.setVariables(loadModel(spuId));
        //输出流
        File dest = new File("D://dtd-schema//leyou//upload", spuId + ".html");

        if (dest.exists()){
            dest.delete();
        }
        try ( PrintWriter writer = new PrintWriter(dest,"UTF-8")){
            //生产HTML
            templateEngine.process("item",context,writer);
        }catch (Exception e){
            log.error("[静态页服务]生成静态页异常",e);
        }

    }

    public void deleteHtml(Long spuId) {
        File dest = new File("D://dtd-schema//leyou//upload", spuId + ".html");
        if (dest.exists()){
            dest.delete();
        }
    }
}
