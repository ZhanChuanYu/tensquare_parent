package com.tensquare.manager;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 当前过滤器的执行时机（这个值不能瞎写）
     * 是通过返回值的方式返回的。
     * 取值：
     *      pre：在路由之前执行（前置增强）
     *      post：在route或error之后执行。最终增强
     *      route：在执行路由的时候执行。 后置增强
     *      error：当执行产生异常时，过滤器执行    异常增强
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 当有多个Zuul过滤器时，指定当前过滤器的执行时机
     * 0表示第一个执行。数值越大，执行时机越晚
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 当前过滤器的开关。如果返回是true，则开启。也就是执行。如果是false，就不会执行
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 要增强的内容都写在此方法内部。
     * 此方法的返回值为null时，表示放行。
     * @return
     * @throws ZuulException
     *
     * 跨域操作是两次请求：
     *      第一次：预请求     任何消息头信息都不携带。
     *      第二次：正式请求
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("Zuul过滤器执行了");
        //得到request对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        //通过RequestContext获取请求对象
        HttpServletRequest request = requestContext.getRequest();

        //判断是否是预请求：使用的请求方式是OPTIONS
        if(request.getMethod().equalsIgnoreCase("OPTIONS")){
            return null;
        }

        //判断是否是登录操作
        String uri = request.getRequestURL().toString();
        if(uri.indexOf("/admin/login")>0){
            //表示是登录操作，不用携带认证的头信息
            return null;
        }

        //取出头信息
        String header = request.getHeader("Authorization");//Bearer XXXXXXXXXXXXXXXXX
        //判断头信息是否存在，且符合规则
        if(header != null && header.startsWith("Bearer ")) {
            //验证头信息是否就是管理员
            String token = header.split(" ")[1];
            Claims claims = jwtUtil.parseJWT(token);
            if (claims != null) {
                if (claims.get("roles").equals("admin")) {
                    //使用RequestContext对象，把头信息携带到路由目的地的微服务中
                    requestContext.addZuulRequestHeader("Authorization", header);
                    return null;
                }
            }
        }

        //表示上述执行没有成功，此时已经不能放行了，因为没有权限(有可能是没有消息头，有可能消息头不匹配，有可能是普通用户权限)
        requestContext.setSendZuulResponse(false); //保证后续操作不再执行

        requestContext.setResponseStatusCode(401);
        requestContext.getResponse().setContentType("text/html;charset=UTF-8");//设置响应正文的MIME类型和字符集
        requestContext.setResponseBody("没有权限");

        return null;
    }
}
