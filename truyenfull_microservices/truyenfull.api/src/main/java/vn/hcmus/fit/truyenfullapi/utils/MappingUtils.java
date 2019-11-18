package vn.hcmus.fit.truyenfullapi.utils;

import vn.hcmus.fit.truyenfull.lib_data.tComic;
import vn.hcmus.fit.truyenfullapi.model.Comic;

/**
 * Created by Asus on 11/15/2019.
 */
public class MappingUtils {
    public static tComic db2thrift_Comic(Comic db) {
//        Dùng contructor tComic custom
        tComic t = new tComic(db.getName(),db.getAuthor(),db.getSource(),db.getStatus());
        return t;
    }
}
