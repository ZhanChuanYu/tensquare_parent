package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@RestController
@RequestMapping("/friend")
@CrossOrigin
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 添加好友
     * @param friendid     好友id
     * @param type         加粉 还是 拉黑
     * @return
     */
    @RequestMapping(value="/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result addFriend(@PathVariable("friendid") String friendid, @PathVariable("type") String type, HttpServletRequest request){
        //1.取出请求域中claims
        Claims claims = (Claims)request.getAttribute("user_claims");
        if(claims == null){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }
        //2.取出用户id，去添加
        if("1".equals(type)){//此时是添加好友
            int res = friendService.addFriend(claims.getId(),friendid);
            if(res<1){
                return new Result(false, StatusCode.ERROR,"添加失败");
            }
        }else{//此时是添加非好友
            friendService.addNoFriend(claims.getId(),friendid);
        }

        return new Result(true, StatusCode.OK,"添加成功");
    }


    /**
     * 添加好友
     * @param friendid     好友id
     * @return
     */
    @RequestMapping(value="/{friendid}",method = RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable("friendid") String friendid, HttpServletRequest request){
        //1.取出请求域中claims
        Claims claims = (Claims)request.getAttribute("user_claims");
        if(claims == null){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }
        //2.取出用户id，去删除
         friendService.deleteFriend(claims.getId(),friendid);

        return new Result(true, StatusCode.OK,"删除成功");
    }
}
