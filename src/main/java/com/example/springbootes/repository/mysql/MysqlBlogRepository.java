package com.example.springbootes.repository.mysql;

import com.example.springbootes.entity.mysql.MysqlBlog;
import com.example.springbootes.entity.mysql.MysqlRecord;
import com.example.springbootes.entity.mysql.MysqlUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


public interface MysqlBlogRepository extends JpaRepository<MysqlBlog,Integer> {
    /**
     * 查找用户
     * @return
     */
    @Query("select e from MysqlUser e where e.userid = :userID")
    List<MysqlUser> queryUser(String userID);

    /**
     * 注册
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "insert into user(userid,userpwd) values(:userID,:userPwd)",nativeQuery = true)
    int register(String userID,String userPwd);

    /**
     * 登陆
     * @return
     */
    @Query("select e from MysqlUser e where e.userid = :userID and e.userpwd = :userPwd")
    List<MysqlUser> login(String userID,String userPwd);

    /**
     * 添加浏览记录
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "insert into record(userid,record_order,movie_id,record_time) values(:userID,:recordOrder,:movieId,:recordTime)",nativeQuery = true)
    int insertRecord(String userID, int recordOrder, String movieId, Date recordTime);

    /**
     * 查询该用户最大浏览数
     * @return
     */
    @Query("select max(e.recordOrder) from MysqlRecord e where e.userid = :userID")
    Integer queryMaxRecordOrder(String userID);

    /**
     * 通过用户ID和浏览序号，删除该用户浏览记录
     * @return
     */
    @Transactional
    @Modifying
    @Query("delete from MysqlRecord e where e.userid = :userID and e.recordOrder = :recordOrder")
    Integer deleteRecordOrder(String userID,int recordOrder);

    /**
     * 查看用户浏览记录
     * @return
     */
    @Query("select e from MysqlRecord e where e.userid = :userID")
    List<MysqlRecord> queryRecord(String userID);

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

    /**
     * 根据条件选择筛选
     * @return
     */
    @Query("select e from MysqlBlog e where e.genres like concat('%',:genresVal,'%') and e.countries like concat('%',:countriesVal,'%') and e.year >= :yearVal1 and e.year <= :yearVal2 order by e.rating desc")
    List<MysqlBlog> queryByChoiceRating(String genresVal,String countriesVal,String yearVal1,String yearVal2);

    @Query("select e from MysqlBlog e where e.genres like concat('%',:genresVal,'%') and e.countries like concat('%',:countriesVal,'%') and e.year >= :yearVal1 and e.year <= :yearVal2 order by e.year desc")
    List<MysqlBlog> queryByChoiceYear(String genresVal,String countriesVal,String yearVal1,String yearVal2);

    @Query("select e from MysqlBlog e where e.genres like concat('%',:genresVal,'%') and e.countries like concat('%',:countriesVal,'%') and e.year >= :yearVal1 and e.year <= :yearVal2 order by e.ratingsCount desc")
    List<MysqlBlog> queryByChoiceRatingCount(String genresVal,String countriesVal,String yearVal1,String yearVal2);
    /**
     * 模糊查询
     * @param keyword
     * @return
     */
    @Query("select e from MysqlBlog e where e.title like concat('%',:keyword,'%') or e.summary like concat('%',:keyword,'%')or e.directors like concat('%',:keyword,'%')or e.casts like concat('%',:keyword,'%')")
    List<MysqlBlog> queryBlog(@Param("keyword") String keyword);


}
