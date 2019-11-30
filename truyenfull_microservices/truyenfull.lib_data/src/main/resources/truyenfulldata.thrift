namespace java vn.hcmus.fit.truyenfull.lib_data

typedef i64 long
typedef i32 int


struct tComic{
	1: long id;
	2: string name;
	3: string urlname;
	4: string author;
	5: string source;
	6: string status;
	7: list<tChapter> chapterList;
	8: list<tCategory> categoryList;
}

struct tChapter{
	1: long id;
	2: long index;
	3: string name;
	4: string content;
	5: tComic comic;
}

struct tCategory{
	1: long id;
	2: string name;
	3: string urlname;
	4: list<tComic> comicList;
}

service TruyenFullDataService{
	// Comic
	// CRUD comic
	string getAllComic(1:int page,2:int maxLength,3:string sortBy,4:string orderBy);
	string updateComic(1:long id,2:tComic comic);
	string addComic(1:tComic comic);
	string deleteComic(1:long id);
	
	// Get comics based on some criteria		
	string getAComic(1:long id);
	string getTopRatedComics();
	string getLatestComic(1:int page,2:int maxLength,3:string sortBy,4:string orderBy)
	string getFinishedComics(1:int page,2:int maxLength,3:string sortBy,4:string orderBy);
	string getHotComics(1:int page,2:int maxLength,3:string sortBy,4:string orderBy);
	string getComicsByCategory(1:long catId,2:int page,3:int maxLength,4:string sortBy,5:string orderBy);
	string getComicsByAuthor(1:string autName,2:int page,3:int maxLength,4:string sortBy,5:string orderBy);
	
	
	// Get some properties of a comic
	string getReviewsOfComic(1:long id);
	string getChaptersOfComic(1:long id,2:int page,3:int maxLength,4:string sortBy,5:string orderBy);
	string getCategoriesOfComic(1:long id);
	
	// Searching
	string searchComic(1:string name,2:int page,3:int maxLength,4:string sortBy,5:string orderBy);
	
}