package vn.hcmus.fit.truyenfull.data.controller;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import vn.hcmus.fit.truyenfull.data.model.Comic;
import vn.hcmus.fit.truyenfull.data.repository.ComicRepositiory;
import vn.hcmus.fit.truyenfull.data.utils.ReponseUtils;
import vn.hcmus.fit.truyenfull.lib_data.TruyenFullDataService;


/**
 * Created by Asus on 11/14/2019.
 */
@Controller
public class DataController implements TruyenFullDataService.Iface {
    @Autowired
    ComicRepositiory comicRepositiory;
    @Override
    public String getAllChaptersOfComic(String name) {
//        Tìm kiếm comic có tên tương ứng
//        return "Goi dc Data service";
        Comic comic = comicRepositiory.findByUrlname(name);
        if(comic == null)
            return "TÊN COMIC KHÔNG TỒN TẠI ^<>^.";
        else{
            String response = ReponseUtils.returnComic(comic).toString();
            return response;
        }
    }
}
