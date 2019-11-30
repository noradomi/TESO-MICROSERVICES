package vn.hcmus.fit.truyenfullapi.utils;

import vn.hcmus.fit.truyenfull.lib_data.tCategory;
import vn.hcmus.fit.truyenfull.lib_data.tChapter;
import vn.hcmus.fit.truyenfull.lib_data.tComic;
import vn.hcmus.fit.truyenfullapi.model.Category;
import vn.hcmus.fit.truyenfullapi.model.Chapter;
import vn.hcmus.fit.truyenfullapi.model.Comic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 11/18/2019.
 */
public class MappingAPIUtils {
    public static tChapter db2thrift_Chapter(Chapter db) {
        tChapter t = new tChapter(db.getId().longValue(),db.getIndex(),
                db.getName(),db.getContent(),
                db2thrift_Comic(db.getComic()));
        return t;
    }

    public static List<tChapter> db2thrift_Chapter(List<Chapter> db){
        List<tChapter> list = new ArrayList<>();
        for (Chapter c : db) {
            list.add(db2thrift_Chapter(c));
        }
        return list;
    }

    public static tCategory db2thrift_Category(Category db) {
//        List<tComic> tComicList = db2thrift_Comic(db.getComicList());
        tCategory t = new tCategory(db.getId(),db.getName(),db.getUrlname(),null);
        return t;
    }

    public static List<tCategory> db2thrift_Category(List<Category> db){
        List<tCategory> list = new ArrayList<>();
        for (Category c : db) {
            list.add(db2thrift_Category(c));
        }
        return list;
    }

    public static tComic db2thrift_Comic(Comic db) {
        List<tCategory> tCategoryList = db2thrift_Category(db.getCategoryList());
        List<tChapter> tChapterList = new ArrayList<>();
        tComic t = new tComic(
                db.getName(),db.getUrlname(),
                db.getAuthor(),db.getSource(),db.getStatus(),
                tChapterList,
                tCategoryList);
        return t;
    }

    public static List<tComic> db2thrift_Comic(List<Comic> ts) {
        List<tComic> list  = new ArrayList<>(ts.size());
        for (Comic t : ts) {
            list.add(db2thrift_Comic(t));
        }
        return list;
    }
}
