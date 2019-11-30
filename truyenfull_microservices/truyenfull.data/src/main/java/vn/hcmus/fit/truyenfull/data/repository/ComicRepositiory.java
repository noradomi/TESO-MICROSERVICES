package vn.hcmus.fit.truyenfull.data.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hcmus.fit.truyenfull.data.model.Comic;

import java.util.List;


@Repository
public interface ComicRepositiory extends JpaRepository<Comic,Long>, JpaSpecificationExecutor<Comic> {
    Comic findByName(String name);
    Comic findByUrlname(String urlname);


    @Query(value = "select * from Comic c join comic_category cc on c.id = cc.comic_id and cc.category_id = :id",nativeQuery = true)
    Page<Comic> findComicsByCatId(@Param(value = "id") long id,Pageable pageable);

    @Query(value = "select * from Comic c order by  c.rating desc limit 10",nativeQuery = true)
    Page<Comic>  findTop10ByRating(Pageable pageable);

    Page<Comic> findByAuthor(String autName,Pageable pageable);

    @Query(value = "select * from Comic c  join comic_catalog cat on c.id = cat.comic_id and cat.catalog_id = :num",
            nativeQuery = true)
    Page<Comic> findComicsByCriteria(@Param(value = "num") int num, Pageable pageable);

    Page<Comic> findByNameContaining(String term,Pageable pageable);

    @Query(value = "select * from Comic c order by  c.vote_count desc limit 10",nativeQuery = true)
    List<Comic>  findTop10ByVote_count();
}
