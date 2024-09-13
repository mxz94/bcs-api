package cn.bcs.system.service;

import cn.bcs.common.annotation.DictConfig;
import cn.bcs.common.core.domain.entity.SysDictData;
import cn.bcs.common.core.domain.entity.SysDictType;
import cn.bcs.common.utils.DictUtils;
import cn.bcs.common.utils.StringUtils;
import cn.bcs.system.mapper.SysDictTypeMapper;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 字典 业务层
 *
 * @author ruoyi
 */
@Slf4j
@Service
public class SysDictTypeService extends ServiceImpl<SysDictTypeMapper, SysDictType> {
    @Resource
    private SysDictDataService sysDictDataService;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        initializationDict();
        loadingDictCache();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @SuppressWarnings({"unchecked"})
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        dictDatas = sysDictDataService.lambdaQuery().eq(SysDictData::getDictType, dictType).orderByAsc(SysDictData::getDictSort).list();
        if (StringUtils.isNotEmpty(dictDatas)) {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }


    /**
     * 加载字典缓存数据
     */
    @SuppressWarnings({"unchecked"})
    public void loadingDictCache() {
        Map<String, List<SysDictData>> dictDataMap = sysDictDataService.lambdaQuery().orderByAsc(SysDictData::getDictSort).list()
                .stream().collect(Collectors.groupingBy(SysDictData::getDictType));
        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
            DictUtils.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getDictSort)).collect(Collectors.toList()));
        }
    }

    /**
     * 初始化字典，对添加字段注解的枚举类初始化进数据库
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void initializationDict() {
        log.info("初始化字典.....");
        Map<String, SysDictType> existDictMap = this.list().stream().collect(Collectors.toMap(SysDictType::getDictType, Function.identity()));
        Map<String, List<SysDictData>> existDictDataMap = sysDictDataService.list().stream().collect(Collectors.groupingBy(SysDictData::getDictType));
        try {
            List<SysDictType> sysDictTypeList = new ArrayList<>();
            List<SysDictData> sysDictDataList = new ArrayList<>();
            List<String> dictTypeList = new ArrayList<>();
            Set<Class<?>> classes = ClassUtil.scanPackage("cn.bcs");
            for (Class<?> aClass : classes) {
                if (ClassUtil.isEnum(aClass)) {
                    Class<Enum> em = (Class<Enum>) aClass;
                    DictConfig annotation = em.getAnnotation(DictConfig.class);
                    if (Objects.isNull(annotation)) {
                        continue;
                    }
                    String dictType = annotation.dictType();
                    if (dictTypeList.contains(dictType)) {
                        throw new RuntimeException("初始化字典失败, 字典code重复, code:" + dictType);
                    }
                    dictTypeList.add(dictType);
                    String dictName = annotation.dictName();
                    String dictDesc = annotation.dictDesc();
                    Method codeMethod = ClassUtil.getDeclaredMethod(aClass, "getCode");
                    Method descMethod = ClassUtil.getDeclaredMethod(aClass, "getDesc");
                    if (codeMethod == null || descMethod == null) {
                        SysDictType sysDictType = existDictMap.getOrDefault(dictType, new SysDictType());
                        sysDictType.setDictType(dictType).setDictName(dictName);
                        sysDictTypeList.add(sysDictType);
                        continue;
                    }
                    if (existDictMap.containsKey(dictType)) {
                        //表中已存在，判断值
                        List<SysDictData> sysDictDatas = existDictDataMap.getOrDefault(dictType, new ArrayList<>());
                        Map<String, SysDictData> dictDataMap = sysDictDatas.stream().collect(Collectors.toMap(SysDictData::getDictValue, Function.identity()));
                        //得到enum的所有实例
                        Object[] objs = em.getEnumConstants();
                        for (int i = 0; i < objs.length; i++) {
                            Object obj = objs[i];
                            String dictValue = String.valueOf(codeMethod.invoke(obj));
                            String dictLabel = String.valueOf(descMethod.invoke(obj));
                            if (dictDataMap.containsKey(dictValue)) {
                                if (!dictDataMap.get(dictValue).getDictLabel().equals(dictLabel)) {
                                    sysDictDataList.add(dictDataMap.get(dictValue).setDictLabel(dictLabel));
                                }
                            } else {
                                sysDictDataList.add(new SysDictData().setDictType(dictType).setDictSort((long) i).setDictLabel(dictLabel).setDictValue(dictValue));
                            }
                        }
                        SysDictType sysDictType = existDictMap.get(dictType);
                        sysDictType.setDictType(dictType).setDictName(dictName);
                        sysDictTypeList.add(sysDictType);
                    } else {
                        //直接保存
                        SysDictType sysDictType = new SysDictType().setDictType(dictType).setDictName(dictName);
                        sysDictTypeList.add(sysDictType);
                        Object[] objs = em.getEnumConstants();
                        for (int i = 0; i < objs.length; i++) {
                            Object obj = objs[i];
                            String dictValue = String.valueOf(codeMethod.invoke(obj));
                            String dictLabel = String.valueOf(descMethod.invoke(obj));
                            sysDictDataList.add(new SysDictData().setDictType(dictType).setDictSort((long) i).setDictLabel(dictLabel).setDictValue(dictValue));
                        }
                    }
                }
            }
            if (CollUtil.isNotEmpty(sysDictTypeList)) {
                this.saveOrUpdateBatch(sysDictTypeList);
            }
            if (CollUtil.isNotEmpty(sysDictDataList)) {
                sysDictDataService.saveOrUpdateBatch(sysDictDataList);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空字典缓存数据
     */
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 重置字典缓存数据
     */
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

}
