package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * 吐槽操作的持久层接口
 */
public interface SpitDao extends MongoRepository<Spit,String>{

    /**
     *  根据上级id查询吐槽，查询吐槽的回复
     * @param parentid
     * @param pageable
     * @return
     */
    Page<Spit> findByParentid(String parentid, Pageable pageable);
}
