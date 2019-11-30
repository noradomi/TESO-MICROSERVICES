package vn.hcmus.fit.truyenfull.data.utils;

/**
 * Created by Asus on 11/22/2019.
 */
public class PaginationUtils {
    public static boolean isValidPaginationInput(int page,int maxLength){
        if(page <= 0 ) return false;
        else if(maxLength < 1 || maxLength > 100)
            return false;
        else
            return true;
    }
}
