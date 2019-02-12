package com.tensquare.web;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Component
public class WebFilter extends ZuulFilter {

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
        //取出头信息
        String header = request.getHeader("Authorization");
        //使用RequestContext对象，把头信息携带到路由目的地的微服务中
        requestContext.addZuulRequestHeader("Authorization",header);
        return null;
    }
}
