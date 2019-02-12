package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    /**
     * 查询所有操作
     * @return
     */
    @RequestMapping(method= RequestMethod.GET)
    public Result findAll(){
        List<Spit> spits = spitService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",spits);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id){
        Spit spit = spitService.findById(id);
        return new Result(true, StatusCode.OK,"查询成功",spit);
    }

    /**
     * 保存操作
     * @param spit
     * @return
     */
    @RequestMapping(method=RequestMethod.POST)
    public Result add(@RequestBody Spit spit){
        spitService.add(spit);
        return new Result(true, StatusCode.OK,"操作成功");
    }


    /**
     * 更新操作
     * @param spit
     * @return
     */
    @RequestMapping(value="/{id}",method=RequestMethod.PUT)
    public Result update(@PathVariable("id")String id,@RequestBody Spit spit){
        spit.set_id(id);
        spitService.update(spit);
        return new Result(true, StatusCode.OK,"操作成功");
    }


    /**
     * 删除操作
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public Result update(@PathVariable("id")String id){
        spitService.delete(id);
        return new Result(true, StatusCode.OK,"操作成功");
    }

    /**
     * 根据上级id查询吐槽信息
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value="/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentid(@PathVariable("parentid") String parentid,@PathVariable("page") int page,@PathVariable("size") int size){
        //1.获取分页的结果集
        Page<Spit> spitPage = spitService.findByParentid(parentid,page,size);
        //2.创建分页对象
        PageResult pageResult = new PageResult(spitPage.getTotalElements(), spitPage.getContent());
        //3.创建返回值对象，并返回
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }


    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 点赞操作
     * @param id
     * @return
     */
    @RequestMapping(value="/thumbup/{id}",method = RequestMethod.PUT)
    public Result updateThumbup(@PathVariable("id") String id){

        String userid = "9999";//此时先写死，后面会改成取出来的


        //判断当前用户是否已经点过赞了，如果点过，就不能让其再次点赞。
        //判断的依据就是去redis中获取有没有此用户对此条吐槽的点赞信息
        Object value = redisTemplate.opsForValue().get("spit_"+userid+"_"+id);
        if(value != null) {
            //缓存中有点赞的信息，就不让再点赞了
            return new Result(false, StatusCode.REPERROR, "请不要重复点赞");
        }
        spitService.updateThumbup(id);
        //把当前用户的点赞信息记录到缓存服务器
        redisTemplate.opsForValue().set("spit_"+userid+"_"+id,"1",30, TimeUnit.DAYS);
        return new Result(true,StatusCode.OK,"点赞成功");
    }
}
