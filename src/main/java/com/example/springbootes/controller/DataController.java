package com.example.springbootes.controller;

import com.example.springbootes.entity.es.EsBlog;
import com.example.springbootes.entity.mysql.MysqlBlog;
import com.example.springbootes.entity.mysql.MysqlRecord;
import com.example.springbootes.entity.mysql.MysqlUser;
import com.example.springbootes.repository.es.EsBlogRepository;
import com.example.springbootes.repository.mysql.MysqlBlogRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.DigestUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class DataController {
    private static final String MYSQL = "mysql";
    private static final String ES = "es";

    @Autowired
    private MysqlBlogRepository mysqlBlogRepository;
    @Autowired
    private EsBlogRepository esBlogRepository;

    // 注册
    @PostMapping("/register")
    public Object register(@RequestBody user user){
        Map<Integer, String> map = new HashMap<>();
        String userid = user.getUserid();
        String userpwd = user.getUserpwd();
        String md5Pwd = DigestUtils.md5DigestAsHex(userpwd.getBytes());

        List<MysqlUser> mysqlUser = mysqlBlogRepository.queryUser(userid);
        if(mysqlUser.size() != 0){
            map.put(401,"账号已存在！");
        }
        else if (userid.equals("") || userpwd.equals("")) {
            map.put(401,"请完善账户信息！");
        }else {
            int register = mysqlBlogRepository.register(userid, md5Pwd);
            map.put(200,"注册成功!");
        }
        return map;
    }

    // 登陆
    @PostMapping("/login")
    public Object login(@RequestBody user user){
        Map<Integer, String> map = new HashMap<>();
        String userid = user.getUserid();
        String userpwd = user.getUserpwd();
        String md5Pwd = DigestUtils.md5DigestAsHex(userpwd.getBytes());

        List<MysqlUser> login = mysqlBlogRepository.login(userid, md5Pwd);
        if (userid.equals("") || userpwd.equals("")) {
            map.put(401,"账号密码不得为空！");
        } else if(login.size() == 0){
            map.put(401,"账号或密码错误！");
        } else {
            map.put(200,"登录成功!");
        }
        return map;
    }

    // 添加浏览记录（当用户点击某一搜索记录时）
    @PostMapping("/record")
    public Object record(@RequestBody record record){
        String userid = record.getUserid();
        String movieId = record.getMovieid();
        int recordOrder = 0;
        int MaxOrder = 10;
        Date now = new Date();

        Integer MaxRecordOrder = mysqlBlogRepository.queryMaxRecordOrder(userid);
        // 查询是否已存在该电影的搜索记录
        List<MysqlRecord> mysqlRecords = mysqlBlogRepository.queryRecord(userid);
        if(mysqlRecords.size()>0){
            for(int i=0;i<mysqlRecords.size();i++){
                if(movieId.equals(mysqlRecords.get(i).getMovieId()))
                    recordOrder = mysqlRecords.get(i).getRecordOrder();
            }
        }

        if (MaxRecordOrder == null){
            mysqlBlogRepository.insertRecord(userid, 1,movieId,now);
        } else if (recordOrder != 0){   // 已存在该电影的搜索记录
            mysqlBlogRepository.deleteRecordOrder(userid,recordOrder);
            mysqlBlogRepository.insertRecord(userid, recordOrder,movieId,now);
        } else if(MaxRecordOrder >= MaxOrder){  //最多保存10条
            mysqlBlogRepository.deleteRecordOrder(userid,MaxRecordOrder-9);
            mysqlBlogRepository.insertRecord(userid, MaxRecordOrder+1,movieId,now);
        }
        else
            mysqlBlogRepository.insertRecord(userid, MaxRecordOrder+1,movieId,now);
        return "insert record successful";
    }

    // 电影根据评分排序
    @GetMapping("/blogs")
    public Object blog(){
        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryAll();
        return mysqlBlogs;
    }

    // 根据movieID查看电影详情
//    @PostMapping("/movie")
//    public Object movie( String movieID){ ;
//        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryByMovieID(movieID);
//        return mysqlBlogs;
//    }
    @GetMapping("/movie/{id}")
    public Object movie(@PathVariable("id") String movieID){
        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryByMovieID(movieID);
        MysqlBlog blog = mysqlBlogs.get(0);
        return blog;
    }

    // 根据条件选择筛选
//    @PostMapping("/choice")
//    public Object choice(@RequestBody String genresVal,String countriesVal,String yearVal){ ;
//        if(genresVal.equals(""))
//            genresVal = "全部类型";
//        if(countriesVal.equals("全部地区"))
//            countriesVal = "";
//        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryByChoice(genresVal,countriesVal,yearVal);
//        return mysqlBlogs;
//    }

    // 根据条件选择筛选
    @PostMapping("/choice")
    public Object choice(@RequestBody movieclass movieclass){
        String genresVal = movieclass.getGenresVal();
        String countriesVal = movieclass.getCountriesVal();
        String yearVal = movieclass.getYearVal();
        String sortVal = movieclass.getSortVal();
        String yearVal1, yearVal2;
        if(genresVal.equals("全部类型"))
            genresVal = "";
        if(countriesVal.equals("全部地区"))
            countriesVal = "";
        switch(yearVal){
            case "2020":
                yearVal1 = "2020";yearVal2 = "2020";break;
            case "2019":
                yearVal1 = "2019";yearVal2 = "2019";break;
            case "2010年代":
                yearVal1 = "2010";yearVal2 = "2019";break;
            case "2000年代":
                yearVal1 = "2000";yearVal2 = "2009";break;
            case "90年代":
                yearVal1 = "1990";yearVal2 = "1999";break;
            case "80年代":
                yearVal1 = "1980";yearVal2 = "1989";break;
            case "70年代":
                yearVal1 = "1970";yearVal2 = "1979";break;
            case "60年代":
                yearVal1 = "1960";yearVal2 = "1969";break;
            case "更早":
                yearVal1 = "0";yearVal2 = "1959";break;
            case "全部年代":
                yearVal1 = "0";yearVal2 = "99999";break;
            default:
                yearVal1 = "0";yearVal2 = "999999";break;
        }
        switch(sortVal){
            case "豆瓣高分":
                sortVal = "e.rating desc";break;
            case "最新上映":
                sortVal = "e.year desc";break;
            case "热门电影":
                sortVal = "e.ratingsCount desc";break;
            default:
                sortVal = "e.rating desc";break;
        }

        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryByChoice(genresVal,countriesVal,yearVal1,yearVal2,sortVal);
        return mysqlBlogs;
    }

    // 模糊查询
    @PostMapping("/search")
    public Object search(@RequestBody Param param){
        Map<String, Object> map = new HashMap<>();
        // 统计耗时
        StopWatch watch = new StopWatch();
        watch.start();
        String type = param.getType();
        // mysql 的搜索
        if (MYSQL.equals(type)) {
            List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryBlog(param.getKeyword());
            map.put("list", mysqlBlogs);
        }
        // es 的搜索
        else if (ES.equals(type)) {
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.should(QueryBuilders.matchPhraseQuery("title", param.getKeyword()));
            builder.should(QueryBuilders.matchPhraseQuery("summary", param.getKeyword()));
            String s = builder.toString();
            Page<EsBlog> search = (Page<EsBlog>) esBlogRepository.search(builder);
            List<EsBlog> content = search.getContent();
            map.put("list", content);
        } else {
            return "你要啥呢小老弟";
        }
        watch.stop();
        // 计算耗时
        long timeMillis = watch.getTotalTimeMillis();
        map.put("duration", timeMillis);
        return map;
    }

    @Data
    public static class user{
        // mysql, es
        private String userid;
        private String userpwd;
    }

    @Data
    public static class record{
        // mysql, es
        private String userid;
        private String movieid;
    }

    @Data
    public static class Param{
        // mysql, es
        private String type;
        private String keyword;
    }

    @Data
    public static class movieclass{
//        String genresVal,String countriesVal,String yearVal,String sortVal
        private String genresVal;
        private String countriesVal;
        private String yearVal;
        private String sortVal;
    }
}
