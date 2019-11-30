package vn.hcmus.fit.truyenfullapi.model;


import lombok.Data;
import java.util.ArrayList;
import java.util.List;


@Data
public class Comic {

    private Long id;


    private String name;


    private String urlname;

    private String author;

    private String source;

    private String status;

    private List<Chapter> chapterList = new ArrayList<>();

    //    Mapping voi Category

    private List<Category> categoryList = new ArrayList<>();

    public Comic() {
    }

    public Comic(String name, String urlname, String author, String source, String status, List<Chapter> chapterList, List<Category> categoryList) {
        this.name = name;
        this.urlname = urlname;
        this.author = author;
        this.source = source;
        this.status = status;
        this.chapterList = chapterList;
        this.categoryList = categoryList;
    }
}
