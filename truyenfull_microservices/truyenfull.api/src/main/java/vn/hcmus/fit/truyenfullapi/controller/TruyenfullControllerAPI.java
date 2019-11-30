package vn.hcmus.fit.truyenfullapi.controller;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.hcmus.fit.truyenfull.lib_data.tComic;
import vn.hcmus.fit.truyenfull.lib_data.utils.MappingUtils;
import vn.hcmus.fit.truyenfullapi.config.TruyenfullClientCrawler;
import vn.hcmus.fit.truyenfullapi.config.TruyenfullClientData;
import vn.hcmus.fit.truyenfullapi.model.Comic;
import vn.hcmus.fit.truyenfullapi.utils.MappingAPIUtils;

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

//    Crawl dữ liệu truyện từ truyenfull.vn
    @GetMapping(value = "comics/crawl",produces = "application/json")
    public String crawlerComics(@RequestParam(value = "sl",defaultValue = "1") int sl,
                                @RequestParam(value = "url",defaultValue = "truyen-hot") String url) throws TException {
//        sl: số lượng truyện muốn crawl
//        url -> vd: truyen-hot,ngon-tinh,....
        return client_crawler.crawler(sl,url);
    }

//    Lấy danh sách toàn bộ truyện (phân trang và sắp xếp)
    @GetMapping(value = "comics",produces = "application/json")
    public  String GETAllComic(
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int maxLength,
            @RequestParam(value = "sort_by") String sortBy,
            @RequestParam(value = "order_by") String orderBy
    ) throws TException {
        return client_data.getAllComic(page,maxLength,sortBy,orderBy);
    }

//    Lấy thông tin 1 truyện bằng id
    @GetMapping(value = "comics/{id}",produces = "application/json")
    public String GETAComic(@PathVariable(value = "id") long id) throws TException {

        return client_data.getAComic(id);
    }

//    Thêm mới 1 truyện
    @PostMapping(value = "comics",produces = "application/json")
    public String ADDComic(@Valid @RequestBody Comic newComic) throws TException {
        tComic t = MappingAPIUtils.db2thrift_Comic(newComic);
        return client_data.addComic(t);
    }

//    Update 1 truyện
    @PutMapping(value = "comics/{id}",produces = "application/json")
    public  String UPDATEComic(@PathVariable(value = "id") Long comicId,
                               @Valid @RequestBody Comic comicDetails ) throws TException {
        return client_data.updateComic(comicId, MappingUtils.convertObject(comicDetails,tComic.class));
    }



    @DeleteMapping(value = "/comics/{id}",produces = "application/json")
    public String DELETEComic(@PathVariable(value = "id") long comicId) throws TException {
        return client_data.deleteComic(comicId);
    }


//    Lấy thông tin các chapter của truyện (phân trang và sắp xếp)
    @GetMapping(value = "/comics/{id}/chapters",produces = "application/json")
    public String getChaptersOfComic(@PathVariable(value = "id") long id,
                                     @RequestParam(value = "page",defaultValue = "1") int page,
                                     @RequestParam(value = "limit", defaultValue = "10") int maxLength,
                                     @RequestParam(value = "sort_by") String sortBy,
                                     @RequestParam(value = "order_by") String orderBy) throws TException {
        return client_data.getChaptersOfComic(id,page,maxLength,sortBy,orderBy);

    }

//  Lấy các truyện có rating cao nhất (phân trang và sắp xếp)
    @GetMapping(value = "/comics/top_rated",produces = "application/json")
    public String getTopRatedComics() throws TException {
        return client_data.getTopRatedComics();
    }

//    Lấy các truyện theo tác giả truyện (phân trang và sắp xếp)
    @GetMapping(value = "/comics/author",produces = "application/json")
    public String getComicsByAuthor(@RequestParam(value = "authName",defaultValue = "") String authName,
                                    @RequestParam(value = "page",defaultValue = "1") int page,
                                    @RequestParam(value = "limit",defaultValue = "10") int maxLength,
                                    @RequestParam(value = "sort_by") String sortBy,
                                    @RequestParam(value = "order_by") String orderBy) throws TException {
        return client_data.getComicsByAuthor(authName,page,maxLength,sortBy,orderBy);
    }

//    Lấy các truyện theo thể loại (id) (phân trang và sắp xếp)
    @GetMapping(value = "/category/{idCat}/comics",produces = "application/json")
    public String getComicsByCategory(@PathVariable(value = "idCat") long id,
                                      @RequestParam(value = "page",defaultValue = "1") int page,
                                      @RequestParam(value = "maxLength",defaultValue = "10") int maxLength,
                                      @RequestParam(value = "sort_by") String sortBy,
                                      @RequestParam(value = "order_by") String orderBy) throws TException {
        return client_data.getComicsByCategory(id,page,maxLength,sortBy,orderBy);
    }

//    Lấy các truyện đã HOÀN THÀNH (phân trang và sắp xếp)
    @GetMapping(value = "/comics/finish",produces = "application/json")
    public String getFinishedComics(@RequestParam(value = "page",defaultValue = "1") int page,
                                    @RequestParam(value = "maxLength",defaultValue = "10") int maxLength,
                                    @RequestParam(value = "sort_by") String sortBy,
                                    @RequestParam(value = "order_by") String orderBy) throws TException {
        return client_data.getFinishedComics(page,maxLength,sortBy,orderBy);
    }

//    Lấy các truyện MỚI NHẤT (phân trang và sắp xếp)
    @GetMapping(value = "comics/latest",produces = "application/json")
    public String getLatestComic(@RequestParam(value = "page",defaultValue = "1") int page,
                                 @RequestParam(value = "maxLength",defaultValue = "10") int maxLength,
                                 @RequestParam(value = "sort_by") String sortBy,
                                 @RequestParam(value = "order_by") String orderBy) throws TException {
        return client_data.getLatestComic(page,maxLength,sortBy,orderBy);
    }

//    Lấy các truyện HOT NHẤT (phân trang và sắp xếp)
    @GetMapping(value = "comics/hot",produces = "application/json")
    public String getHotComics(@RequestParam(value = "page",defaultValue = "1") int page,
                               @RequestParam(value = "maxLength",defaultValue = "10") int maxLength,
                               @RequestParam(value = "sort_by") String sortBy,
                               @RequestParam(value = "order_by") String orderBy) throws TException {
        return client_data.getHotComics(page,maxLength,sortBy,orderBy);
    }

//    Lấy thông tin thể loại của truyện
    @GetMapping(value = "comics/{id}/category",produces = "application/json")
    public  String getCategoriesOfComic(@PathVariable(value = "id") long id) throws TException {
        return client_data.getCategoriesOfComic(id);
    }

//    Tìm kiếm 1 truyện theo các fields
    @GetMapping(value = "comics/search",produces = "application/json")
    public String searchComic(@RequestParam(value = "query",defaultValue = "") String query,
                              @RequestParam(value = "page",defaultValue = "1") int page,
                              @RequestParam(value = "limit",defaultValue = "10") int maxLength,
                              @RequestParam(value = "sort_by",required = false) String sortBy,
                              @RequestParam(value = "order_by",required = false) String orderBy) throws TException {
        return client_data.searchComic(query,page,maxLength,sortBy,orderBy);
    }
}
