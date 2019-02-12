package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * 文章搜索的控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleSearchController {

    @Autowired
    private ArticleSearchService articleSearchService;

    /**
     * 保存操作
     * @param article
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Article article){
        articleSearchService.add(article);
        return new Result(true, StatusCode.OK,"保存成功");
    }

    /**
     * 按照关键字查询文章，带分页
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value="/search/{keywords}/{page}/{size}",method = RequestMethod.GET)
    public Result findByTitleOrContentLike(@PathVariable("keywords") String keywords,@PathVariable("page") int page ,@PathVariable("size") int size){
        //1.获取带有分页的结果集
        Page<Article> articlePage = articleSearchService.findByTitleOrContentLike(keywords,page,size);
        //2.创建带有分页的封装对象
        PageResult<Article> pageResult = new PageResult<Article>(articlePage.getTotalElements(),articlePage.getContent());
        //3.返回查询结果的封装对象
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }

    /**
     * 查询所有
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        Iterable<Article> articles = articleSearchService.findAll();
        return new Result(true,StatusCode.OK,"查询成功",articles);
    }
}
