package vn.hcmus.fit.truyenfull.data.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "chapter")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "index_chapter")
    private Long index; // so thu tu chua chapter


    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private Comic comic;

    public Chapter() {
    }

    public Chapter(Long index, String name, String content, Comic comic) {
        this.index = index;
        this.name = name;
        this.content = content;
        this.comic = comic;
    }
}
