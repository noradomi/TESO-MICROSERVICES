package vn.hcmus.fit.truyenfull.crawler.selector;

public interface ComicContentBaseSelector<T extends ChapterContentBaseSelector>{
    String title();

    String author();

    String category();

    String description();

    String vote_count();

    String doneFlag();

    String rating();

    String dataFrom();

    String getChapterList();

    String reviews();

    T getChapterSelector();

    String getNextChapterPageSelector();

    String getCurrChapterPageSelector();


}
