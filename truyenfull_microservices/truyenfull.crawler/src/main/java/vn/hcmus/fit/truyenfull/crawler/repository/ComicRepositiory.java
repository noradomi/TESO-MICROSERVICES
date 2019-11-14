package vn.hcmus.fit.truyenfull.crawler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import vn.hcmus.fit.truyenfull.crawler.model.Comic;


@Repository
public interface ComicRepositiory extends JpaRepository<Comic,Long> {
    Comic findByName(String name);
    Comic findByUrlname(String urlname);

}
