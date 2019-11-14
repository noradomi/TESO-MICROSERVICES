package vn.hcmus.fit.truyenfull.crawler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hcmus.fit.truyenfull.crawler.model.Catalog;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog,Long> {
    Catalog findByUrlname(String urlName);
}
