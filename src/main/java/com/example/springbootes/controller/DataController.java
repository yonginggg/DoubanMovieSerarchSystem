//package com.example.springbootes.controller;
//
//import com.example.springbootes.entity.es.EsBlog;
//import com.example.springbootes.entity.mysql.MysqlBlog;
//import com.example.springbootes.entity.mysql.MysqlRecord;
//import com.example.springbootes.entity.mysql.MysqlUser;
//import com.example.springbootes.repository.es.EsBlogRepository;
//import com.example.springbootes.repository.mysql.MysqlBlogRepository;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.util.DigestUtils;
//import org.springframework.util.StopWatch;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.*;
//import java.util.regex.Pattern;
//import java.util.regex.Matcher;
//@RestController
//@Slf4j
//public class DataController {
//    private static final String MYSQL = "mysql";
//    private static final String ES = "es";
//
//    @Autowired
//    private MysqlBlogRepository mysqlBlogRepository;
//    @Autowired
//    private EsBlogRepository esBlogRepository;
//
//    // 注册
//    @PostMapping("/register")
//    public Object register(@RequestBody user user){
//        Map<Integer, String> map = new HashMap<>();
//        String userid = user.getUserid();
//        String userpwd = user.getUserpwd();
//        String md5Pwd = DigestUtils.md5DigestAsHex(userpwd.getBytes());
//
//        List<MysqlUser> mysqlUser = mysqlBlogRepository.queryUser(userid);
//        if(mysqlUser.size() != 0){
//            map.put(401,"账号已存在！");
//        }
//        else if (userid.equals("") || userpwd.equals("")) {
//            map.put(401,"请完善账户信息！");
//        }else {
//            int register = mysqlBlogRepository.register(userid, md5Pwd);
//            map.put(200,"注册成功!");
//        }
//        return map;
//    }
//
//    // 登陆
//    @PostMapping("/login")
//    public Object login(@RequestBody user user){
//        Map<Integer, String> map = new HashMap<>();
//        String userid = user.getUserid();
//        String userpwd = user.getUserpwd();
//        String md5Pwd = DigestUtils.md5DigestAsHex(userpwd.getBytes());
//
//        List<MysqlUser> login = mysqlBlogRepository.login(userid, md5Pwd);
//        if (userid.equals("") || userpwd.equals("")) {
//            map.put(401,"账号密码不得为空！");
//        } else if(login.size() == 0){
//            map.put(401,"账号或密码错误！");
//        } else {
//            map.put(200,"登录成功!");
//        }
//        return map;
//    }
//
//    // 添加浏览记录（当用户点击某一搜索记录时）
//    @PostMapping("/record")
//    public Object record(@RequestBody record record){
//        String userid = record.getUserid();
//        String movieId = record.getMovieid();
//        int recordOrder = 0;
//        int MaxOrder = 10;
//        Date now = new Date();
//
//        Integer MaxRecordOrder = mysqlBlogRepository.queryMaxRecordOrder(userid);
//        // 查询是否已存在该电影的搜索记录
//        List<MysqlRecord> mysqlRecords = mysqlBlogRepository.queryRecord(userid);
//        if(mysqlRecords.size()>0){
//            for(int i=0;i<mysqlRecords.size();i++){
//                if(movieId.equals(mysqlRecords.get(i).getMovieId()))
//                    recordOrder = mysqlRecords.get(i).getRecordOrder();
//            }
//        }
//
//        if (MaxRecordOrder == null){
//            mysqlBlogRepository.insertRecord(userid, 1,movieId,now);
//        } else if (recordOrder != 0){   // 已存在该电影的搜索记录
//            mysqlBlogRepository.deleteRecordOrder(userid,recordOrder);
//            mysqlBlogRepository.insertRecord(userid, recordOrder,movieId,now);
//        } else if(MaxRecordOrder >= MaxOrder){  //最多保存10条
//            mysqlBlogRepository.deleteRecordOrder(userid,MaxRecordOrder-9);
//            mysqlBlogRepository.insertRecord(userid, MaxRecordOrder+1,movieId,now);
//        }
//        else
//            mysqlBlogRepository.insertRecord(userid, MaxRecordOrder+1,movieId,now);
//        return "insert record successful";
//    }
//
//    // 电影根据评分排序
//    @GetMapping("/blogs")
//    public Object blog(){
//        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryAll();
//        return mysqlBlogs;
//    }
//
//    @GetMapping("/movie/{id}")
//    public Object movie(@PathVariable("id") String movieID){
//        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryByMovieID(movieID);
//        MysqlBlog blog = mysqlBlogs.get(0);
//        return blog;
//    }
//
//    // 根据条件选择筛选
//    @PostMapping("/choice")
//    public Object choice(@RequestBody movieclass movieclass){
//        String genresVal = movieclass.getGenresVal();
//        String countriesVal = movieclass.getCountriesVal();
//        String yearVal = movieclass.getYearVal();
//        String sortVal = movieclass.getSortVal();
//        String yearVal1, yearVal2;
//        if(genresVal.equals("全部类型"))
//            genresVal = "";
//        if(countriesVal.equals("全部地区"))
//            countriesVal = "";
//        switch(yearVal){
//            case "2020":
//                yearVal1 = "2020";yearVal2 = "2020";break;
//            case "2019":
//                yearVal1 = "2019";yearVal2 = "2019";break;
//            case "2010年代":
//                yearVal1 = "2010";yearVal2 = "2019";break;
//            case "2000年代":
//                yearVal1 = "2000";yearVal2 = "2009";break;
//            case "90年代":
//                yearVal1 = "1990";yearVal2 = "1999";break;
//            case "80年代":
//                yearVal1 = "1980";yearVal2 = "1989";break;
//            case "70年代":
//                yearVal1 = "1970";yearVal2 = "1979";break;
//            case "60年代":
//                yearVal1 = "1960";yearVal2 = "1969";break;
//            case "更早":
//                yearVal1 = "0";yearVal2 = "1959";break;
//            case "全部年代":
//                yearVal1 = "0";yearVal2 = "99999";break;
//            default:
//                yearVal1 = "0";yearVal2 = "999999";break;
//        }
//        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryByChoiceRating(genresVal,countriesVal,yearVal1,yearVal2);
//        switch(sortVal){
//            case "豆瓣高分":
//                mysqlBlogs = mysqlBlogRepository.queryByChoiceRating(genresVal,countriesVal,yearVal1,yearVal2);
//            case "最新上映":
//                mysqlBlogs = mysqlBlogRepository.queryByChoiceYear(genresVal,countriesVal,yearVal1,yearVal2);
//            case "热门电影":
//                mysqlBlogs = mysqlBlogRepository.queryByChoiceRatingCount(genresVal,countriesVal,yearVal1,yearVal2);
//            default:
//        }
//        return mysqlBlogs;
//    }
//
//    // 模糊查询
//    @PostMapping("/search")
//    public Object search(@RequestBody Param param){
//        Map<String, Object> map = new HashMap<>();
//        // 统计耗时
//        StopWatch watch = new StopWatch();
//        watch.start();
//        String type = param.getType();
//        // mysql 的搜索
//        if (MYSQL.equals(type)) {
//            List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryBlog(param.getKeyword());
//            map.put("list", mysqlBlogs);
//        }
//        // es 的搜索
//        else if (ES.equals(type)) {
//            BoolQueryBuilder builder = QueryBuilders.boolQuery();
//            builder.should(QueryBuilders.matchPhraseQuery("title", param.getKeyword()));
//            builder.should(QueryBuilders.matchPhraseQuery("summary", param.getKeyword()));
//            String s = builder.toString();
//            Page<EsBlog> search = (Page<EsBlog>) esBlogRepository.search(builder);
//            List<EsBlog> content = search.getContent();
//            map.put("list", content);
//        } else {
//            return "你要啥呢小老弟";
//        }
//        watch.stop();
//        // 计算耗时
//        long timeMillis = watch.getTotalTimeMillis();
//        map.put("duration", timeMillis);
//        return map;
//    }
//
//    // 根据电影类别推荐
//    @GetMapping("/genreCommand/{id}")
//    public Object genreCommand(@PathVariable("id") String userid){
//        List<MysqlRecord> mysqlRecords = mysqlBlogRepository.queryRecord(userid);
//        // 电影ID列表
//        List<String> MovieIDList = new ArrayList<String>();
//        for(int i=0;i<mysqlRecords.size();i++){
//            MovieIDList.add(mysqlRecords.get(i).getMovieId());
//        }
//        String genres = new String();
//        String casts = new String();
//
//        for(int i=0;i<MovieIDList.size();i++){
//            String genre = mysqlBlogRepository.queryByMovieID(MovieIDList.get(i)).get(0).getGenres();
//            genre = genre.replace("[", "").replace("]","，").replace("'","").replace(" ","");
//            genres = genres + genre;
//
//            String cast = mysqlBlogRepository.queryByMovieID(MovieIDList.get(i)).get(0).getCasts();
//            cast = cast.replace("[", "").replace("]","，").replace("'","").replace(" ","");
//            casts = casts + cast;
//        }
//        String[] genreSplit = genres.split("，");
//        String[] castSplit = casts.split("，");
//
//
//        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryByChoiceRating(st(Arrays.asList(genreSplit)),"","0","999999");
//        List<MysqlBlog> genreBlogs = mysqlBlogs;
//        if (mysqlBlogs.size()>5) {
//            genreBlogs = TOP5(mysqlBlogs);
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("type",st(Arrays.asList(genreSplit)));
//        map.put("list", genreBlogs);
//        return map;
//    }
//
//    // 根据主演推荐
//    @GetMapping("/castCommand/{id}")
//    public Object castCommand(@PathVariable("id") String userid){
//        List<MysqlRecord> mysqlRecords = mysqlBlogRepository.queryRecord(userid);
//        // 电影ID列表
//        List<String> MovieIDList = new ArrayList<String>();
//        for(int i=0;i<mysqlRecords.size();i++){
//            MovieIDList.add(mysqlRecords.get(i).getMovieId());
//        }
//
//        String casts = new String();
//
//        for(int i=0;i<MovieIDList.size();i++){
//            String cast = mysqlBlogRepository.queryByMovieID(MovieIDList.get(i)).get(0).getCasts();
//            cast = cast.replace("[", "").replace("]","，").replace("'","").replace(" ","");
//            casts = casts + cast;
//        }
//        String[] castSplit = casts.split("，");
//        BoolQueryBuilder builder = QueryBuilders.boolQuery();
//        builder.should(QueryBuilders.matchPhraseQuery("casts", st(Arrays.asList(castSplit))));
//        String s = builder.toString();
//        Page<EsBlog> search = (Page<EsBlog>) esBlogRepository.search(builder);
//        List<EsBlog> castBlogs = search.getContent();
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("type",st(Arrays.asList(castSplit)));
//        map.put("list", castBlogs);
//        return map;
//    }
//
//    // 根据导演推荐
//    @GetMapping("/directorCommand/{id}")
//    public Object directorCommand(@PathVariable("id") String userid){
//        List<MysqlRecord> mysqlRecords = mysqlBlogRepository.queryRecord(userid);
//        // 电影ID列表
//        List<String> MovieIDList = new ArrayList<String>();
//        for(int i=0;i<mysqlRecords.size();i++){
//            MovieIDList.add(mysqlRecords.get(i).getMovieId());
//        }
//
//        String directors = new String();
//
//        for(int i=0;i<MovieIDList.size();i++){
//            String director = mysqlBlogRepository.queryByMovieID(MovieIDList.get(i)).get(0).getDirectors();
//            directors = directors + "，" + director;
//        }
//        String[] directorSplit = directors.split("，");
//        BoolQueryBuilder builder = QueryBuilders.boolQuery();
//        builder.should(QueryBuilders.matchPhraseQuery("directors", st(Arrays.asList(directorSplit))));
//        String s = builder.toString();
//        Page<EsBlog> search = (Page<EsBlog>) esBlogRepository.search(builder);
//        List<EsBlog> directorBlogs = search.getContent();
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("reason",st(Arrays.asList(directorSplit)));
//        map.put("list", directorBlogs);
//        return map;
//    }
//
//    // 获取前五
//    public static List<MysqlBlog> TOP5( List<MysqlBlog> items ){
//        List<MysqlBlog> a = new ArrayList<MysqlBlog>();
//        for(int i=0;i<5;i++){
//            a.add(items.get(i));
//        }
//        return a;
//    }
//
//    // list转map
//    public static Map<String,Integer> frequencyOfListElements( String[] items ) {
//        if (items == null || items.length == 0) return null;
//        Map<String, Integer> map = new HashMap<String, Integer>();
//        for (String temp : items) {
//            Integer count = map.get(temp);
//            map.put(temp, (count == null) ? 1 : count + 1);
//        }
//        return map;
//    }
//
//    // 获取list中最多出现的元素及次数
//    public String st(List<String> list) {
//        String regex;
//        Pattern p;
//        Matcher m;
//
//        String tmp = "";
//        String tot_str = list.toString();
//        //System.out.println(tot_str);   //[aa, aa, aa, aa, bb, bb, cc, cc, dd, ed]
//        int max_cnt = 0;
//        String max_str = "";
//        for (String str : list) {
//            if (tmp.equals(str)) continue;
//            tmp = str;
//            regex = str;
//            p = Pattern.compile(regex);
//            m = p.matcher(tot_str);
//            int cnt = 0;
//            while (m.find()) {
//                cnt++;
//            }
//            //System.out.println(str + ":" + cnt);
//            if (cnt > max_cnt) {
//                max_cnt = cnt;
//                max_str = str;
//            }
//        }
//        return max_str;
//    }
//
//    @Data
//    public static class user{
//        // mysql, es
//        private String userid;
//        private String userpwd;
//    }
//
//    @Data
//    public static class record{
//        // mysql, es
//        private String userid;
//        private String movieid;
//    }
//
//    @Data
//    public static class Param{
//        // mysql, es
//        private String type;
//        private String keyword;
//    }
//
//    @Data
//    public static class movieclass{
//        private String genresVal;
//        private String countriesVal;
//        private String yearVal;
//        private String sortVal;
//    }
//}

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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            int register = mysqlBlogRepository.register(userid, md5Pwd,"空");
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
            map.put(401,"账号密码不得为空!");
        } else if(login.size() == 0){
            map.put(401,"账号或密码错误!");
        } else {
            map.put(200,"登录成功!");
        }
        return map;
    }

    // 判断是否有搜索记录
    @GetMapping("/queryRecord/{id}")
    public Object queryRecord(@PathVariable("id") String userid){
        Map<Integer, String> map = new HashMap<>();
        List<MysqlRecord> MysqlRecord = mysqlBlogRepository.queryRecord(userid);
        if(MysqlRecord.size() == 0){
            map.put(401,"无搜索记录");
        }
        else {
            map.put(200,"有搜索记录");
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
    @GetMapping("/movie/{id}")
    public Object movie(@PathVariable("id") String movieID){
        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryByMovieID(movieID);
        MysqlBlog blog = mysqlBlogs.get(0);
        return blog;
    }

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
        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryByChoiceRating(genresVal,countriesVal,yearVal1,yearVal2);
        switch(sortVal){
            case "豆瓣高分":
                mysqlBlogs = mysqlBlogRepository.queryByChoiceRating(genresVal,countriesVal,yearVal1,yearVal2);break;
            case "最新上映":
                mysqlBlogs = mysqlBlogRepository.queryByChoiceYear(genresVal,countriesVal,yearVal1,yearVal2);break;
            case "热门电影":
                mysqlBlogs = mysqlBlogRepository.queryByChoiceRatingCount(genresVal,countriesVal,yearVal1,yearVal2);break;
            default:
        }
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
            builder.should(QueryBuilders.matchPhraseQuery("title", param.getKeyword()));        //中文名
            builder.should(QueryBuilders.matchPhraseQuery("summary", param.getKeyword()));      //简介
            builder.should(QueryBuilders.matchPhraseQuery("directors", param.getKeyword()));    //导演
            builder.should(QueryBuilders.matchPhraseQuery("casts", param.getKeyword()));        //主演
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

    // 历史记录
    @GetMapping("/history/{id}")
    public Object history(@PathVariable("id") String userid){
        List<MysqlRecord> mysqlRecords = mysqlBlogRepository.queryRecord(userid);
        // 电影ID列表
        List<String> MovieIDList = new ArrayList<String>();
        for(int i=0;i<mysqlRecords.size();i++){
            MovieIDList.add(mysqlRecords.get(i).getMovieId());
        }

        ArrayList<MysqlBlog> mysqlBlogs = new ArrayList<>();

        for(int i=0;i<MovieIDList.size();i++){
            MysqlBlog mysqlBlog = mysqlBlogRepository.queryByMovieID(MovieIDList.get(i)).get(0);
            mysqlBlogs.add(mysqlBlog);
        }

        return mysqlBlogs;
    }

    // 默认推荐
    @GetMapping("/indexCommand")
    public Object indexCommand(){
        List<String> MovieIDList = new ArrayList<String>();
        MovieIDList.add("1292052");
        MovieIDList.add("26683290");
        MovieIDList.add("30176393");
        MovieIDList.add("27010768");
        MovieIDList.add("2124724");

        ArrayList<MysqlBlog> mysqlBlogs = new ArrayList<>();

        for(int i=0;i<MovieIDList.size();i++){
            MysqlBlog mysqlBlog = mysqlBlogRepository.queryByMovieID(MovieIDList.get(i)).get(0);
            mysqlBlogs.add(mysqlBlog);
        }
        return mysqlBlogs;
    }

    // 同标签的其他用户的记录来推荐
    @GetMapping("/labelCommand/{id}")
    public Object labelCommand(@PathVariable("id") String userid){
        List<MysqlUser> mysqlUser = mysqlBlogRepository.queryUser(userid);
        String label = mysqlUser.get(0).getLabel();

        List<MysqlUser> labelUser = mysqlBlogRepository.queryUserByLabel(label,userid);
        int size = labelUser.size();
        // 随机数
        Random r = new Random();
        int x = r.nextInt(size);

        String labelUserId = labelUser.get(x).getUserid();

        List<MysqlRecord> mysqlRecords = mysqlBlogRepository.queryRecord(labelUserId);
        // 电影ID列表
        List<String> MovieIDList = new ArrayList<String>();
        for(int i=0;i<mysqlRecords.size();i++){
            MovieIDList.add(mysqlRecords.get(i).getMovieId());
        }

        ArrayList<MysqlBlog> mysqlBlogs = new ArrayList<>();

        System.out.println(labelUserId);
        for(int i=0;i<MovieIDList.size();i++){
            MysqlBlog mysqlBlog = mysqlBlogRepository.queryByMovieID(MovieIDList.get(i)).get(0);
            mysqlBlogs.add(mysqlBlog);
        }

        return mysqlBlogs;
    }

    // 根据电影类别推荐
    @GetMapping("/genreCommand/{id}")
    public Object genreCommand(@PathVariable("id") String userid){
        List<MysqlRecord> mysqlRecords = mysqlBlogRepository.queryRecord(userid);
        // 电影ID列表
        List<String> MovieIDList = new ArrayList<String>();
        for(int i=0;i<mysqlRecords.size();i++){
            MovieIDList.add(mysqlRecords.get(i).getMovieId());
        }

        String genres = new String();
        String casts = new String();

        for(int i=0;i<MovieIDList.size();i++){
            String genre = mysqlBlogRepository.queryByMovieID(MovieIDList.get(i)).get(0).getGenres();
            genre = genre.replace("[", "").replace("]","，").replace("'","").replace(" ","");
            genres = genres + genre;

            String cast = mysqlBlogRepository.queryByMovieID(MovieIDList.get(i)).get(0).getCasts();
            cast = cast.replace("[", "").replace("]","，").replace("'","").replace(" ","");
            casts = casts + cast;
        }
        String[] genreSplit = genres.split("，");
        String[] castSplit = casts.split("，");


        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryByChoiceRating(st(Arrays.asList(genreSplit)),"","0","999999");
        List<MysqlBlog> genreBlogs = mysqlBlogs;
        if (mysqlBlogs.size()>5) {
            genreBlogs = TOP5(mysqlBlogs);
        }

        // 修改用户标签
        mysqlBlogRepository.updateLabel(userid,st(Arrays.asList(genreSplit)));

        Map<String, Object> map = new HashMap<>();
        map.put("type",st(Arrays.asList(genreSplit)));
        map.put("list", genreBlogs);
        return map;
    }

    // 根据主演推荐
    @GetMapping("/castCommand/{id}")
    public Object castCommand(@PathVariable("id") String userid){
        List<MysqlRecord> mysqlRecords = mysqlBlogRepository.queryRecord(userid);
        // 电影ID列表
        List<String> MovieIDList = new ArrayList<String>();
        for(int i=0;i<mysqlRecords.size();i++){
            MovieIDList.add(mysqlRecords.get(i).getMovieId());
        }

        String casts = new String();

        for(int i=0;i<MovieIDList.size();i++){
            String cast = mysqlBlogRepository.queryByMovieID(MovieIDList.get(i)).get(0).getCasts();
            cast = cast.replace("[", "").replace("]","，").replace("'","").replace(" ","");
            casts = casts + cast;
        }
        String[] castSplit = casts.split("，");
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.should(QueryBuilders.matchPhraseQuery("casts", st(Arrays.asList(castSplit))));
        String s = builder.toString();
        Page<EsBlog> search = (Page<EsBlog>) esBlogRepository.search(builder);
        List<EsBlog> castBlogs = search.getContent();

        Map<String, Object> map = new HashMap<>();
        map.put("type",st(Arrays.asList(castSplit)));
        map.put("list", castBlogs);
        return map;
    }

    // 根据导演推荐
    @GetMapping("/directorCommand/{id}")
    public Object directorCommand(@PathVariable("id") String userid){
        List<MysqlRecord> mysqlRecords = mysqlBlogRepository.queryRecord(userid);
        // 电影ID列表
        List<String> MovieIDList = new ArrayList<String>();
        for(int i=0;i<mysqlRecords.size();i++){
            MovieIDList.add(mysqlRecords.get(i).getMovieId());
        }

        String directors = new String();

        for(int i=0;i<MovieIDList.size();i++){
            String director = mysqlBlogRepository.queryByMovieID(MovieIDList.get(i)).get(0).getDirectors();
            directors = directors + "，" + director;
        }
        String[] directorSplit = directors.split("，");
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.should(QueryBuilders.matchPhraseQuery("directors", st(Arrays.asList(directorSplit))));
        String s = builder.toString();
        Page<EsBlog> search = (Page<EsBlog>) esBlogRepository.search(builder);
        List<EsBlog> directorBlogs = search.getContent();

        Map<String, Object> map = new HashMap<>();
        map.put("reason",st(Arrays.asList(directorSplit)));
        map.put("list", directorBlogs);
        return map;
    }

    // 获取前五
    public static List<MysqlBlog> TOP5( List<MysqlBlog> items ){
        List<MysqlBlog> a = new ArrayList<MysqlBlog>();
        for(int i=0;i<5;i++){
            a.add(items.get(i));
        }
        return a;
    }

    // list转map
    public static Map<String,Integer> frequencyOfListElements( String[] items ) {
        if (items == null || items.length == 0) return null;
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String temp : items) {
            Integer count = map.get(temp);
            map.put(temp, (count == null) ? 1 : count + 1);
        }
        return map;
    }

    // 获取list中最多出现的元素及次数
    public String st(List<String> list) {
        String regex;
        Pattern p;
        Matcher m;

        String tmp = "";
        String tot_str = list.toString();
        //System.out.println(tot_str);   //[aa, aa, aa, aa, bb, bb, cc, cc, dd, ed]
        int max_cnt = 0;
        String max_str = "";
        for (String str : list) {
            if (tmp.equals(str)) continue;
            tmp = str;
            regex = str;
            p = Pattern.compile(regex);
            m = p.matcher(tot_str);
            int cnt = 0;
            while (m.find()) {
                cnt++;
            }
            //System.out.println(str + ":" + cnt);
            if (cnt > max_cnt) {
                max_cnt = cnt;
                max_str = str;
            }
        }
        return max_str;
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
        // String genresVal,String countriesVal,String yearVal,String sortVal
        private String genresVal;
        private String countriesVal;
        private String yearVal;
        private String sortVal;
    }

}

