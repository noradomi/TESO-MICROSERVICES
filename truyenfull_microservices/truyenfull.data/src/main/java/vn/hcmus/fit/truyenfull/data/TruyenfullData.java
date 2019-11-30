package vn.hcmus.fit.truyenfull.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vn.hcmus.fit.truyenfull.data.model.Comic;
import vn.hcmus.fit.truyenfull.data.repository.ChapterRepository;
import vn.hcmus.fit.truyenfull.data.repository.ComicRepositiory;
import vn.hcmus.fit.truyenfull.data.repository.RedisRepository;

import java.util.List;

@SpringBootApplication
public class TruyenfullData implements CommandLineRunner{

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private ComicRepositiory comicRepositiory;

    @Autowired
    private ChapterRepository chapterRepository;

    public static void main(String[] args) {
        SpringApplication.run(TruyenfullData.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        //        Lưu chapters của 50 truyện có vote_count cao nhất -> 50 bảng HASH với tên bảng HASH = ComicID
        List<Comic> comics = comicRepositiory.findTop10ByVote_count();

//        Lưu chapter vào redis
        comics.stream().forEach(c -> redisRepository.addList(c.getChapterList()));

        System.out.println("Update cache thành công");
    }
}
