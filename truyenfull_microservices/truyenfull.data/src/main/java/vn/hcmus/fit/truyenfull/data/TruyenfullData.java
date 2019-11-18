package vn.hcmus.fit.truyenfull.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vn.hcmus.fit.truyenfull.data.model.Category;
import vn.hcmus.fit.truyenfull.data.model.Comic;
import vn.hcmus.fit.truyenfull.data.repository.CategoryRepository;
import vn.hcmus.fit.truyenfull.data.repository.ComicRepositiory;

@SpringBootApplication
public class TruyenfullData {

//    public static void main(String[] args) {
//        SpringApplication.run(TruyenfullData.class, args);
//    }
//    @Autowired
//    private ComicRepositiory comicRepositiory;
//
//    @Autowired
//    private CategoryRepository categoryRepository;

    public static void main(String[] args) {
        SpringApplication.run(TruyenfullData.class, args);
    }


//    @Override
//    public void run(String... args) throws Exception {
//
//        Category lichsu = categoryRepository.findByUrlname("lich-su");
//
//        Category ngontinh = categoryRepository.findByUrlname("ngon-tinh");
//
//        Comic comic = new Comic();
//        comic.setAuthor("aaaaa");
//        comic.setName("aaaaaaaaaaa");
//        comic.setUrlname("aaaaaaaaaaaaaaaa");
//        comic.setSource("bbbbbbbbbbbbb");
//        comic.setStatus("aaaavvvvvvvvvv");
//
//        lichsu.getComicList().add(comic);
//        ngontinh.getComicList().add(comic);
//
//        comicRepositiory.save(comic);
//    }
}
