package vn.hcmus.fit.truyenfullapi.controller;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vn.hcmus.fit.truyenfull.lib_data.tComic;
import vn.hcmus.fit.truyenfull.lib_data.utils.MappingUtils;
import vn.hcmus.fit.truyenfullapi.config.TruyenfullClientCrawler;
import vn.hcmus.fit.truyenfullapi.config.TruyenfullClientData;
import vn.hcmus.fit.truyenfullapi.model.Comic;
import vn.hcmus.fit.truyenfullapi.utils.MappingAPIUtils;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

    @GetMapping(value = "/getAllComic",produces = "application/json")
    public  String GETAllComic() throws TException {
        return client_data.getAllComic();
    }

    @GetMapping(value = "/getComicByName",produces = "application/json")
    public String GETComicByName(HttpServletRequest request) throws TException {
        String name = request.getParameter("comicName");
        if(StringUtils.isEmpty(name))
            return "KHÔNG CÓ GÌ ĐỂ TÌM";
        else{
            return client_data.getComicByName(name);
        }
    }

    @PutMapping(value = "/updateComic/{id}",produces = "application/json")
    public  String UPDATEComic(@PathVariable(value = "id") Long comicId,
                               @Valid @RequestBody Comic comicDetails ) throws TException {
        return client_data.updateComic(comicId, MappingUtils.convertObject(comicDetails,tComic.class));
    }

    @PostMapping(value = "/addComic",produces = "application/json")
    public String ADDComic(@Valid @RequestBody Comic newComic) throws TException {
        tComic t = MappingAPIUtils.db2thrift_Comic(newComic);
        System.out.println("is Here");
        return client_data.addComic(t);
    }
}
