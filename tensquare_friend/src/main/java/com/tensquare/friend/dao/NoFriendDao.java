package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * 交友的持久层接口
 */
public interface NoFriendDao extends JpaRepository<NoFriend,String> {

    /**
     * 查询当前用户是否拉黑过friendid
     * @param userid
     * @param friendid
     * @return
     */
    @Query("select count(f) from NoFriend f where f.userid = ?1 and f.friendid = ?2 ")
    int findNoFriendCount(String userid, String friendid);




}
