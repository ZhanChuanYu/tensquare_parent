package com.tensquare.qa.client.impl;

import com.tensquare.qa.client.LabelClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Component
public class LabelClientImpl implements LabelClient {

    @Override
    public Result findById(String labelid) {
        System.out.println("熔断器执行了");
        return new Result(false, StatusCode.REMOTEERROR,"应用调用失败，熔断器启动");
    }
}
