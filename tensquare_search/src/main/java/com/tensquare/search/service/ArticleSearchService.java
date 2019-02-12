package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleSearchDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import util.IdWorker;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Service
public class ArticleSearchService {

    @Autowired
    private ArticleSearchDao articleSearchDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 保存操作
     * @param article
     */
    public void add(Article article){
        //设置id
        article.setId(String.valueOf(idWorker.nextId()));
        //实现保存
        articleSearchDao.save(article);
    }

    /**
     * 查询所有
     * @return
     */
    public Iterable<Article> findAll(){
       return articleSearchDao.findAll();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Article findById(String id){
        return articleSearchDao.findById(id).get();
    }

    /**
     * 更新操作
     * @param article
     */
    public void update(Article article){
        articleSearchDao.save(article);
    }

    /**
     * 删除操作
     */
    public void delete(String id){
        articleSearchDao.deleteById(id);
    }

    /**
     * 按照文章的标题或者内容检索文件，带分页
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    public Page<Article> findByTitleOrContentLike(String keywords, int page ,int size){
        //1.创建分页对象
        PageRequest pageRequest = PageRequest.of(page-1,size);
        //2.查询结果并返回
        return articleSearchDao.findByTitleOrContentLike(keywords,keywords,pageRequest);
    }
}


