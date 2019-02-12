package com.tensquare.friend.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@FeignClient("tensquare-user")
public interface UserClient {

    /**
     * 更新粉丝数量
     * @param userid
     * @param amount
     */
    @RequestMapping(value="/user/incfans/{userid}/{amount}",method= RequestMethod.PUT)
    Result incUserFans(@PathVariable("userid") String userid, @PathVariable("amount") int amount);


    /**
     * 更新关注数量
     * @param userid
     * @param amount
     */
    @RequestMapping(value="/user/incfollowcount/{userid}/{amount}",method=RequestMethod.PUT)
    Result incUserFollowCount(@PathVariable("userid") String userid,@PathVariable("amount") int amount);
}
