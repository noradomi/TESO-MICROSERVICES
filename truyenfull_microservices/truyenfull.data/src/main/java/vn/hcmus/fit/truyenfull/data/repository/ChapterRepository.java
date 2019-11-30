package vn.hcmus.fit.truyenfull.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hcmus.fit.truyenfull.data.model.Chapter;



@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Long> {
    Chapter findByName(String name);
    Chapter findByNameAndId(String name, Long id);

    @Query(value = "select * FROM Chapter c where c.comic_id = :id",nativeQuery = true)
    Page<Chapter> findChapterByComic(@Param("id") long id, Pageable pageable);
}
