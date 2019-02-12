package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{

    /**
     * 根据手机号查询用户
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 更新用户的粉丝数量
     * @param userid    用户的id
     * @param amount    粉丝数量的值，它的值只能是+1或者-1
     */
    @Modifying
    @Query("update User u set u.fanscount = u.fanscount+?2 where u.id = ?1 ")
    void incUserFans(String userid,int amount);


    /**
     * 更新用户的关注数量
     * @param userid    用户的id
     * @param amount    关注数量的值，它的值只能是+1或者-1
     */
    @Modifying
    @Query("update User u set u.followcount = u.followcount+?2 where u.id = ?1 ")
    void incUserFollowCount(String userid,int amount);
}
