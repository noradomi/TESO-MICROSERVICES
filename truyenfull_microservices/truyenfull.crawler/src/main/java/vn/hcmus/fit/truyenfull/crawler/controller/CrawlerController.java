package vn.hcmus.fit.truyenfull.crawler.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import vn.hcmus.fit.truyenfull.crawler.model.*;
import vn.hcmus.fit.truyenfull.crawler.repository.*;
import vn.hcmus.fit.truyenfull.crawler.selector.TruyenFullCatalogSelector;
import vn.hcmus.fit.truyenfull.crawler.selector.TruyenFullChapterSelector;
import vn.hcmus.fit.truyenfull.crawler.selector.TruyenFullComicSelector;
import vn.hcmus.fit.truyenfull.crawler.selector.TruyenFullSelector;
import vn.hcmus.fit.truyenfull.crawler.utils.ReponseUtil;
import vn.hcmus.fit.truyenfull.lib_crawler.TruyenFullCrawlerService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class CrawlerController implements TruyenFullCrawlerService.Iface{
    @Autowired
    CatalogRepository catalogRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ComicRepositiory comicRepsitory;

    @Autowired
    ChapterRepository chapterRepository;



    /**
     *  Crawler DANH SÁCH DANH MUC ở trang chủ: https://truyenfull.vn/
     */
    @GetMapping("/crawlCatalog")
    public Set<Catalog> getListCatalog() throws IOException {
//        Chuyển List sang Set.
        Set<Catalog> catalogs = catalogRepository.findAll().stream().collect(Collectors.toSet());

        TruyenFullCatalogSelector catalogSelector = TruyenFullCatalogSelector.getInstance();

        Document document = Jsoup.connect(catalogSelector.homePage()).get();
        Elements elements = document.select(catalogSelector.listCatalog());
        for (Element element : elements) {
            Catalog newCatalog = new Catalog();
            String[] temp = (element.attr("href").split("/"));
            String name = element.text();
            String urlname = temp[temp.length - 1];
            newCatalog.setName(name);
            newCatalog.setUrlname(urlname);
            catalogs.add(newCatalog);
        }

        catalogRepository.saveAll(catalogs);
        return catalogs;
    }


    /**
     *  Crawler THẺ LOẠI ở trang chủ: https://truyenfull.vn/
     */
    @GetMapping("/crawlCategory")
    public Set<Category> getListCategory() throws IOException {
//        Chuyển List sang Set.
        Set<Category> categories = categoryRepository.findAll().stream().collect(Collectors.toSet());
        Document document = Jsoup.connect("https://truyenfull.vn/").get();
        Elements elements = document.select("div.row > div.col-md-4 > ul > li > a");
        for (Element element : elements) {
            Category newCategory = new Category();
            String[] temp = (element.attr("href").split("/"));
            String name = element.text();
            String urlName = temp[temp.length - 1];
            newCategory.setName(name);
            newCategory.setUrlname(urlName);
            newCategory.setDescription(getDescriptionCategory(element.attr("href")));
            categories.add(newCategory);
        }

        categoryRepository.saveAll(categories);
        return categories;
    }

    public String getDescriptionCategory(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        Element ele = document.selectFirst("div.panel-body");
        return ele.text();
    }

    /**
     *  soluong: số lượng truyện cần crawl
     *  url: danh muc cần crawl: truyen-hot, ngon-tin, truyen-hot,... Mặc định sẽ crawl ở truyện hot
     */
//    @GetMapping("/crawlerComics")
    public String crawler(int soluong,String url) throws IOException {
        if(categoryRepository.findAll().isEmpty())
            getListCategory();
        TruyenFullSelector selector = new TruyenFullSelector();
        String urlComic,urlLatestChapter;
        boolean hasNext = false;
//        Url danh muc truyen crawl
        String urlNextPage = selector.mainUrl()+url+"/";
        int i =0 ;
//        Danh sách truyện đã crawl
        Set<Comic> res = new HashSet<>();
        do{
            Document document = Jsoup.connect(urlNextPage).get();
            Elements elements = document.select(selector.getComicRow());
            for (Element element : elements) {
                System.out.println((i+1)+") Tên truyện: " + element.text());
//                Lấy url của tên truyện
                urlComic = element.select("a[itemprop=url]").first().attr("href");
//                Lấy url của chapter mới nhất của truyện
                urlLatestChapter = element.select("div.text-info > div > a").first().attr("href");
//                System.out.println(urlComic);

                //Xét các danh mục cho truyện: New , Hot hay Full
                boolean isNew,isFull,isHot;
                isNew = element.select("span.label-new").first() != null;
                isHot = element.select("span.label-hot").first() != null;
                isFull = element.select("span.label-full").first() != null;

                Comic crawledComic = crawlComic(urlComic,urlLatestChapter,isNew,isFull,isHot);
                if( crawledComic != null){
                    i++;
                    res.add(crawledComic);
                }
                if(i == soluong)
                    break;
                System.out.println("/t Status: Done");
            }

            Element nextPageButton = document.selectFirst(selector.getCatalogContentSelector().getNextComicPageSelector());
            if(nextPageButton !=null) {
                if (!nextPageButton.attr("href").equals("javascript:void(0)")) {
                    hasNext = true;
                    urlNextPage = nextPageButton.attr("href");
//                    System.out.println("Truyen hot link page:" + urlNextPage);
                } else {
                    hasNext = false;
                }
            }
            else{ // Trường hợp tất cả chapter chỉ có 1 page
                hasNext = false;
            }

        } while (hasNext && i<soluong);
        return ReponseUtil.success(ReponseUtil.returnListNameComic(res.stream().collect(Collectors.toList())));
    }

    public Comic crawlComic(String urlComic,String urlLatestChapter,boolean isNew,boolean isHot,boolean isFull) throws IOException {
        TruyenFullComicSelector selector = TruyenFullComicSelector.getInstance();
        String url = urlComic;

        String[] temp = urlComic.split("/");

        String urlName = temp[temp.length - 1];


        /**
         * Kiểm tra comic đã crawled chưa:
         *  TH1: Chưa -> Tạo comic mới và crawl
         *  TH2: Rồi -> Không tạo mới mà chỉ xét chapter của comic
         *      a> Crawl chưa hết chapter -> crawl chapter tiếp
         *      b> Crawl hết chapter rồi nhưng có thêm chapter mới (Trạng thái comic là: "Đang ra") -> crawl chapter tiếp
         *      c> Crawl hết chapter rồi và trạng thái comic là "Full:" -> không crawl nữa
         */
//        Nếu comic đã được crawl rồi
        Comic testedComic = comicRepsitory.findByUrlname(urlName);

        if( testedComic != null ) {
            // Và cũng đã crawl hết chapter thì không crawl nữa

            if(isCrawledChapter(urlLatestChapter)) {
//                System.out.println("Đã crawl truyện "+urlName);
                return null;
            }
            // Ngược lại nếu còn chapter chưa crawl thì crawl chapters và add vào comic đó.

            else{
//                System.out.println(urlName + "->  còn thiếu chapter" );
                crawlChapterOfCommic(url,testedComic);
                return null;
            }
        }

//        Các trường hợp còn lại thì crawl bình thường, chapter được crawl rồi thì bỏ qua.
        Document document = Jsoup.connect(url).get();
        Comic crawledComic = new Comic();

        //        Set catalog: New, Full, Hot cho comic
        if(isNew){
            Catalog catalog = catalogRepository.findByUrlname("new");
            crawledComic.getCatalogList().add(catalog);
        }
        if(isHot){
            Catalog catalog = catalogRepository.findByUrlname("hot");
            crawledComic.getCatalogList().add(catalog);
        }
        if(isFull){
            Catalog catalog = catalogRepository.findByUrlname("full");
            crawledComic.getCatalogList().add(catalog);
        }

//        Set các field cho comic, trừ chapterlist
        crawledComic.setName(document.selectFirst(selector.title()).text());
        crawledComic.setUrlname(urlName);
        crawledComic.setAuthor(document.selectFirst(selector.author()).text());

        crawledComic.setDescription(document.selectFirst(selector.description()).text());
        crawledComic.setRating(Double.parseDouble(document.selectFirst(selector.rating()).text()));
        crawledComic.setVote_count(Integer.parseInt(document.selectFirst(selector.vote_count()).text()));

//        Set reviews của comic
        /*
       Elements reviewRows = document.select(selector.reviews());
        for (Element reviewRow : reviewRows) {
            Review_Comic reviewComic = new Review_Comic();
            reviewComic.setReviewerName(reviewRow.select("a.UFICommentActorName").text());
            reviewComic.setContent(reviewRow.select("span._5mdd").text());
            crawledComic.getReviewComicList().add(reviewComic);
        }
         */


        if(document.selectFirst(selector.doneFlag()) == null){
            crawledComic.setStatus("Đang ra");
        }
        else {
            crawledComic.setStatus(document.selectFirst(selector.doneFlag()).text());
        }
        String source = document.selectFirst(selector.dataFrom()) != null
                ? document.selectFirst(selector.dataFrom()).text()
                : "null";
        crawledComic.setSource(source);
//      Set các thể loại cho comic
        Elements genres = document.select(selector.category());
        for (Element genre : genres) {
            Category category =  categoryRepository.findByName(genre.attr("title"));
//            category.getComicList().add(crawledComic);
            crawledComic.getCategoryList().add(category);
        }
        comicRepsitory.save(crawledComic);
        crawlChapterOfCommic(url,crawledComic);
        return crawledComic;
    }

    //    Hàm kiểm tra chapter đã được crawl chưa thông qua tên chapter
    public boolean isCrawledChapter(String urlChapter) throws IOException {
        Document document = Jsoup.connect(urlChapter).get();
        TruyenFullChapterSelector chapterSelector = TruyenFullChapterSelector.getInstance();
        Element chapter_title = document.selectFirst(chapterSelector.name_index());
        if(chapter_title == null)
            return true;
        String title = "";
        title = chapter_title.attr("title");
        int index = title.indexOf(":");
//        Set các field cho chapter
        if(chapterRepository.findByName(chapter_title.text()) != null)
            return true;
        else
            return false;
    }


    public void crawlChapterOfCommic(String commicUrl,Comic comic) throws IOException {
        boolean hasNext = false;
        TruyenFullComicSelector comicSelector = TruyenFullComicSelector.getInstance();
        String urlChapter;
        int indexChapter = 1;
        do {
            Document document = Jsoup.connect(commicUrl).get();
            Elements elements = document.select(comicSelector.getChapterList());

            for (Element element : elements) {
                urlChapter = element.attr("href");
//                Nếu chapter đã được crawled rồi thì pass
                if(isCrawledChapter(urlChapter)) {
                    continue;
                }
                crawlChapterDetails(urlChapter,comic,indexChapter);
                indexChapter++;
            }
            Element nextPageButton = document.select(comicSelector.getNextChapterPageSelector()).first();
            if(nextPageButton !=null) {
                if (!nextPageButton.attr("href").equals("javascript:void(0)")) {
                    hasNext = true;
                    commicUrl = nextPageButton.attr("href");
                } else {
                    hasNext = false;
                }
            }
            else{ // Trường hợp tất cả chapter chỉ có 1 page
                hasNext = false;
            }
        } while (hasNext);
    }

    public boolean crawlChapterDetails(String urlChapter,Comic comic,int chapterIndex) throws IOException {
        TruyenFullChapterSelector chapterSelector = TruyenFullChapterSelector.getInstance();
        Document document = Jsoup.connect(urlChapter).get();
        Chapter crawledChapter = new Chapter();
        Element chapter_title = document.selectFirst(chapterSelector.name_index());
        String title = "";
        crawledChapter.setName(chapter_title.text());
        crawledChapter.setContent(document.selectFirst(chapterSelector.content()).text());
        crawledChapter.setIndex(new Long(chapterIndex));
        comic.addChapter(crawledChapter);
        chapterRepository.save(crawledChapter);
        return true;
    }
}
