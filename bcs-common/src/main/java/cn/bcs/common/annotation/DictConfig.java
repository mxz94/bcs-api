package cn.bcs.common.annotation;


import java.lang.annotation.*;

/**
 * @author zhaoshuaixiang
 * @date 2021-09-24 17:06:46
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DictConfig {
    /**
     * dictType
     *
     * @return String
     */
    String dictType();

    /**
     * 字典名称
     *
     * @return String
     */
    String dictName();

    /**
     * 字典描述
     *
     * @return String
     */
    String dictDesc() default "";
}
