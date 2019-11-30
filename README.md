# TESO-MICROSERVICES

# Mô tả các chức năng 

### 1. Crawl comic 

  Crawl data comic từ truyenfull.vn

* **URL**

  /comics/crawl

* **Method:**
  
  `GET`
  
*  **URL Params** 

    `sl=[1-~] unsigned integer default:1 -> số lượng comic cần crawl`
    
    `url=[1-~] unsigned integer default:'truyen-hot' -> danh mục muốn crawl`

* **Success Response:**
  
  ![11](https://user-images.githubusercontent.com/38036797/69901580-31e09980-13b6-11ea-8e7c-019b43ff12cb.PNG)

* **Sample Call:**

   `GET` /comics/crawl <br />
   `GET` /comics/crawl?sl=2&url=truyen-moi



### 2. Get all comic

  Lấy tất cả các truyện có phân trang và sort theo properties.

* **URL**

  /comics

* **Method:**
  
  `GET`
  
* **Success Response:**
  
<img src="https://user-images.githubusercontent.com/38036797/69901649-2cd01a00-13b7-11ea-8871-834c35e6dc08.PNG" width="800" height="500" />
 
* **Error Response:**
    **Content:**
    
        {
          "StatusCode": 1000,
          "Message": "Page and MaxLength is inValid! (page > 0,1 <= maxLength <= 100 "
        }
        or
        {
          "StatusCode": 1000,
          "Message": "Query parameter is invalid : sort_by = IDD is not a field of class."
        }

* **Sample Call:**

   `GET` /comics?page=1&limit=10&sort_by=name&order_by=asc <br />


### 3. Search a comic 

  Tìm kiếm một comic + multiquery (:equal,>,<)

* **URL**

  /comics/search

* **Method:**
  
  `GET`
  
*  **Data Params**

   **Required:**
   
       `query`

* **Success Response:**
![13](https://user-images.githubusercontent.com/38036797/69901864-b97bd780-13b9-11ea-8f83-277cf252be76.PNG)
 
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
  
    **Content:** 
    
        {
            "StatusCode": 1000,
            "Message": "Query parameter is invalid : sort_by = aaaa is not a field of class."
        }
    
      OR
    
        {
            "StatusCode": 1000,
            "Message": "Query parameter is invalid (order_by = {asc,desc})"
        }
        
* **Code:** 404 NotFound <br />
    
        {
            "StatusCode": 404,
            "Message": "Data Empty"
        }
        

* **Sample Call:**

   `GET` /comics/search?page=1&limit=50000&sort_by=name&order_by=a&query=name:Yêu,vote_count>15



### 4. CRUD Comic

  Thêm, Sửa, Xóa 1 comic

* **ADD COMIC**

  ![14](https://user-images.githubusercontent.com/38036797/69902155-bb936580-13bc-11ea-8b8c-c840fb7fe55a.PNG)

* **UPDATE COMIC**

  ![15](https://user-images.githubusercontent.com/38036797/69902171-ee3d5e00-13bc-11ea-8889-07db1d66eab9.PNG)

* **DELETE COMIC**
  
  ![16](https://user-images.githubusercontent.com/38036797/69902234-86d3de00-13bd-11ea-9cf5-36a81241c3b6.PNG)
  
### 5.  Get Top Rated Comics

  Lấy 10 comic có rating cao nhất 

* **URL**

  /comics/top_rated

* **Method:**
  
  `GET`
  
* **Success Response:**
  
  ![17](https://user-images.githubusercontent.com/38036797/69902419-1b3f4000-13c0-11ea-95cb-edfebd5e0333.PNG)
 
* **Sample Call:**

   `GET` comics/top_rated <br />


### 6. Get Latest Comics

  Lấy các Comic mới nhất (có pagination và sorting)

* **URL**

  comics/latest

* **Method:**
  
  `GET`
   
* **Success Response:**
  
  ![18](https://user-images.githubusercontent.com/38036797/69902454-a7e9fe00-13c0-11ea-9dc0-d88dee3d8117.PNG)
 
* **Error Response:**

  `Như trên`

* **Sample Call:**

   `GET` comics/latest?page=1&limit=10&sort_by=rating&order_by=asc <br />
   

### 7. Get finished comics

  Lấy các Comics đã hoàn thành (có pagination và sorting)
  Các params như trên.
  
![19](https://user-images.githubusercontent.com/38036797/69902486-15962a00-13c1-11ea-9ebf-ea0c5ae4d88c.PNG)

### 8. Get hot comics
  Lấy các Comics hot (có pagination và sorting)

 ![20](https://user-images.githubusercontent.com/38036797/69902512-5beb8900-13c1-11ea-8dff-de79012ce26f.PNG)

### 9. Get a specified Comic

  Lấy thông tin chi tiết của 1 comic bằng id

* **URL**

  /comics/{id}

* **Method:**
  
  `GET`
  
   
* **Success Response:**
  
  ![21](https://user-images.githubusercontent.com/38036797/69902537-89383700-13c1-11ea-8866-fa6a210158ef.PNG)

 
* **Error Response:**

  * **Code:** 404 Not Found <br />
    **Content:** 
    ![22](https://user-images.githubusercontent.com/38036797/69902623-8be75c00-13c2-11ea-99de-10e48c99163b.PNG)
        
   
### 10. Get categories of comic
  Lấy các thể loại của comic

* **URL**

  /comics/{id}/category

* **Method:**
  
  `GET`
   
* **Success Response:**
  
  ![23](https://user-images.githubusercontent.com/38036797/69902679-fef0d280-13c2-11ea-9620-d09e04916efb.PNG)
 
   
### 11. Redis Caching - Get Chapters Of Comic

  Lấy các chapters của Comic (có pagination) + redis cache

* **URL**

  /comics/{id}/chapters

* **Method:**
  
  `GET`
      
* **Success Response:**
  
  ![24](https://user-images.githubusercontent.com/38036797/69902743-82aabf00-13c3-11ea-8504-44932a813a72.PNG)
 
* **Redis Cache**

    Lưu chapters của 10 comic có số vote_count cao nhất vào redis sử dụng Hash:
    * key: Id comic
    * field: Id chapter
    * value: chapter

  ![25](https://user-images.githubusercontent.com/38036797/69902846-ac181a80-13c4-11ea-87c1-d9af5ef3e79d.PNG)

* **Cập nhật cache:**

   Mỗi lần restart lại server sẽ update lại cache.
   ![26](https://user-images.githubusercontent.com/38036797/69902901-52642000-13c5-11ea-86a8-65051dc77001.PNG)
   
      
