package com.tensquare.user.controller;

import com.tensquare.user.pojo.Admin;
import com.tensquare.user.service.AdminService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",adminService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",adminService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Admin> pageList = adminService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Admin>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",adminService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param admin
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Admin admin  ){
		adminService.add(admin);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param admin
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Admin admin, @PathVariable String id ){
		admin.setId(id);
		adminService.update(admin);		
		return new Result(true,StatusCode.OK,"修改成功");
	}


	/**
	 * 删除
	 * @param id
	 *
	 * Bearer token
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id, HttpServletRequest request){
		//1.获取请求域中的claims
		Claims claims = (Claims)request.getAttribute("admin_claims");
		if(claims == null){
			return new Result(false,StatusCode.ACCESSERROR,"没有权限");
		}


		//获取用户的Token，解析token，得到claims信息，判断登陆者是否具备删除权限
		adminService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	/**
	 * 删除
	 * @param id
	 *
	 * Bearer token

	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id,@RequestHeader(value="Authorization",required = false) String header){
		//1.没有消息头，不让删
		if(header == null){
			return new Result(false,StatusCode.ACCESSERROR,"没有权限1");
		}
		//2.判断消息头是否符合规则
		if(!header.startsWith("Bearer ")){
			return new Result(false,StatusCode.ACCESSERROR,"没有权限2");
		}
		//3.取出token
		String token = header.split(" ")[1];
		//4.解析token
		Claims claims = jwtUtil.parseJWT(token);
		if(claims == null){
			return new Result(false,StatusCode.ACCESSERROR,"没有权限3");
		}
		//5.取出自定义的部分：roles
		if(!"admin".equals(claims.get(("roles")))){
			return new Result(false,StatusCode.ACCESSERROR,"没有权限4");
		}


		//获取用户的Token，解析token，得到claims信息，判断登陆者是否具备删除权限
		adminService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}*/


	@Autowired
	private JwtUtil jwtUtil;
	/**
	 * 管理员登录
	 * @param admin
	 * @return
	 */
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public Result login(@RequestBody Admin admin){
		Admin sysAdmin = adminService.findByLoginnameAndPassword(admin.getLoginname(),admin.getPassword());
		//判断登录用户是否存在（我们是通过异常处理实现的，所以此时一定存在了）
		//签发Token
		String token = jwtUtil.createJWT(sysAdmin.getId(),sysAdmin.getLoginname(),"admin");
		return new Result(true,StatusCode.OK,"登录成功",token);
	}
}
