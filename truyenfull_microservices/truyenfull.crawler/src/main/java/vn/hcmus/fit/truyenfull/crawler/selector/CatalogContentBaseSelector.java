package vn.hcmus.fit.truyenfull.crawler.selector;

public interface CatalogContentBaseSelector {
    String homePage();
    String listCatalog();
    String getNextComicPageSelector();
    String getCurrComicPageSelector();
}
