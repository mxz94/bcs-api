package cn.bcs.common.core.page;

import cn.bcs.common.constant.HttpStatus;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author ruoyi
 */
@ApiModel
@Data
public class TableDataInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总记录数")
    private long total;

    @ApiModelProperty(value = "列表数据")
    private List<T> rows;

    @ApiModelProperty(value = "消息状态码")
    private int code;

    @ApiModelProperty(value = "消息内容")
    private String msg;


    public static <T> TableDataInfo<T> ok() {
        return restResult(null, HttpStatus.SUCCESS, "操作成功");
    }

    public static <T> TableDataInfo<T> ok(List<T> data) {
        return restResult(data, HttpStatus.SUCCESS, "操作成功");
    }

    public static <T> TableDataInfo<T> ok(List<T> data, String msg) {
        return restResult(data, HttpStatus.SUCCESS, msg);
    }

    public static <T> TableDataInfo<T> fail() {
        return restResult(null, HttpStatus.ERROR, "操作失败");
    }

    public static <T> TableDataInfo<T> fail(String msg) {
        return restResult(null, HttpStatus.ERROR, msg);
    }

    public static <T> TableDataInfo<T> fail(List<T> data) {
        return restResult(data, HttpStatus.ERROR, "操作失败");
    }

    public static <T> TableDataInfo<T> fail(List<T> data, String msg) {
        return restResult(data, HttpStatus.ERROR, msg);
    }

    public static <T> TableDataInfo<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T> TableDataInfo<T> restResult(List<T> list, int code, String msg) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(code);
        rspData.setMsg(msg);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

}
