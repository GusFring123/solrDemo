package com.solr.jd.service;

import com.solr.jd.dao.JdDao;
import com.solr.jd.pojo.ProductModel;
import com.solr.jd.pojo.QueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JdServiceImpl implements JdService {

    @Autowired
    private JdDao jdDao;

    @Override
    public List<ProductModel> getResultModelFromSolr(QueryVo queryVo) throws Exception {
        return jdDao.getResultModelFromSolr(queryVo);
    }
}
