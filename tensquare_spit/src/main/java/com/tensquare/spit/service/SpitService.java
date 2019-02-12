package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * 吐槽的业务层类
 */
@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询所有
     * @return
     */
    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    /**
     * 保存操作
     * @param spit
     */
    public void add(Spit spit){
        //设置id
        spit.set_id(String.valueOf(idWorker.nextId()));
        //实现保存
        spitDao.save(spit);
    }

    /**
     * 更新操作
     * @param spit
     */
    public void update(Spit spit){
        spitDao.save(spit);
    }

    /**
     * 删除操作
     * @param id
     */
    public void delete(String id){
        spitDao.deleteById(id);
    }

    /**
     * 根据上级id查询吐槽信息（就是查询吐槽的回复）
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findByParentid(String parentid, int page,int size){
        //1.创建分页对象
        PageRequest pageRequest = PageRequest.of(page-1,size);
        //2.执行查询并返回
        return spitDao.findByParentid(parentid,pageRequest);
    }


    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     *  实现点赞功能
     */
    public void updateThumbup(String id){
        //1.创建查询对象
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));//update({_id:id})
        //2.创建更新对象
        Update update = new Update();
        update.inc("thumbup",1);//update({_id:id},{$inc:{thumbup:NumberInt(1)}})
        //3.实现更新
        mongoTemplate.updateFirst(query,update,"spit");
    }


}
