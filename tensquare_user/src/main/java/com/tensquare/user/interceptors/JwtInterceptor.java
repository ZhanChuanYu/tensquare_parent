package com.tensquare.user.interceptors;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * 自定义拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 放行的方法
     * @param request
     * @param response
     * @param handler
     * @return   true就放行  false就不放行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //根据之前控制器中方法，来获取Claims，并且把Claims存入请求域中
        //获取请求消息头
        String header = request.getHeader("Authorization");
        //1.有消息头
        if(header != null){
            //2.判断消息头是否符合规则
            if(header.startsWith("Bearer ")) {
                //3.取出token
                String token = header.split(" ")[1];
                //4.解析token
                Claims claims = jwtUtil.parseJWT(token);
                if(claims != null){
                    //5.取出自定义的部分：roles
                    if("admin".equals(claims.get(("roles")))){
                        request.setAttribute("admin_claims",claims);//管理员权限
                    }
                    if("user".equals(claims.get(("roles")))){
                        request.setAttribute("user_claims",claims);//用户权限
                    }
                }
            }
        }
        return true;
    }
}
