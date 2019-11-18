namespace java vn.hcmus.fit.truyenfull.lib_data

typedef i32 int
typedef i64 long

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

service ComicService{
	tComic getByName(1:string name);
}