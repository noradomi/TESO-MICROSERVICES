package vn.hcmus.fit.truyenfull.crawler.selector;

public class TruyenFullSelector implements
        WebComicBaseSelector<TruyenFullComicSelector, TruyenFullCategorySelector,TruyenFullCatalogSelector> {

    @Override
    public String mainUrl() {
        return "https://truyenfull.vn/danh-sach/";
    }

    @Override
    public String getCategoryListSelector() {
        return ".list-cat .row a";
    }

    @Override
    public String getComicRow() {
        return "div[class=row][itemscope]";
    }

    public String getCurrChapterOfComic(){
        return "#list-page > div.col-xs-12.col-sm-12.col-md-9.col-truyen-main > div.list.list-truyen.col-xs-12 > div:nth-child(2) > div.col-xs-2.text-info > div > a";

    }
    @Override
    public String getNextStoryPageSelector() {
        return ".pagination li[class=active] + li > a";
    }

    @Override
    public TruyenFullComicSelector getComicContentSelector() {
        return TruyenFullComicSelector.getInstance();
    }

    @Override
    public TruyenFullCategorySelector getCategoryContentSelector() {
        return TruyenFullCategorySelector.getInstance();
    }

    @Override
    public TruyenFullCatalogSelector getCatalogContentSelector() {
        return TruyenFullCatalogSelector.getInstance();
    }

    @Override
    public String getUrlComic() {
        return "div.row > div.col-xs-7 > div > h3.truyen-title > a";
    }
}
