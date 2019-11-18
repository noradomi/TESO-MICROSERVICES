package vn.hcmus.fit.truyenfull.data.controller;

import org.apache.thrift.TException;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import vn.hcmus.fit.truyenfull.data.model.Category;
import vn.hcmus.fit.truyenfull.data.model.Comic;
import vn.hcmus.fit.truyenfull.data.repository.CategoryRepository;
import vn.hcmus.fit.truyenfull.data.repository.ComicRepositiory;
import vn.hcmus.fit.truyenfull.data.utils.MappingUtils;
import vn.hcmus.fit.truyenfull.data.utils.ReponseUtils;
import vn.hcmus.fit.truyenfull.lib_data.TruyenFullDataService;
import vn.hcmus.fit.truyenfull.lib_data.tComic;

import java.util.List;
import java.util.Optional;


/**
 * Created by Asus on 11/14/2019.
 */
@Controller
public class DataController implements TruyenFullDataService.Iface {
    @Autowired
    ComicRepositiory comicRepositiory;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public String getAllComic() throws TException {
        List<Comic> comics= comicRepositiory.findAll();
        String response = ReponseUtils.returnListComic(comics).toString();
        return response;
    }

    @Override
    public String getComicByName(String name) throws TException {
        Comic comic = comicRepositiory.findByUrlname(name);
        if(comic == null)
            return "TÊN COMIC KHÔNG TỒN TẠI ^<>^.";
        else{
            String response = ReponseUtils.returnComic(comic).toString();
            return response;
        }
    }

    @Override
    public String updateComic(long id, tComic comic) throws TException {
        System.out.println("Toi day");
        Comic updatedComic = MappingUtils.thrift2db_Comic(comic);
        Comic oldComic = comicRepositiory.findById(new Long(id)).get();

        if(!StringUtils.isEmpty(updatedComic.getName()))
            oldComic.setName(updatedComic.getName());

        if(!StringUtils.isEmpty(updatedComic.getAuthor()))
            oldComic.setAuthor(updatedComic.getAuthor());

        if(!StringUtils.isEmpty(updatedComic.getSource()));
            oldComic.setSource(updatedComic.getSource());

        if(!StringUtils.isEmpty(updatedComic.getStatus()))
            oldComic.setStatus(updatedComic.getStatus());

        comicRepositiory.save(oldComic);
        return ReponseUtils.returnComic(oldComic).toString();
    }

    @Override
    public String addComic(tComic comic) throws TException {
        Comic comic1 = MappingUtils.thrift2db_Comic(comic);
        comicRepositiory.save(comic1);
        return ReponseUtils.returnComic(comic1).toString();
    }


    @Override
    public boolean deleteComic(long id) throws TException {
        Comic comic = comicRepositiory.findById(new Long(id)).get();
        if(comic != null){
            comicRepositiory.delete(comic);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String getComicsByCategory(long catId) throws TException {
        Optional<Category> category = categoryRepository.findById(new Long(catId));
        String response = ReponseUtils.returnCategory(category.get()).toString();
        return response;
    }
}
