package cn.bcs.system.domain.vo;

import cn.bcs.common.core.domain.entity.SysUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author m
 * @date 2023/5/9 18:30
 */
@Accessors(chain = true)
@Data
public class LoginVO {

    private String token;

    private SysUser user;

    private Set<String> roles;

    private Set<String> permissions;
}
