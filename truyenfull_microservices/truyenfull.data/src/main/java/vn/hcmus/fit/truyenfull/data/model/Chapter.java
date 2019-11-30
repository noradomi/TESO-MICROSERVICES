package vn.hcmus.fit.truyenfull.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "chapter")
public class Chapter implements Comparable< Chapter >{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "index_chapter")
    private Long index; // so thu tu chua chapter


    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;

    @JsonIgnore
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

    @Override
    public int compareTo(Chapter o){
        return this.getId().compareTo(o.getId());
    }
}
