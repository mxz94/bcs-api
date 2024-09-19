package cn.bcs.system.domain;

import cn.bcs.common.annotation.DictConvert;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.enums.SysUserType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TeamTreeVO {

    private Long userId;
    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @DictConvert(dictType = "sys_user_type")
    @ApiModelProperty(value = "用户类型" + SysUserType.INFO)
    private String userType;
    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String avatar;

    private List<TeamTreeVO> children;

    private Long fromUserId;
}
