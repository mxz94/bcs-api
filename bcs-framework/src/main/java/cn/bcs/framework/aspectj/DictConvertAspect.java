package cn.bcs.framework.aspectj;

import cn.bcs.common.annotation.DictConvert;
import cn.bcs.common.constant.Constants;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.page.TableDataInfo;
import cn.bcs.framework.dict.DictService;
import cn.bcs.framework.dict.vo.DictDataVO;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 字典aop类
 */
@Aspect
@Component
@Slf4j
public class DictConvertAspect {

    // 定义切点Pointcut
    @Pointcut("execution(public * cn.bcs..*.*Controller.*(..)) || @annotation(cn.bcs.common.annotation.DictConvert)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        parseDictText(result);
        return result;
    }

    /**
     * 解析字典文本
     * 在每个需要解析的字典字段添加一个新的文本字段，字段名为 字段字段_dictText
     *
     * @param result Object
     */
    @SuppressWarnings({"rawtypes"})
    private void parseDictText(Object result) {
        if (result instanceof Result) {
            if (((Result) result).getData() == null) {
                return;
            }
            if (((Result) result).getData() instanceof IPage) {
                convertList(result);
            } else if (((Result) result).getData() instanceof ArrayList) {
                convertList(result);
            } else if (((Result) result).getData().getClass().getTypeName().startsWith("cn.bcs")) {
                convertOne(result);
            }
        }
        if (result instanceof TableDataInfo) {
            if (((TableDataInfo) result).getRows() == null) {
                return;
            }
            if (((TableDataInfo) result).getRows() instanceof IPage) {
                convertList2(result);
            } else if (((TableDataInfo) result).getRows() instanceof ArrayList) {
                convertList2(result);
            }
        }
    }

    /**
     * list转换
     *
     * @param result Object
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void convertList(Object result) {
        List<JSONObject> items = new ArrayList<>();
        List<Object> list = new ArrayList<>();
        if (((Result) result).getData() instanceof IPage) {
            list = ((IPage) ((Result<?>) result).getData()).getRecords();
        } else if (((Result) result).getData() instanceof ArrayList) {
            list = (List) ((Result) result).getData();
        }
        for (Object record : list) {
            ObjectMapper mapper = new ObjectMapper();
            String json = "{}";
            try {
                // 解决@JsonFormat注解解析不了的问题详见SysAnnouncement类的@JsonFormat
                json = mapper.writeValueAsString(record);
            } catch (JsonProcessingException e) {
                log.error("json解析失败" + e.getMessage(), e);
            }
            if (!JSONUtil.isJson(json)) {
                return;
            }
            // 解决返回json数据后key顺序错乱
            JSONObject item = JSONObject.parseObject(json);
            // 遍历所有字段，把字典Code取出来，放到 map 里
            for (Field field : ConvertUtils.getAllFields(record)) {
                String value = item.getString(field.getName());
                if (StringUtils.isEmpty(value)) {
                    continue;
                }
                if (field.getAnnotation(DictConvert.class) != null) {
                    String dictType = field.getAnnotation(DictConvert.class).dictType();
                    List<DictDataVO> dictList = DictService.dictMap.get(dictType);
                    String dictText = translDictText(dictList, value);
                    item.put(field.getName() + Constants.DICT_TEXT_SUFFIX, dictText);
                }
                // date类型默认转换string格式化日期
                if ("java.util.Date".equals(field.getType().getName()) && field.getAnnotation(JsonFormat.class) == null && item.get(field.getName()) != null) {
                    SimpleDateFormat aDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    item.put(field.getName(), aDate.format(new Date((Long) item.get(field.getName()))));
                }
            }
            items.add(item);
        }
        if (((Result) result).getData() instanceof IPage) {
            ((IPage) ((Result) result).getData()).setRecords(items);
        } else if (((Result) result).getData() instanceof ArrayList) {
            ((Result) result).setData(items);
        }
    }

    private void convertList2(Object result) {
        List<JSONObject> items = new ArrayList<>();
        List<Object> list = new ArrayList<>();
        if (((TableDataInfo) result).getRows() instanceof IPage) {
            list = ((IPage) ((TableDataInfo<?>) result).getRows()).getRecords();
        } else if (((TableDataInfo) result).getRows() instanceof ArrayList) {
            list = (List) ((TableDataInfo) result).getRows();
        }
        for (Object record : list) {
            ObjectMapper mapper = new ObjectMapper();
            String json = "{}";
            try {
                // 解决@JsonFormat注解解析不了的问题详见SysAnnouncement类的@JsonFormat
                json = mapper.writeValueAsString(record);
            } catch (JsonProcessingException e) {
                log.error("json解析失败" + e.getMessage(), e);
            }
            if (!JSONUtil.isJson(json)) {
                return;
            }
            // 解决返回json数据后key顺序错乱
            JSONObject item = JSONObject.parseObject(json);
            // 遍历所有字段，把字典Code取出来，放到 map 里
            for (Field field : ConvertUtils.getAllFields(record)) {
                String value = item.getString(field.getName());
                if (StringUtils.isEmpty(value)) {
                    continue;
                }
                if (field.getAnnotation(DictConvert.class) != null) {
                    String dictType = field.getAnnotation(DictConvert.class).dictType();
                    List<DictDataVO> dictList = DictService.dictMap.get(dictType);
                    String dictText = translDictText(dictList, value);
                    item.put(field.getName() + Constants.DICT_TEXT_SUFFIX, dictText);
                }
                // date类型默认转换string格式化日期
                if ("java.util.Date".equals(field.getType().getName()) && field.getAnnotation(JsonFormat.class) == null && item.get(field.getName()) != null) {
                    SimpleDateFormat aDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    item.put(field.getName(), aDate.format(new Date((Long) item.get(field.getName()))));
                }
            }
            items.add(item);
        }
        if (((TableDataInfo) result).getRows() instanceof IPage) {
            ((IPage) ((TableDataInfo) result).getRows()).setRecords(items);
        } else if (((TableDataInfo) result).getRows() instanceof ArrayList) {
            ((TableDataInfo) result).setRows(items);
        }
    }

    /**
     * 单条转换
     *
     * @param result Object
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void convertOne(Object result) {
        Object record = ((Result) result).getData();
        ObjectMapper mapper = new ObjectMapper();
        String json = "{}";
        try {
            // 解决@JsonFormat注解解析不了的问题详见SysAnnouncement类的@JsonFormat
            json = mapper.writeValueAsString(record);
        } catch (JsonProcessingException e) {
            log.error("json解析失败" + e.getMessage(), e);
        }
        // 解决返回json数据后key顺序错乱
        JSONObject item = JSONObject.parseObject(json);
        // 遍历所有字段，把字典Code取出来，放到 map 里
        for (Field field : ConvertUtils.getAllFields(record)) {
            String value = item.getString(field.getName());
            if (StringUtils.isEmpty(value)) {
                continue;
            }
            if (field.getAnnotation(DictConvert.class) != null) {
                String dictType = field.getAnnotation(DictConvert.class).dictType();
                List<DictDataVO> dictList = DictService.dictMap.get(dictType);
                String dictText = translDictText(dictList, value);
                item.put(field.getName() + Constants.DICT_TEXT_SUFFIX, dictText);
            }
            // date类型默认转换string格式化日期
            if ("java.util.Date".equals(field.getType().getName()) && field.getAnnotation(JsonFormat.class) == null && item.get(field.getName()) != null) {
                SimpleDateFormat aDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                item.put(field.getName(), aDate.format(new Date((Long) item.get(field.getName()))));
            }
        }
        ((Result) result).setData(item);
    }

    /**
     * 字典值替换文本
     *
     * @param dictModels List
     * @param value      value
     * @return String
     */
    private String translDictText(List<DictDataVO> dictModels, String value) {
        if (CollUtil.isEmpty(dictModels)) {
            return value;
        }
        String dictText = value;
        for (DictDataVO dict : dictModels) {
            if (value.equals(dict.getDictValue())) {
                dictText = dict.getDictLabel();
                break;
            }
        }
        return dictText;
    }
}
