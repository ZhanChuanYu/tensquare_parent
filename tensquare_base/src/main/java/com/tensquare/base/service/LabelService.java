package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * 标签的业务类
 */
@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询所有标签
     * @return
     */
    public List<Label> findAll(){
        return labelDao.findAll();
    }

    /**
     * 根据id查询一个标签
     * @param id
     * @return
     */
    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    /**
     * 保存
     * @param label
     */
    public void add(Label label){
        label.setId(String.valueOf(idWorker.nextId()));
        labelDao.save(label);
    }

    /**
     * 更新
     * @param label
     */
    public void update(Label label){
        labelDao.save(label);
    }

    /**
     * 根据id删除
     * @param id
     */
    public void delete(String id){
        labelDao.deleteById(id);
    }

    /**
     * findByCondition 根据条件查询
     * findBySearch是为了和控制器中的方法名称一致
     * 以上两个方法命名都可以
     * @return
     *
     * Map<String,Object>
     *     属性名称,属性的值
     */
    public List<Label> findSearch(Map searchMap){
        //创建查询条件对象
        Specification spec = createSpecification(searchMap);
        //执行查询并返回
        return labelDao.findAll(spec);
    }

    /**
     * 条件查询带分页
     * @param searchMap     查询的条件
     * @param page          当前页 从0开始
     * @param size          每次查询的记录条数
     * @return
     */
    public Page<Label> findPage(Map searchMap, int page, int size){
        //创建查询条件对象
        Specification spec = createSpecification(searchMap);
        //创建分页对象
        PageRequest pageRequest = PageRequest.of(page-1,size);
        //返回查询结果
        return labelDao.findAll(spec,pageRequest);
    }




    /**
     * 创建查询条件
     * @param searchMap
     * @return
     */
    private Specification<Label> createSpecification(Map searchMap){
       return  new Specification<Label>() {
            /**
             * JPA中的五种查询方式
             * OID
             * JPQL
             * SQL
             * QBC
             * 对象导航查询
             *
             * @param root  它就是lable对象（它是哪个对象是由创建时的泛型决定的）
             * @param cq 它是使用Query By Criteria（QBC查询）方式查询的查询对象
             * @param cb 它是用于构建查询条件的对象
             * @return
             */
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {


                //定义封装查询条件的对象
                List<Predicate> predicates = new ArrayList<>();

                //labelname 判断是否输入了标签名称
                if(searchMap.get("labelname") != null && !"".equals(searchMap.get("labelname"))){
                    //模糊查询
                    Predicate p1 = cb.like(root.get("labelname").as(String.class),"%"+searchMap.get("labelname")+"%");
                    predicates.add(p1);
                }
                //state 判断是否输入了标签状态，0不可用，1可用
                if(searchMap.get("state") != null && !"".equals(searchMap.get("labelname"))){
                    //精确查询
                    Predicate p2 = cb.equal(root.get("state"),searchMap.get("state"));
                    predicates.add(p2);
                }
                //count 判断是否输入了使用数量
                if(searchMap.get("count") != null ){
                    //精确查询
                    Predicate  p3 = cb.equal(root.get("count"),searchMap.get("count"));
                    predicates.add(p3);
                }
                //recommend 判断是否输入了是否推荐  0不推荐，1推荐
                if(searchMap.get("recommend") != null && !"".equals(searchMap.get("recommend"))){
                    //精确查询
                    Predicate p4 = cb.equal(root.get("recommend"),searchMap.get("recommend"));
                    predicates.add(p4);
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));//p1 and p2 and p3 and p4

            }
        };
    }
}
