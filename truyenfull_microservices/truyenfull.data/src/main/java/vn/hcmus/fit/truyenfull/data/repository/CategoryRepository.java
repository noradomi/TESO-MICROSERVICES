package vn.hcmus.fit.truyenfull.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hcmus.fit.truyenfull.data.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByUrlname(String urlname);
    Category findByName(String name);
}
