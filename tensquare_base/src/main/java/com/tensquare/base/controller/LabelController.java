package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * 标签的控制器
 */
@RestController
@RequestMapping("/label")
@CrossOrigin//开启跨域访问的支持
@RefreshScope//刷新配置
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * 查询所有标签
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
//        int i = 1/0;
        //1.查询所有标签
        List<Label> labels = labelService.findAll();
        //2.返回结果
        return new Result(true, StatusCode.OK,"查询成功",labels);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}",method=RequestMethod.GET)
    public Result findById(@PathVariable("id") String id){
        System.out.println("base 微服务的 findById执行了 +  第二个微服务执行的");
        //1.根据id查询标签
        Label label = labelService.findById(id);
        //2.返回结果
        return new Result(true,StatusCode.OK,"查询成功",label);
    }

    /**
     * 保存
     * @param label
     * @return
     */
    @RequestMapping(method=RequestMethod.POST)
    public Result add(@RequestBody Label label){
        labelService.add(label);
        return new Result(true,StatusCode.OK,"保存成功");
    }


    /**
     * 更新
     * @param label
     * @return
     */
    @RequestMapping(value="/{id}",method=RequestMethod.PUT)
    public Result update(@RequestBody Label label,@PathVariable("id") String id){
        label.setId(id);
        labelService.update(label);
        return new Result(true,StatusCode.OK,"更新成功");
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public Result delete(@PathVariable("id") String id){
        labelService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 条件查询标签列表
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap){
        //条件查询标签列表
        List<Label> labels = labelService.findSearch(searchMap);
        //返回
        return new Result(true,StatusCode.OK,"查询成功",labels);
    }

    /**
     * 条件查询带分页
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value="/search/{page}/{size}",method = RequestMethod.POST)
    public Result findPage(@RequestBody Map searchMap,@PathVariable("page") int page,@PathVariable("size") int size){
        //1.调用业务层查询带有分页的结果集
        Page<Label> labelPage = labelService.findPage(searchMap,page,size);
        //2.创建带有分页的结果对象
        PageResult pageResult = new PageResult(labelPage.getTotalElements(),labelPage.getContent());
        //3.创建返回值对象
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }


    @Value("${sms.ip}")
    private String ip;

    /**
     * 读取码云上的自定义配置
     * @return
     */
    @RequestMapping(value="/testConfig",method = RequestMethod.GET)
    public Result testConfig(){
        return new Result(true,StatusCode.OK,"获取到的ip是："+ip);
    }
}
