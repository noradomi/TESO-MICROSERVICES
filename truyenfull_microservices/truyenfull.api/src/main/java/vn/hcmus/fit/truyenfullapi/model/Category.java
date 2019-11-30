package vn.hcmus.fit.truyenfullapi.model;



import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data

public class Category {

    private Long id;


    private String name;


    private String urlname;


    private List<Comic> comicList = new ArrayList<>();

    public Category(String name, String urlname, List<Comic> comicList) {
        this.name = name;
        this.urlname = urlname;
        this.comicList = comicList;
    }
}
