package vn.hcmus.fit.truyenfullapi.model;

import lombok.Data;

@Data

public class Chapter {
    private Long id;


    private Long index; // so thu tu chua chapter


    private String name;


    private String content;


    private Comic comic;

    public Chapter(Long index, String name, String content, Comic comic) {
        this.index = index;
        this.name = name;
        this.content = content;
        this.comic = comic;
    }
}
