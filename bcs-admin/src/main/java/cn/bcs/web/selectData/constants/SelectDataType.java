package cn.bcs.web.selectData.constants;

import cn.bcs.common.annotation.DictConfig;
import lombok.Getter;

/**
 * 启用禁用状态
 * @author m
 * @date 2023/5/8 18:21
 */
@Getter
@DictConfig(dictType = "select_data_type", dictName = "选择数据类型")
public enum SelectDataType {
    //PERSON("person", "档案责任人"),
    //FILE_TYPE("fileType", "类别"),
    //SECRECY_LEVEL("secrecyLevel", "保密等级"),
    //BINDING_TYPE("bindingType", "装订方式"),
    //MATERIAL("material", "材质管理"),
    //EXTERNAL_DEVICE("externalDevice", "外接设备"),
    QUESTION("question", "常见问题"),
    TAOCAN("taocan", "套餐管理");
    public static final String INFO = " taocan 套餐 question 常见问题";
    private final String code;
    private final String desc;

    SelectDataType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SelectDataType getByCode(String code) {
        if (code != null) {
            for (SelectDataType status : SelectDataType.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
