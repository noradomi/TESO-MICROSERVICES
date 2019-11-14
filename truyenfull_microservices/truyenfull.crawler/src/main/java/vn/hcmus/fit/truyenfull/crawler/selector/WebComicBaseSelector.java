package vn.hcmus.fit.truyenfull.crawler.selector;

public interface WebComicBaseSelector<T extends ComicContentBaseSelector,U extends CategoryContentBaseSelector, Z extends  CatalogContentBaseSelector> {
    String mainUrl();

    String getCategoryListSelector();

    String getComicRow();

    String getNextStoryPageSelector();

    String getCurrChapterOfComic();

    T getComicContentSelector();

    U getCategoryContentSelector();

    Z getCatalogContentSelector();

    String getUrlComic();
}
