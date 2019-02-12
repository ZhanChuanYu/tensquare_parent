package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * 交友的持久层接口
 */
public interface FriendDao extends JpaRepository<Friend,String> {

    /**
     * 查询当前用户是否关注过friendid
     * @param userid
     * @param friendid
     * @return
     */
    @Query("select count(f) from Friend f where f.userid = ?1 and f.friendid = ?2 ")
    int findFriendCount(String userid,String friendid);

    /**
     * 更新islike的值
     * @param userid
     * @param friendid
     * @param islike       它的取值不固定，取关也是此方法，只不过传值是0
     */
    @Modifying
    @Query("update Friend  set islike = ?3 where userid = ?1 and friendid = ?2 ")
    void updateIslike(String userid,String friendid,String islike);

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Modifying
    @Query("delete from Friend where userid = ?1 and friendid = ?2")
    void deleteFriend(String userid,String friendid);
}
