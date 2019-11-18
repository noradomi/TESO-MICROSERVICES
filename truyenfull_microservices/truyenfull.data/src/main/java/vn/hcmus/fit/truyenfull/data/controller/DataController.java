package vn.hcmus.fit.truyenfull.data.controller;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import vn.hcmus.fit.truyenfull.data.model.Category;
import vn.hcmus.fit.truyenfull.data.model.Comic;
import vn.hcmus.fit.truyenfull.data.repository.CategoryRepository;
import vn.hcmus.fit.truyenfull.data.repository.ComicRepositiory;
import vn.hcmus.fit.truyenfull.data.utils.MappingDataUtils;
import vn.hcmus.fit.truyenfull.data.utils.ReponseUtils;
import vn.hcmus.fit.truyenfull.lib_data.TruyenFullDataService;
import vn.hcmus.fit.truyenfull.lib_data.tComic;
import vn.hcmus.fit.truyenfull.lib_data.utils.MappingUtils;

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
        try {
            List<Comic> comics= comicRepositiory.findAll();
            return ReponseUtils.success(ReponseUtils.returnListComic(comics));
        } catch (Exception e) {
            e.printStackTrace();
            return ReponseUtils.serverError();
        }
    }

    @Override
    public String getComicByName(String name) throws TException {
        try {
            Comic comic = comicRepositiory.findByUrlname(name);
            if(comic == null)
                return ReponseUtils.NotFound();
            else{
                return ReponseUtils.success(ReponseUtils.returnComic(comic));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReponseUtils.serverError();
        }
    }

    @Override
    public String updateComic(long id, tComic comic) throws TException {
        try {
            Comic updatedComic = MappingUtils.convertObject(comic,Comic.class);
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
            return ReponseUtils.success(ReponseUtils.returnComic(oldComic));
        } catch (Exception e) {
            e.printStackTrace();
            return ReponseUtils.serverError();
        }
    }

    @Override
    public String addComic(tComic tcomic) throws TException {
//        Comic comic1 = MappingUtils.thrift2db_Comic(comic);
        try {
//            System.out.println(tcomic);
            if(StringUtils.isEmpty(tcomic.getName()) || StringUtils.isEmpty(tcomic.getUrlname()))
                return ReponseUtils.inValid();

            Comic comic = MappingDataUtils.thrift2db_Comic(tcomic);

            comicRepositiory.save(comic);

            return ReponseUtils.success(ReponseUtils.returnComic(comic));
        } catch (Exception e) {
            e.printStackTrace();
            return ReponseUtils.serverError();
        }

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
