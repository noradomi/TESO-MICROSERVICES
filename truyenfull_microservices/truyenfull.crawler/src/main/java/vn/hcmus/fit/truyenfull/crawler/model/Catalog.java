package vn.hcmus.fit.truyenfull.crawler.model;

import lombok.Data;


import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "catalog") // Danh sach truyen
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String urlname;

    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER
    ,mappedBy = "catalogList")
    private List<Comic> comics;
}
