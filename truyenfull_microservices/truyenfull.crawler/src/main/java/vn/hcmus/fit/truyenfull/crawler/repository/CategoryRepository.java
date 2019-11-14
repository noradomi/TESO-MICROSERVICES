package vn.hcmus.fit.truyenfull.crawler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hcmus.fit.truyenfull.crawler.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByUrlname(String urlname);
    Category findByName(String name);
}
