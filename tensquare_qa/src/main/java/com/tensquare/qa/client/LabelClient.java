package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.LabelClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@FeignClient(value="tensquare-base",fallback = LabelClientImpl.class)//指定微服务的名称
public interface LabelClient {

    /**
     * 根据id查询
     * @param labelid
     * @return
     */
    @RequestMapping(value="/label/{labelid}",method= RequestMethod.GET)
    Result findById(@PathVariable("labelid") String labelid);
}
