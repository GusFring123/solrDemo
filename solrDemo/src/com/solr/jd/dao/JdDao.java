package com.solr.jd.dao;

import com.solr.jd.pojo.ProductModel;
import com.solr.jd.pojo.QueryVo;

import java.util.List;

public interface JdDao {
    List<ProductModel> getResultModelFromSolr(QueryVo queryVo) throws Exception;
}
