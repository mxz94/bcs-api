package cn.bcs.web.selectData.constants;

import cn.bcs.common.annotation.DictConfig;
import lombok.Getter;

/**
 * 启用禁用状态
 * @author zhaoshuaixiang
 * @date 2023/5/8 18:21
 */
@Getter
@DictConfig(dictType = "select_data_type", dictName = "选择数据类型")
public enum SelectDataType {
    PERSON("person", "档案责任人"),
    FILE_TYPE("fileType", "类别"),
    SECRECY_LEVEL("secrecyLevel", "保密等级"),
    BINDING_TYPE("bindingType", "装订方式"),
    MATERIAL("material", "材质管理"),
    EXTERNAL_DEVICE("externalDevice", "外接设备"),
    AREA("area", "区域管理");
    public static final String INFO = " person:档案责任人，fileType:类别, secrecyLevel:保密等级 bindingType 装订方式 material 材质管理 area 区域管理 externalDevice 外接设备";
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
