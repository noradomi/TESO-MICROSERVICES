package vn.hcmus.fit.truyenfull.crawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.hcmus.fit.truyenfull.crawler.repository.ChapterRepository;
import vn.hcmus.fit.truyenfull.crawler.model.Chapter;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class ChapterController {
    @Autowired
    ChapterRepository chapterRepository;

//    Xu li cho them 1 chuong vao 1 truyen da ton tai
    @PostMapping("/chapter")
    public Chapter addChapterToComic(
            @Valid @RequestBody Chapter chapter) {
        return chapterRepository.save(chapter);
    }
}
