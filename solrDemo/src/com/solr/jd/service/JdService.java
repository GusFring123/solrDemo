package com.solr.jd.service;

import com.solr.jd.pojo.ProductModel;
import com.solr.jd.pojo.QueryVo;

import java.util.List;

public interface JdService {

    public List<ProductModel> getResultModelFromSolr(QueryVo queryVo) throws Exception;
}
