package vn.hcmus.fit.truyenfull.data.utils;

import vn.hcmus.fit.truyenfull.data.model.Chapter;
import vn.hcmus.fit.truyenfull.data.model.Comic;
import vn.hcmus.fit.truyenfull.lib_data.tChapter;
import vn.hcmus.fit.truyenfull.lib_data.tComic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 11/15/2019.
 */
public class MappingUtils {

//    public static Comic thrift2db_Chapter(tComic db) {
//
//    }
//
//    public static Comic thrift2db_Category(tComic db) {
//
//    }

    public static Comic thrift2db_Comic(tComic t) {
        Comic comic = new Comic(t.getName(),t.getUrlname(),t.getAuthor(),t.getSource(),t.getStatus());
        return comic;
    }
}
