package cn.bcs.common.core.redis;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * spring redis 工具类
 *
 * @author ruoyi
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisCache {
    //默认缓存24小时
    private final static TimedCache<String, Object> valueOps = CacheUtil.newTimedCache(TimeUnit.HOURS.toMillis(24));

    static {
        /* 每5ms检查一次过期 */
        valueOps.schedulePrune(5);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        valueOps.put(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        valueOps.put(key, value, timeUnit.toMillis(timeout));
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        return valueOps.keySet().contains(key);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        Object o = valueOps.get(key, false);
        if (o == null) {
            return null;
        }
        return Convert.convert((Class<T>) o.getClass(), o);
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存键值
     */
    public void deleteObject(final String key) {
        valueOps.remove(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     */
    public void deleteObject(final Collection<String> collection) {
        collection.forEach(valueOps::remove);
    }


    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return valueOps.keySet().stream()
                .filter(key -> key.startsWith(pattern))
                .collect(Collectors.toList());
    }

}
