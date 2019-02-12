package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{

    /**
     * 审核操作
     * @param id
     */
    @Modifying
    @Query("update Article  set state = 1 where id = ?1 ")
    void examine(String id);

    /**
     * 点赞操作
     * @param id
     */
    @Modifying
    @Query(value="update tb_article set thumbup = (case when thumbup is null then 1 else thumbup+1 end) where id = ?1 ",nativeQuery = true)
    void updateThumbup(String id);
}
