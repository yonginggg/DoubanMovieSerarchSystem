package com.example.springbootes.repository.mysql;

import com.example.springbootes.entity.mysql.MysqlBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//public interface MysqlBlogRepository extends JpaRepository<MysqlBlog,Integer> {
//    @Query("select e from MysqlBlog e order by e.createTime desc")
//    List<MysqlBlog> queryAll();
//    @Query("select e from MysqlBlog e where e.title like concat('%',:keyword,'%') " +
//            "or e.content like concat('%',:keyword,'%') order by e.createTime desc")
//    List<MysqlBlog> queryBlogs(@Param("keyword") String keyword);
//}

public interface MysqlBlogRepository extends JpaRepository<MysqlBlog,Integer> {
    /**
     * 电影根据评分排序
     * @return
     */
    @Query("select e from MysqlBlog e order by e.rating desc")
    List<MysqlBlog> queryAll();

    /**
     * 根据movieID查看电影详情
     * @return
     */
    @Query("select e from MysqlBlog e where e.movieId = :movieID")
    List<MysqlBlog> queryByMovieID(String movieID);

//    /**
//     * 根据条件选择筛选
//     * @return
//     */
//    @Query("select e from MysqlBlog e where e.genres like concat('%',:genresVal,'%') and e.countries like concat('%',:countriesVal,'%') and e.year = :yearVal")
//    List<MysqlBlog> queryByChoice(String genresVal,String countriesVal,String yearVal);

    /**
     * 根据条件选择筛选
     * @return
     */
    @Query("select e from MysqlBlog e where e.genres like concat('%',:genresVal,'%') and e.countries like concat('%',:countriesVal,'%') and e.year >= :yearVal1 and e.year <= :yearVal2 order by :sortVal")
    List<MysqlBlog> queryByChoice(String genresVal,String countriesVal,String yearVal1,String yearVal2,String sortVal);

    /**
     * 模糊查询
     * @param keyword
     * @return
     */
    @Query("select e from MysqlBlog e where e.title like concat('%',:keyword,'%') or e.summary like concat('%',:keyword,'%')")
    List<MysqlBlog> queryBlog(@Param("keyword") String keyword);


}
