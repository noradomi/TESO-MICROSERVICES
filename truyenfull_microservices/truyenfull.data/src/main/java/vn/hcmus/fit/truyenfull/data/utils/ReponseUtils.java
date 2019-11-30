package vn.hcmus.fit.truyenfull.data.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import vn.hcmus.fit.truyenfull.data.constant.StatusCode;
import vn.hcmus.fit.truyenfull.data.model.Category;
import vn.hcmus.fit.truyenfull.data.model.Chapter;
import vn.hcmus.fit.truyenfull.data.model.Comic;


import java.util.ArrayList;
import java.util.List;

public class ReponseUtils {
    private static ObjectMapper mapper = new ObjectMapper();

//    Ham tra ve mot Chapter
    public static ObjectNode returnChapter(Chapter chapter){
        ObjectNode node = mapper.createObjectNode();
        node.put("chapter",chapter.getIndex());
        node.put("name",chapter.getName());
        node.put("content",chapter.getContent());
        return node;
    }

    //    Ham tra ve mot Chapter ma khong bao gom content
    public static ObjectNode returnChapterWithoutContent(Chapter chapter){
        ObjectNode node = mapper.createObjectNode();
        node.put("id",chapter.getId());
        node.put("name",chapter.getName());
//        node.put("content",chapter.getContent());
        return node;
    }


//    Ham tra ve mot List<Chapter>
    public static ArrayNode returnListChapter(List<Chapter> chapters){
        ArrayNode nodes = mapper.createArrayNode();
        for (Chapter chapter : chapters) {
            nodes.add((returnChapter(chapter)));
        }
        return nodes;
    }

    public static ObjectNode returnChaptersByComicId(List<Chapter> chapters,int page,long total_results,int total_pages){
        ObjectNode node = mapper.createObjectNode();
        node.put("page",page);
        node.put("total_results",total_results);
        node.put("total_pages",total_pages);
        node.put("results",returnListChapterWithoutContent(chapters));
        return node;
    }

    //    Ham tra ve mot List<Chapter> voi Chapter khong bao gom Content
    public static ArrayNode returnListChapterWithoutContent(List<Chapter> chapters){
        if(chapters == null)
            return null;
        ArrayNode nodes = mapper.createArrayNode();
        for (Chapter chapter : chapters) {
            nodes.add((returnChapterWithoutContent(chapter)));
        }
        return nodes;
    }

//    Ham tra ve mot Comic
    public static ObjectNode returnComic(Comic comic){
        ObjectNode node = mapper.createObjectNode();
        node.put("id",comic.getId());
        node.put("name",comic.getName());
        node.put("author",comic.getAuthor());
        node.put("source",comic.getSource());
        node.put("status",comic.getStatus());
        node.put("description",comic.getDescription());
        node.put("rating",comic.getRating());
        node.put("vote_count",comic.getVote_count());
//        node.set("chapter-list",returnListChapterWithoutContent(comic.getChapterList()));
        List<String> listNameCat = new ArrayList<>();

//        ForEach Java 8
        comic.getCategoryList().forEach(cat-> listNameCat.add(cat.getName()));
        node.put("category-list",listNameCat.toString());
        return node;
    }

    //    Hàm trả về một name của một Comic
    public static ObjectNode returnNameComic(Comic comic){
        ObjectNode node = mapper.createObjectNode();
        node.put("id",comic.getId());
        node.put("name",comic.getName());
        return node;
    }


//    Ham tra ve mot List<Comic>
    public static ArrayNode returnListComic(List<Comic> comics){
        ArrayNode nodes = mapper.createArrayNode();
        for (Comic comic : comics) {
            nodes.add((returnComic(comic)));
        }
        return nodes;
    }

    //    Ham tra ve mot List Name <Comic>
    public static ArrayNode returnListNameComic(List<Comic> comics){
        ArrayNode nodes = mapper.createArrayNode();
        for (Comic comic : comics) {
            nodes.add((returnNameComic(comic)));
        }
        return nodes;
    }

//    Hàm trả về một Category
    public static ObjectNode returnCategory(Category category){
        ObjectNode node = mapper.createObjectNode();
        node.put("id",category.getId());
        node.put("name",category.getName());
        node.set("comic-list",returnListNameComic(category.getComicList()));
        return node;
    }

    //    Hàm trả về một name của một Comic
    public static ObjectNode returnNameCategory(Category category){
        ObjectNode node = mapper.createObjectNode();
        node.put("id",category.getId());
        node.put("name",category.getName());
        return node;
    }



    //    Ham tra ve mot List<Category>
    public static ArrayNode returnListCategory(List<Category> categories){
        ArrayNode nodes = mapper.createArrayNode();
        for (Category category : categories) {
            nodes.add((returnCategory(category)));
        }
        return nodes;
    }

    //    Ham tra ve mot List Name <Comic>
    public static ArrayNode returnListNameCategory(List<Category> categories){
        if(categories == null)
            return null;
        ArrayNode nodes = mapper.createArrayNode();
        for (Category category : categories) {
            nodes.add((returnNameCategory(category)));
        }
        return nodes;
    }

    //  Các hàm xử lí response
    public static ObjectNode successBodyPagination(ArrayNode res,int page,long total_results,int total_pages){
        ObjectNode node = mapper.createObjectNode();
        node.put("page",page);
        node.put("total_results",total_results);
        node.put("total_pages",total_pages);
        node.put("results",res);
        return node;
    }

    public static String successPagination(Page<Comic> p){
        ObjectNode node = mapper.createObjectNode();
        node.put(StatusCode.class.getSimpleName(), StatusCode.SUCCESS.getValue());
        node.put("Message", StatusCode.SUCCESS.name());

        ObjectNode body = mapper.createObjectNode();
        body.put("page",p.getNumber()+1);
        body.put("total_results",p.getTotalElements());
        body.put("total_pages",p.getTotalPages());
        body.set("results",returnListNameComic(p.getContent()));
        node.set("Response",body);
        return node.toString();
    }

//    Overloading methods
    public static String successNonPagination(ObjectNode p){
        ObjectNode node = mapper.createObjectNode();
        node.put(StatusCode.class.getSimpleName(), StatusCode.SUCCESS.getValue());
        node.put("Message", StatusCode.SUCCESS.name());

        ObjectNode body = mapper.createObjectNode();
        body.set("results",p);

        node.set("Response",body);
        return node.toString();
    }

    public static String successNonPagination(ArrayNode p){
        ObjectNode node = mapper.createObjectNode();
        node.put(StatusCode.class.getSimpleName(), StatusCode.SUCCESS.getValue());
        node.put("Message", StatusCode.SUCCESS.name());

        ObjectNode body = mapper.createObjectNode();
        body.set("results",p);

        node.set("Response",body);
        return node.toString();
    }

//    Overloading methods
    public static ObjectNode successBodyNonPagination(ObjectNode res){
        ObjectNode node = mapper.createObjectNode();
        node.set("results",res);
        return node;
    }


    public static ObjectNode successBodyNonPagination(ArrayNode res){
        ObjectNode node = mapper.createObjectNode();
        node.set("results",res);
        return node;
    }

    public static String success(JsonNode body){
        ObjectNode node = mapper.createObjectNode();
        node.put(StatusCode.class.getSimpleName(), StatusCode.SUCCESS.getValue());
        node.put("Message", StatusCode.SUCCESS.name());
        node.set("Response",body);
        return node.toString();
    }

    public static String NotFound(String msg){
        ObjectNode node = mapper.createObjectNode();
        node.put(StatusCode.class.getSimpleName(), StatusCode.NOT_FOUND.getValue());
//        node.put("Message", StatusCode.NOT_FOUND.name());
        node.put("Message",msg);
        return node.toString();
    }

    public static String inValid(String msg){
        ObjectNode node = mapper.createObjectNode();
        node.put(StatusCode.class.getSimpleName(), StatusCode.PARAMETER_INVALID.getValue());
//        node.put("Message", StatusCode.PARAMETER_INVALID.name());
        node.put("Message",msg);
        return node.toString();
    }

    public static String serverError(){
        ObjectNode node = mapper.createObjectNode();
        node.put(StatusCode.class.getSimpleName(), StatusCode.SERVER_ERROR.getValue());
        node.put("Message", StatusCode.SERVER_ERROR.name());
//        node.put("Response","SERVER FAIL :(");
        return node.toString();
    }

}
