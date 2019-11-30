package vn.hcmus.fit.truyenfull.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hcmus.fit.truyenfull.data.model.Catalog;

/**
 * Created by Asus on 11/27/2019.
 */
public interface CatalogRepository extends JpaRepository<Catalog,Long> {
    Catalog findByName(String name);
}
