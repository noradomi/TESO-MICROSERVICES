package vn.hcmus.fit.truyenfull.lib_data.utils;


import org.dozer.DozerBeanMapper;
import vn.hcmus.fit.truyenfull.lib_data.tCategory;
import vn.hcmus.fit.truyenfull.lib_data.tChapter;
import vn.hcmus.fit.truyenfull.lib_data.tComic;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 11/15/2019.
 */
public class MappingUtils {



    private static DozerBeanMapper mapper = new DozerBeanMapper();;

    //    Hàm convert T thành E
    public static <E,T> E convertObject(T input,Class<E> eClass){
        return mapper.map(input,eClass);
    }
    //    Hàm convert List<T> thành List<E>
    public static <E,T> List<E> convertList(List<T> input, Class<E> eClass){
        List<E> eList = new ArrayList<>();
        for (T t : input  ) {
            eList.add(mapper.map(t,eClass));
        }
        return eList;
    }
}
