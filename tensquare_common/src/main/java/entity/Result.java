package entity;

import java.io.Serializable;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * 返回结果的实体类
 *    是否执行成功
 *    状态码
 *    提示信息
 *    返回的数据（它也可有能没有，比如增删改操作）
 */
public class Result implements Serializable{

    private Boolean flag;
    private Integer code;
    private String message;
    private Object data;

    /**
     * 用于增删改方法给结果赋值
     * @param flag
     * @param code
     * @param message
     */
    public Result(Boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    /**
     * 用于查询方法给结果赋值
     * @param flag
     * @param code
     * @param message
     * @param data
     */
    public Result(Boolean flag, Integer code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 空参构造函数
     */
    public Result() {
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "flag=" + flag +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
