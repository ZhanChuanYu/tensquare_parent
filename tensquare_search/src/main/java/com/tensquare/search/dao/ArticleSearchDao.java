package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * 文章搜索的持久层接口
 */
public interface ArticleSearchDao extends ElasticsearchRepository<Article,String>{

    /**
     * 根据文章标题或者内容模糊查询 带分页
     * @param title
     * @param content
     * @param pageable
     * @return
     */
    Page<Article>  findByTitleOrContentLike(String title, String content, Pageable pageable);
}
