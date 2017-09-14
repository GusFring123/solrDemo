package com.solr.jd.controller;

import com.solr.jd.pojo.ProductModel;
import com.solr.jd.pojo.QueryVo;
import com.solr.jd.service.JdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class JdController {

    @Autowired
    private JdService jdService;

    @RequestMapping(value = "list")
    public String list(QueryVo queryVo, Model model) throws Exception {

        List<ProductModel> productModelList = jdService.getResultModelFromSolr(queryVo);

        model.addAttribute("productModelList", productModelList);

        //回显
        model.addAttribute("catalog_name", queryVo.getCatalog_name());
        model.addAttribute("price", queryVo.getPrice());
        model.addAttribute("sort", queryVo.getSort());
        model.addAttribute("queryString", queryVo.getQueryString());

        return "product_list";
    }
}
