package cn.bcs.framework.dict;

import cn.bcs.common.annotation.DictConfig;
import cn.bcs.common.core.domain.Result;
import cn.bcs.framework.dict.vo.DictDataVO;
import cn.hutool.core.util.ClassUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author zhaoshuaixiang
 * @date 2024/4/8 10:18:21
 */
@Service
public class DictService {

    public static final Map<String, List<DictDataVO>> dictMap = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            Set<Class<?>> classes = ClassUtil.scanPackage("cn.bcs");
            for (Class<?> aClass : classes) {
                if (ClassUtil.isEnum(aClass)) {
                    Class<Enum> em = (Class<Enum>) aClass;
                    DictConfig annotation = em.getAnnotation(DictConfig.class);
                    if (Objects.isNull(annotation)) {
                        continue;
                    }
                    String dictType = annotation.dictType();
                    if (dictMap.containsKey(dictType)) {
                        throw new RuntimeException("初始化字典失败, 字典code重复, code:" + dictType);
                    }
                    Method codeMethod = ClassUtil.getDeclaredMethod(aClass, "getCode");
                    Method descMethod = ClassUtil.getDeclaredMethod(aClass, "getDesc");
                    Method cssClassMethod = ClassUtil.getDeclaredMethod(aClass, "getCssClass");
                    if (codeMethod == null || descMethod == null) {
                        continue;
                    }
                    Object[] objs = em.getEnumConstants();
                    List<DictDataVO> sysDictDataList = new ArrayList<>();
                    for (int i = 0; i < objs.length; i++) {
                        Object obj = objs[i];
                        String dict_value = String.valueOf(codeMethod.invoke(obj));
                        String dict_label = String.valueOf(descMethod.invoke(obj));
                        String cssClass = cssClassMethod == null ? "" : String.valueOf(cssClassMethod.invoke(obj));
                        sysDictDataList.add(new DictDataVO().setDictType(dictType).setDictLabel(dict_label).setDictValue(dict_value).setCssClass(cssClass));
                    }
                    dictMap.put(dictType, sysDictDataList);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("初始化字典失败:" + e);
        }
    }


    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public Result<List<DictDataVO>> selectDictDataByType(String dictType) {
        return Result.success(dictMap.get(dictType));
    }

}
