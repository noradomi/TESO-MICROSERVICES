package vn.hcmus.fit.truyenfull.data.utils;

import vn.hcmus.fit.truyenfull.data.model.Category;
import vn.hcmus.fit.truyenfull.data.model.Chapter;
import vn.hcmus.fit.truyenfull.data.model.Comic;
import vn.hcmus.fit.truyenfull.lib_data.tCategory;
import vn.hcmus.fit.truyenfull.lib_data.tChapter;
import vn.hcmus.fit.truyenfull.lib_data.tComic;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 11/18/2019.
 */
public class MappingDataUtils {
    public static Chapter thrift2db_Chapter(tChapter t) {
        Chapter db = new Chapter(t.getIndex(),
                t.getName(),t.getContent(),
                thrift2db_Comic(t.getComic()));
        return db;
    }

    public static List<Chapter> thrift2db_Chapter(List<tChapter> cs){
        List<Chapter> list = new ArrayList<>();
        for (tChapter c : cs) {
            list.add(thrift2db_Chapter(c));
        }
        return list;
    }

    public static Category thrift2db_Category(tCategory t) {
        //List<Comic> comicList = thrift2db_Comic(t.getComicList());
        Category r = new Category(t.getName(),t.getUrlname(),null);
        return r;
    }

    public static List<Category> thrift2db_Category(List<tCategory> cs){
        List<Category> list = new ArrayList<>();
        for (tCategory c : cs) {
            list.add(thrift2db_Category(c));
        }
        return list;
    }

    public static Comic thrift2db_Comic(tComic t) {
        List<Category> categoryList = thrift2db_Category(t.getCategoryList());
        List<Chapter> chapterList = new ArrayList<>();
        Comic comic = new Comic(t.getName(),t.getUrlname(),t.getAuthor(),t.getSource(),t.getStatus(),
                chapterList,
                categoryList
        );
        return comic;
    }

    public static List<Comic> thrift2db_Comic(List<tComic> ts) {
        List<Comic> list  = new ArrayList<>(ts.size());
        for (tComic t : ts) {
            list.add(thrift2db_Comic(t));
        }
        return list;
    }
}
