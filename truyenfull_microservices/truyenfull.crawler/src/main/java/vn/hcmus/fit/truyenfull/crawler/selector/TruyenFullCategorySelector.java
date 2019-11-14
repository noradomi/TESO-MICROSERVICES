package vn.hcmus.fit.truyenfull.crawler.selector;

public class TruyenFullCategorySelector implements CategoryContentBaseSelector{
    private TruyenFullCategorySelector() {

    }

    private static class SingletonHelper {

        private static final TruyenFullCategorySelector INSTANCE = new TruyenFullCategorySelector();
    }

    public static TruyenFullCategorySelector getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public String description() {
        return ".panel-body";
    }
}
