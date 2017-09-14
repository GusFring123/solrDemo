package com.solr.jd.dao;

import com.solr.jd.pojo.ProductModel;
import com.solr.jd.pojo.QueryVo;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class JdDaoImpl implements JdDao {
    @Autowired
    private SolrServer solrServer;

    @Override
    public List<ProductModel> getResultModelFromSolr(QueryVo queryVo) throws Exception {
        String queryString = queryVo.getQueryString();
        String catalog_name = queryVo.getCatalog_name();

        String price = queryVo.getPrice();
        String sort = queryVo.getSort();

        // 查询 关键词  过滤条件
        // 价格排序 分页 开始行 每页数 高亮 默认域 只查询指定域
        SolrQuery solrQuery = new SolrQuery();
        // 关键词
        solrQuery.setQuery(queryString);
        // 过滤条件
        if(null != catalog_name && !"".equals(catalog_name)){
            solrQuery.set("fq", "product_catalog_name:" + catalog_name);
        }
        if(null != price && !"".equals(price)){
            //0-9   50-*
            String[] p = price.split("-");
            solrQuery.set("fq", "product_price:[" + p[0] + " TO " + p[1] + "]");
        }
        // 价格排序
        if("1".equals(sort)){
            solrQuery.addSort("product_price", SolrQuery.ORDER.desc);
        }else{
            solrQuery.addSort("product_price", SolrQuery.ORDER.asc);
        }
        // 分页
        solrQuery.setStart(0);
        solrQuery.setRows(16);
        // 默认域
        solrQuery.set("df", "product_keywords");
        // 只查询指定域
        solrQuery.set("fl", "id,product_name,product_price,product_picture");
        // 高亮
        // 打开开关
        solrQuery.setHighlight(true);
        // 指定高亮域
        solrQuery.addHighlightField("product_name");
        // 前缀
        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        solrQuery.setHighlightSimplePost("</span>");

        // 后缀
        // 执行查询
        QueryResponse response = solrServer.query(solrQuery);
        // 文档结果集
        SolrDocumentList docs = response.getResults();

        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        // Map K id V Map
        // Map K 域名 V List
        // List list.get(0)
        // 总条数
        long numFound = docs.getNumFound();


        List<ProductModel> productModels = new ArrayList<ProductModel>();

        for (SolrDocument doc : docs) {
            ProductModel productModel = new ProductModel();

            productModel.setPid((String) doc.get("id"));
            productModel.setPrice((Float) doc.get("product_price"));
            productModel.setPicture((String) doc.get("product_picture"));
            Map<String, List<String>> map = highlighting.get((String) doc.get("id"));
            List<String> list = map.get("product_name");

            productModel.setName(list.get(0));
            productModels.add(productModel);
        }
        return productModels;

//        SolrQuery solrQuery = new SolrQuery();
//        //关键词
//        if (null != queryVo.getQueryString() && !"".equals(queryVo.getQueryString())) {
//            solrQuery.setQuery(queryVo.getQueryString());
//            solrQuery.set("df", "product_keywords");
//        }
//        //商品类型
//        if (null != queryVo.getCatalog_name() && !"".equals(queryVo.getCatalog_name().trim())) {
//            solrQuery.addFilterQuery("product_catalog_name:" + queryVo.getCatalog_name());
//        }
//        //价格
//        if (null != queryVo.getPrice() && !"".equals(queryVo.getPrice().trim())) {
//            String[] strs = queryVo.getPrice().trim().split("-");
//            if (strs.length == 2) {
//                solrQuery.addFilterQuery("product_price:[" + strs[0] + " TO " + strs[1] + "]");
//            } else {
//                solrQuery.addFilterQuery("product_price:[" + strs[0] + " TO *]");
//            }
//        }
//        //价格排序
//        if ("1".equals(queryVo.getSort())) {
//            solrQuery.setSort("product_price", SolrQuery.ORDER.desc);
//        } else {
//            solrQuery.setSort("product_price", SolrQuery.ORDER.asc);
//        }
//
//        //显示的域
//        solrQuery.setFields("id,product_name,product_price");
//
//        //1.设置高亮开关
//        solrQuery.setHighlight(true);
//        //2.需要高亮的域
//        solrQuery.addHighlightField("product_name");
//        //3.高亮的简单样式-前缀
//        solrQuery.setHighlightSimplePre(("<span style='color:red'>"));
//        //3.高亮的简单样式-后缀
//        solrQuery.setHighlightSimplePost("</span>");
//
//        QueryResponse queryResponse = solrServer.query(solrQuery);
//
//        //取出高亮
//        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
//
//        //取结果集
//        SolrDocumentList docs = queryResponse.getResults();
//        //打印总条数
//        System.out.println(docs.getNumFound());
//
//        //结果集
//        List<ProductModel> productModels = new ArrayList<>();
//
//        //遍历
//        for (SolrDocument document : docs) {
//            ProductModel productModel = new ProductModel();
//            //ID
//            String id = (String) document.get("id");
//            //价格
//            Float price = (Float) document.get("product_price");
//            //商品图片
//            String picture = (String) document.get("product_picture");
//            //商品类型
//            String catalog = (String) document.get("product_catalog_name");
//
//            //
//            Map<String, List<String>> map = highlighting.get("id");
//            List<String> list = map.get("product_name");
//
//            //判断高亮必须不为空
//            if (null != list && list.size() > 0){
//                String name = list.get(0);
//                productModel.setName(name);
//            }
//
//            productModel.setPid(id);
//            productModel.setCatalog_name(catalog);
//            productModel.setPicture(picture);
//            productModel.setPrice(price);
//
//            productModels.add(productModel);
//
//        }
//
//
//        return productModels;
    }
}
