package cn.bcs.common.core.domain;

import cn.bcs.common.constant.HttpStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

/**
 * 操作消息提醒
 *
 * @author ruoyi
 */
@Data
public class Result<T> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据")
    private T data;

    @ApiModelProperty(value = "消息状态码")
    private int code;

    @ApiModelProperty(value = "消息内容")
    private String msg;

    public static <T> Result<T> success() {
        return restResult(null, HttpStatus.SUCCESS, "操作成功");
    }

    public static <T> Result<T> success(T data) {
        return restResult(data, HttpStatus.SUCCESS, "操作成功");
    }

    public static <T> Result<T> success(String msg) {
        return restResult(null, HttpStatus.SUCCESS, msg);
    }

    public static <T> Result<T> success(String msg, T data) {
        return restResult(data, HttpStatus.SUCCESS, msg);
    }

    public static <T> Result<T> warn(String msg) {
        return restResult(null, HttpStatus.WARN, null);
    }

    public static <T> Result<T> warn(String msg, T data) {
        return restResult(data, HttpStatus.WARN, msg);
    }

    public static <T> Result<T> error() {
        return restResult(null, HttpStatus.ERROR, "操作失败");
    }

    public static <T> Result<T> error(String msg) {
        return restResult(null, HttpStatus.ERROR, msg);
    }

    public static <T> Result<T> error(String msg, T data) {
        return restResult(data, HttpStatus.ERROR, msg);
    }

    public static <T> Result<T> error(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> Result<T> restResult(T data, int code, String msg) {
        Result<T> rspData = new Result<>();
        rspData.setCode(code);
        rspData.setMsg(msg);
        rspData.setData(data);
        return rspData;
    }

}
