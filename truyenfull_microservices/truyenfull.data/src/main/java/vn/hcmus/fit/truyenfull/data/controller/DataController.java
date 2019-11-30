package vn.hcmus.fit.truyenfull.data.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import vn.hcmus.fit.truyenfull.data.exceptions.ResourceNotFoundException;
import vn.hcmus.fit.truyenfull.data.model.Catalog;
import vn.hcmus.fit.truyenfull.data.model.Chapter;
import vn.hcmus.fit.truyenfull.data.model.Comic;
import vn.hcmus.fit.truyenfull.data.repository.*;
import vn.hcmus.fit.truyenfull.data.specification.ComicSpecificationBuilder;
import vn.hcmus.fit.truyenfull.data.utils.MappingDataUtils;
import vn.hcmus.fit.truyenfull.data.utils.PaginationUtils;
import vn.hcmus.fit.truyenfull.data.utils.ReponseUtils;
import vn.hcmus.fit.truyenfull.lib_data.TruyenFullDataService;
import vn.hcmus.fit.truyenfull.lib_data.tComic;
import vn.hcmus.fit.truyenfull.lib_data.utils.MappingUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Asus on 11/14/2019.
 */
@Controller
public class DataController implements TruyenFullDataService.Iface {
    @Autowired
    ComicRepositiory comicRepositiory;

    @Autowired
    CatalogRepository catalogRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    RedisRepository redisRepository;

//    Phương thức lấy danh sách tất cả các truyện hiện có
    @Override
    public String getAllComic(int page, int maxLength, String sortBy, String orderBy) throws TException {
        try {
            if(!PaginationUtils.isValidPaginationInput(page,maxLength))
                return ReponseUtils.inValid("Page and MaxLength is inValid! (page > 0,1 <= maxLength <= 100 ");

            Page<Comic> p;

//            Kiểm tra sortBy có là 1 name field trong class Comic(name,url_name,...) không ?
            Field[] allFields = Comic.class.getDeclaredFields();
            if(!Arrays.stream(allFields).anyMatch(field ->
                    field.getName().equals(sortBy)
            ))
                return ReponseUtils.inValid("Query parameter is invalid : sort_by = "+ sortBy + " is not a field of class.");

            if (sortBy.equals(""))
                p= comicRepositiory.findAll(PageRequest.of(page-1,maxLength));
            else{
                Sort sort = orderBy.toUpperCase().equals("ASC")
                        ? Sort.by(sortBy).ascending()
                        : Sort.by(sortBy).descending();
//                Phân trang và sắp xếp
                p = comicRepositiory.findAll(PageRequest.of(page-1,maxLength,sort));
            }
            if(!orderBy.toUpperCase().equals("ASC") && !orderBy.toUpperCase().equals("DESC"))
                return ReponseUtils.inValid("Query parameter is invalid (order_by = {asc,desc})");

            if(p.isEmpty())
                return ReponseUtils.NotFound("Data Empty");

            return ReponseUtils.successPagination(p);
        } catch (Exception e) {
            e.printStackTrace();
            return ReponseUtils.serverError();
        }
    }


    //  Phương thức Update 1 truyện
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

//    Phương thức thêm 1 truyện
    @Override
    public String addComic(tComic tcomic) throws TException {
//        Comic comic1 = MappingUtils.thrift2db_Comic(comic);
        try {
//            System.out.println(tcomic);
            if(StringUtils.isEmpty(tcomic.getName()) || StringUtils.isEmpty(tcomic.getUrlname()))
                return ReponseUtils.inValid("Missing some parameters.");

            Comic comic = MappingDataUtils.thrift2db_Comic(tcomic);

            comicRepositiory.save(comic);

            return ReponseUtils.success(ReponseUtils.returnComic(comic));
        } catch (Exception e) {
            e.printStackTrace();
            return ReponseUtils.serverError();
        }

    }


//    Phương thức xóa 1 truyện
    @Override
    public String deleteComic(long id) throws TException {
        Comic comic = comicRepositiory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comic", "id", id));
        comicRepositiory.delete(comic);

        return ResponseEntity.ok().build().toString();
    }

//    Phương thức lấy 1 truyện theo Id
    @Override
    public String getAComic(long id) throws TException {
        try {
            Comic comic = comicRepositiory.findById(new Long(id)).orElseThrow(() -> new ResourceNotFoundException("Comic", "id", id));

            return ReponseUtils.successNonPagination(ReponseUtils.returnComic(comic));
        }
        catch (ResourceNotFoundException e){
            e.printStackTrace();
            return ReponseUtils.NotFound("Data Not Found For ID Comic : "+id);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ReponseUtils.serverError();
        }

    }



//    Phương thức lấy các truyện có Mức Rating cao nhất (rating >= 7.0)
    @Override
    public String getTopRatedComics() throws TException {
        try {
            Page<Comic> p = comicRepositiory.findAll(PageRequest.of(0,10,Sort.by(Sort.Direction.DESC, "rating")));
            if(p.isEmpty())
                return ReponseUtils.NotFound("Data Empty");

            return   ReponseUtils.successNonPagination(ReponseUtils.returnListNameComic(p.getContent()));
        } catch (Exception e) {
            e.printStackTrace();
            return ReponseUtils.serverError();
        }
    }


    public String getComicsByCriteria(int page, int maxLength,String sortBy, String orderBy, String criteria){
        try {
            if(!PaginationUtils.isValidPaginationInput(page,maxLength))
                return ReponseUtils.inValid("Page and MaxLength is inValid! (page > 0,1 <= maxLength <= 100 ");

            Page<Comic> p;

//            Kiểm tra sortBy có là 1 name field trong class Comic(name,url_name,...) không ?
            Field[] allFields = Comic.class.getDeclaredFields();
            if(!Arrays.stream(allFields).anyMatch(field ->
                    field.getName().equals(sortBy)
            ))
                return ReponseUtils.inValid("Query parameter is invalid : sort_by = "+ sortBy + " is not a field of class.");

            Catalog c = catalogRepository.findByName(criteria);

            if (sortBy.equals(""))
                p = comicRepositiory.findComicsByCriteria(c.getId().intValue(),PageRequest.of(page-1,maxLength));
            else{
                Sort sort = orderBy.toUpperCase().equals("ASC")
                        ? Sort.by(sortBy).ascending()
                        : Sort.by(sortBy).descending();
//                Phân trang và sắp xếp
                p = comicRepositiory.findComicsByCriteria(c.getId().intValue(),PageRequest.of(page-1,maxLength,sort));
            }
            if(!orderBy.toUpperCase().equals("ASC") && !orderBy.toUpperCase().equals("DESC"))
                return ReponseUtils.inValid("Query parameter is invalid (order_by = {asc,desc})");


            if(p.isEmpty())
                return ReponseUtils.NotFound("Data Empty");

            return ReponseUtils.successPagination(p);

        } catch (Exception e) {
            e.printStackTrace();
            return ReponseUtils.serverError();
        }
    }

//    Phương thức lấy các truyện MỚI NHẤT
    @Override
    public String getLatestComic(int page, int maxLength,String sortBy,String orderBy) throws TException {
        return getComicsByCriteria(page,maxLength,sortBy,orderBy,"New");
    }

//    Phương thức lấy các truyện đã HOÀN THÀNH
    @Override
    public String getFinishedComics(int page, int maxLength,String sortBy,String orderBy) throws TException {
        return getComicsByCriteria(page,maxLength,sortBy,orderBy,"Full");
    }

//  Phương thức lấy các truyện HOT NHẤT
    @Override
    public String getHotComics(int page, int maxLength,String sortBy,String orderBy) throws TException {
        return getComicsByCriteria(page,maxLength,sortBy,orderBy,"Hot");
    }

//    Phương thức lấy truyện theo Category ID
    @Override
    public String getComicsByCategory(long catId,int page,int maxLength,String sortBy,String orderBy) throws TException {
        try {
            if(!PaginationUtils.isValidPaginationInput(page,maxLength))
                return ReponseUtils.inValid("Page and MaxLength is inValid! (page > 0,1 <= maxLength <= 100 ");

            Page<Comic> p;

//            Kiểm tra sortBy có là 1 name field trong class Comic(name,url_name,...) không ?
            Field[] allFields = Comic.class.getDeclaredFields();
            if(!Arrays.stream(allFields).anyMatch(field ->
                    field.getName().equals(sortBy)
            ))
                return ReponseUtils.inValid("Query parameter is invalid : sort_by = "+ sortBy + " is not a field of class.");

            if (sortBy.equals(""))
                p= comicRepositiory.findComicsByCatId(catId,PageRequest.of(page-1,maxLength));
            else{
                Sort sort = orderBy.toUpperCase().equals("ASC")
                        ? Sort.by(sortBy).ascending()
                        : Sort.by(sortBy).descending();
//                Phân trang và sắp xếp
                p = comicRepositiory.findComicsByCatId(catId, PageRequest.of(page-1,maxLength,sort));
            }
            if(!orderBy.toUpperCase().equals("ASC") && !orderBy.toUpperCase().equals("DESC"))
                return ReponseUtils.inValid("Query parameter is invalid (order_by = {asc,desc})");




            if(p.isEmpty())
                return ReponseUtils.NotFound("Data Empty");

            return ReponseUtils.successPagination(p);
        } catch (Exception e) {
            e.printStackTrace();
            return ReponseUtils.serverError();
        }
    }

//    Phương thức lấy truyện theo Tác giả
    @Override
    public String getComicsByAuthor(String autName, int page, int maxLength,String sortBy,String orderBy) throws TException {
        try {
            if(!PaginationUtils.isValidPaginationInput(page,maxLength))
                return ReponseUtils.inValid("Page and MaxLength is inValid! (page > 0,1 <= maxLength <= 100 ");

            Page<Comic> p;

//            Kiểm tra sortBy có là 1 name field trong class Comic(name,url_name,...) không ?
            Field[] allFields = Comic.class.getDeclaredFields();
            if(!Arrays.stream(allFields).anyMatch(field ->
                    field.getName().equals(sortBy)
            ))
                return ReponseUtils.inValid("Query parameter is invalid : sort_by = "+ sortBy + " is not a field of class.");

            if (sortBy.equals(""))
                p= comicRepositiory.findByAuthor(autName,PageRequest.of(page-1,maxLength));
            else{
                Sort sort = orderBy.toUpperCase().equals("ASC")
                        ? Sort.by(sortBy).ascending()
                        : Sort.by(sortBy).descending();
//                Phân trang và sắp xếp
                p = comicRepositiory.findByAuthor(autName, PageRequest.of(page-1,maxLength,sort));
            }
            if(!orderBy.toUpperCase().equals("ASC") && !orderBy.toUpperCase().equals("DESC"))
                return ReponseUtils.inValid("Query parameter is invalid (order_by = {asc,desc})");

            if(p.isEmpty())
                return ReponseUtils.NotFound("Data Empty");

            return ReponseUtils.successPagination(p);
        } catch (Exception e) {
            e.printStackTrace();
            return ReponseUtils.serverError();
        }
    }


    @Override
    public String getReviewsOfComic(long id) throws TException {
        return null;
    }

//    Phương thức lấy danh sách Chapter của truyện
    @Override
    public String getChaptersOfComic(long id, int page, int maxLength,String sortBy,String orderBy) throws TException {

        if(!PaginationUtils.isValidPaginationInput(page,maxLength))
            return ReponseUtils.inValid("Page and MaxLength is inValid! (page > 0,1 <= maxLength <= 100 ");

        Page<Chapter> p;
//        Kiểm tra trong redis cache có lưu chapters của comic id này không? Nếu có trả về dữ liệu từ cạche
        if(!redisRepository.findAllComic(String.valueOf(id)).isEmpty()){
            System.out.println("Có dữ liệu trong cache ^v^");
            List<Chapter> chapters = new ArrayList(redisRepository.findAllComic(String.valueOf(id)).values());
            Collections.sort(chapters);
//            Sort theo property sort_by và order_by
            boolean isAsc = orderBy.toUpperCase().equals("ASC");
            MutableSortDefinition sort = new MutableSortDefinition(sortBy, false, isAsc);
//            Phân trang bằng PagedListHolder
            PagedListHolder pagedListHolder = new PagedListHolder(chapters);
            pagedListHolder.setPageSize(maxLength);
            pagedListHolder.setPage(page-1);

           return ReponseUtils.returnChaptersByComicId(
                   pagedListHolder.getPageList(),
                   pagedListHolder.getPage()+1,
                   chapters.size(),
                   pagedListHolder.getPageCount())
                   .toString();
        }
//        Dữ liệu trả về có phân trang


//            Kiểm tra sortBy có là 1 name field trong class Comic(name,url_name,...) không ?
        Field[] allFields = Comic.class.getDeclaredFields();
        if(!Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(sortBy)
        ))
            return ReponseUtils.inValid("Query parameter is invalid : sort_by = "+ sortBy + " is not a field of class.");

        if(!orderBy.toUpperCase().equals("ASC") && !orderBy.toUpperCase().equals("DESC"))
            return ReponseUtils.inValid("Query parameter is invalid (order_by = {asc,desc})");

        if (sortBy.equals(""))
            p = chapterRepository.findChapterByComic(id,PageRequest.of(page-1,maxLength));
        else{
            Sort sort = orderBy.toUpperCase().equals("ASC")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
//                Phân trang và sắp xếp
            p = chapterRepository.findChapterByComic(id,PageRequest.of(page-1,maxLength,sort));
        }

        if(p.isEmpty())
            return ReponseUtils.NotFound("Data Empty");

        return ReponseUtils.returnChaptersByComicId(p.getContent(),p.getNumber(),p.getTotalElements(),p.getTotalPages()).toString();
    }

//    Phương thức lấy danh sách Thể loại của truyện
    @Override
    public String getCategoriesOfComic(long id) throws TException {
        try {
            Comic comic = comicRepositiory.findById(new Long(id)).get();

            if(comic.equals(null))
                return ReponseUtils.NotFound("Data Not Found For ID Comic : "+id);

            ArrayNode catList = ReponseUtils.returnListNameCategory(comic.getCategoryList());
            return ReponseUtils.successNonPagination(catList);
        } catch (Exception e) {
            e.printStackTrace();
            return ReponseUtils.serverError();
        }
    }

//    Phương thức tìm kiếm truyện (query)
    @Override
    public String searchComic(String name, int page, int maxLength,String sortBy,String orderBy) throws TException {
        try {
            if(!PaginationUtils.isValidPaginationInput(page,maxLength))
                return ReponseUtils.inValid("Page and MaxLength is inValid!");

            ComicSpecificationBuilder builder = new ComicSpecificationBuilder();
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(name + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }

            Specification<Comic> spec = builder.build();

            //            Kiểm tra sortBy có là 1 name field trong class Comic(name,url_name,...) không ?
            Page<Comic> p;
            Field[] allFields = Comic.class.getDeclaredFields();
            if(!Arrays.stream(allFields).anyMatch(field ->
                    field.getName().equals(sortBy)
            ))
                return ReponseUtils.inValid("Query parameter is invalid : sort_by = "+ sortBy + " is not a field of class.");

            if(!orderBy.toUpperCase().equals("ASC") && !orderBy.toUpperCase().equals("DESC"))
                return ReponseUtils.inValid("Query parameter is invalid (order_by = {asc,desc})");

            if (sortBy.equals(""))
                p = comicRepositiory.findAll(spec,PageRequest.of(page-1,maxLength));
            else {
                Sort sort = orderBy.toUpperCase().equals("ASC")
                        ? Sort.by(sortBy).ascending()
                        : Sort.by(sortBy).descending();
                p = comicRepositiory.findAll(spec, PageRequest.of(page - 1, maxLength, sort));

            }
            if(p.isEmpty())
                return ReponseUtils.NotFound("Data Empty");
            return ReponseUtils.successPagination(p);
        } catch (Exception e) {
            e.printStackTrace();
            return ReponseUtils.serverError();
        }


    }

}
