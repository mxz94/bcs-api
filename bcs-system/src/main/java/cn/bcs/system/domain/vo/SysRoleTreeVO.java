package cn.bcs.system.domain.vo;

import cn.hutool.core.lang.tree.Tree;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author m
 * @date 2023/5/15 10:00
 */
@Accessors(chain = true)
@Data
public class SysRoleTreeVO {

    @ApiModelProperty(value = "菜单列表")
    private List<Tree<Integer>> menus;

    @ApiModelProperty(value = "角色已选中菜单列表")
    private List<Long> checkedKeys;
}
