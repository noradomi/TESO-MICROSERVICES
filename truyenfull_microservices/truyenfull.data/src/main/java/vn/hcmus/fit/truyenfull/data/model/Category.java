package vn.hcmus.fit.truyenfull.data.model;



import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String urlname;

    //    @ManyToMany(mappedBy = "categoryList")
    @ManyToMany(fetch = FetchType.EAGER,cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    },mappedBy = "categoryList")
//    @JoinTable(name="comic_category",
//            joinColumns = @JoinColumn(name = "category_id"),
//            inverseJoinColumns = @JoinColumn(name = "comic_id"))
    private List<Comic> comicList = new ArrayList<>();

    public Category() {
    }

    public Category(String name, String urlname, List<Comic> comicList) {
        this.name = name;
        this.urlname = urlname;
        this.comicList = comicList;
    }
}
