package vn.hcmus.fit.truyenfull.data.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vn.hcmus.fit.truyenfull.data.model.Chapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Asus on 11/30/2019.
 */
@Repository
public class RedisRepository {

    @Autowired
    private RedisTemplate<String,Object> template;

//    Lưu 1 chapter với KEY là id comic của Chapter đó
    public void add(Chapter c){
        template.opsForHash().putIfAbsent(c.getComic().getId().toString(),c.getId(),c);
    }

    public void addList(List<Chapter> chapter){
        chapter.stream().forEach(x -> template.opsForHash().putIfAbsent(x.getComic().getId().toString(),x.getId(),x));
    }

    public Map<Object, Object> findAllComic(String comicID) {
        return template.opsForHash().entries(comicID);
    }
}
