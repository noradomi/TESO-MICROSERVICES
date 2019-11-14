package vn.hcmus.fit.truyenfullapi.controller;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hcmus.fit.truyenfullapi.config.TruyenfullClientCrawler;
import vn.hcmus.fit.truyenfullapi.config.TruyenfullClientData;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Asus on 11/14/2019.
 */
@RestController
public class TruyenfullControllerAPI {
    @Autowired
    TruyenfullClientCrawler client_crawler;

    @Autowired
    TruyenfullClientData client_data;

    @GetMapping(value = "/crawler")
    public String crawlerComics(HttpServletRequest request) throws TException {
        int sl = Integer.parseInt(request.getParameter("sl"));
        if(client_crawler.crawler(sl)){
            return "CRAWLER THÀNH CÔNG "+sl +" TRUYỆN";
        }
        return "CRAWLER THẤT BẠI";
    }

    @GetMapping(value = "/findComicByName")
    public String findComicByName(HttpServletRequest request) throws TException {
        String name = request.getParameter("comicName");
        if(StringUtils.isEmpty(name))
            return "KHÔNG CÓ GÌ ĐỂ TÌM";
        else{
            return client_data.getAllChaptersOfComic(name);
        }
    }
}
