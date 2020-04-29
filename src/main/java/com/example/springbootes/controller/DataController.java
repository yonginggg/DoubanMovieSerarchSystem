package com.example.springbootes.controller;

import com.example.springbootes.entity.es.EsBlog;
import com.example.springbootes.entity.mysql.MysqlBlog;
import com.example.springbootes.repository.es.EsBlogRepository;
import com.example.springbootes.repository.mysql.MysqlBlogRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
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
