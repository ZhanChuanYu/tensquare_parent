package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private UserClient userClient;


    /**
     * 添加好友
     * @param userid
     * @param friendid
     * @return
     */
    @Transactional
    public int addFriend(String userid,String friendid){
        //1.判断当前用户是否已经关注过了
        int count = friendDao.findFriendCount(userid,friendid);
        if(count  < 1){//没有关注过,可以关注
            Friend friend = new Friend();
            friend.setUserid(userid);
            friend.setFriendid(friendid);
            friend.setIslike("0");
            friendDao.save(friend);
            //关注数加1，是关注谁，谁的粉丝数+1,所以此处是friendid
            userClient.incUserFans(friendid,1);
            userClient.incUserFollowCount(userid,1);
        }
        //2.判断当前用户是否被关注过（被friendid关注过）
        int inverseCount = friendDao.findFriendCount(friendid,userid);
        if(inverseCount > 0){
            friendDao.updateIslike(userid,friendid,"1");
            friendDao.updateIslike(friendid,userid,"1");
        }
        return 1;
    }

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Transactional
    public void deleteFriend(String userid,String friendid){
        //删除好友
        friendDao.deleteFriend(userid,friendid);
        //同时变成单方面关注（islike状态是0）
        friendDao.updateIslike(friendid,userid,"0");

        userClient.incUserFans(friendid,-1);
        userClient.incUserFollowCount(userid,-1);
    }


    @Autowired
    private NoFriendDao noFriendDao;

    /**
     * 添加非好友
     * @param userid
     * @param friendid
     */
    public void addNoFriend(String userid,String friendid){
        //1.根据用户id和好友id去非好友表中看是否拉黑过
        int count = noFriendDao.findNoFriendCount(userid,friendid);
        if(count>0){
            throw new RuntimeException("已经拉黑了");
        }
        //2.执行保存
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }
}
