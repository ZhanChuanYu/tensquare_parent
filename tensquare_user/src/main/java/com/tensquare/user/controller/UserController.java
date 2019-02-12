package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import java.util.HashMap;
import java.util.Map;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 查询全部数据
	 *
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Result findAll() {
		return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
	}

	/**
	 * 根据ID查询
	 *
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Result findById(@PathVariable String id) {
		return new Result(true, StatusCode.OK, "查询成功", userService.findById(id));
	}

	/**
	 * 分页+多条件查询
	 *
	 * @param searchMap 查询条件封装
	 * @param page      页码
	 * @param size      页大小
	 * @return 分页结果
	 */
	@RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return new Result(true, StatusCode.OK, "查询成功", new PageResult<User>(pageList.getTotalElements(), pageList.getContent()));
	}

	/**
	 * 根据条件查询
	 *
	 * @param searchMap
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap) {
		return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
	}

	/**
	 * 增加
	 *
	 * @param user
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Result add(@RequestBody User user) {
		userService.add(user);
		return new Result(true, StatusCode.OK, "增加成功");
	}

	/**
	 * 修改
	 *
	 * @param user
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id) {
		user.setId(id);
		userService.update(user);
		return new Result(true, StatusCode.OK, "修改成功");
	}

	/**
	 * 删除
	 *
	 * @param id
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Result delete(@PathVariable String id) {
		userService.deleteById(id);
		return new Result(true, StatusCode.OK, "删除成功");
	}

	/**
	 * 发送验证码：
	 * 		它不是实际发送，只是做了存入redis和rabbitmq
	 * @param mobile
	 */
	@RequestMapping(value="/sendsms/{mobile}",method = RequestMethod.POST )
	public Result sendSms(@PathVariable("mobile") String mobile){
		userService.sendSms(mobile);
		return new Result(true,StatusCode.OK,"发送成功");
	}

	/**
	 * 用户注册
	 * @param user
	 * @param code
	 * @return
	 */
	@RequestMapping(value="/register/{code}",method = RequestMethod.POST)
	public Result register(@RequestBody User user ,@PathVariable("code") String code){
		System.out.println(user);
		userService.register(user,code);
		return new Result(true,StatusCode.OK,"注册成功");
	}


	@Autowired
	private JwtUtil jwtUtil;
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public Result login(@RequestBody User user){
		User sysuser = userService.findByMobileAndPassword(user.getMobile(),user.getPassword());
		//判断登录用户是否存在（我们是通过异常处理实现的，所以此时一定存在了）
		//签发Token
		String token = jwtUtil.createJWT(sysuser.getId(),sysuser.getNickname(),"user");
		Map map=new HashMap();
		map.put("token",token);
		map.put("name",user.getNickname());//昵称
		map.put("avatar",user.getAvatar());//头像
		return new Result(true,StatusCode.OK,"登陆成功",map);
	}




	/**
	 * 更新粉丝数量
	 * @param userid
	 * @param amount
	 */
	@RequestMapping(value="/incfans/{userid}/{amount}",method=RequestMethod.PUT)
	public Result incUserFans(@PathVariable("userid") String userid,@PathVariable("amount") int amount){
		userService.incUserFans(userid,amount);
		return new Result(true,StatusCode.OK,"操作成功");
	}


	/**
	 * 更新关注数量
	 * @param userid
	 * @param amount
	 */
	@RequestMapping(value="/incfollowcount/{userid}/{amount}",method=RequestMethod.PUT)
	public Result incUserFollowCount(@PathVariable("userid") String userid,@PathVariable("amount") int amount){
		userService.incUserFollowCount(userid,amount);
		return new Result(true,StatusCode.OK,"操作成功");
	}
}
