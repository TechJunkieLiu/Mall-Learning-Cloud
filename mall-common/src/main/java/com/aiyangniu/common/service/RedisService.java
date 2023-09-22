package com.aiyangniu.common.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis操作接口
 *
 * @author lzq
 * @date 2023/09/22
 */
public interface RedisService {

    /**
     * 保存属性
     *
     * @param key 键
     * @param value 值
     * @param time 过期时间
     */
    void set(String key, Object value, long time);

    /**
     * 保存属性
     *
     * @param key 键
     * @param value 值
     */
    void set(String key, Object value);

    /**
     * 获取属性
     *
     * @param key 键
     * @return 属性
     */
    Object get(String key);

    /**
     * 删除属性
     *
     * @param key 键
     * @return 是否成功
     */
    Boolean del(String key);

    /**
     * 批量删除属性
     *
     * @param keys 键
     * @return 删除个数
     */
    Long del(List<String> keys);

    /**
     * 设置过期时间
     *
     * @param key 键
     * @param time 过期时间
     * @return 是否成功
     */
    Boolean expire(String key, long time);

    /**
     * 获取过期时间
     *
     * @param key 键
     * @return 过期时间
     */
    Long getExpire(String key);

    /**
     * 判断是否有该属性
     *
     * @param key 键
     * @return 是否有
     */
    Boolean hasKey(String key);

    /**
     * 按delta递增
     *
     * @param key 键
     * @param delta 1
     * @return 递增数
     */
    Long incr(String key, long delta);

    /**
     * 按delta递减
     *
     * @param key 键
     * @param delta delta
     * @return 递减数
     */
    Long decr(String key, long delta);

    /**
     * 获取Hash结构中的属性
     *
     * @param key 键
     * @param hashKey hashKey
     * @return 属性
     */
    Object hGet(String key, String hashKey);

    /**
     * 向Hash结构中放入一个属性
     *
     * @param key 键
     * @param hashKey hashKey
     * @param value 值
     * @param time 过期时间
     * @return 是否成功
     */
    Boolean hSet(String key, String hashKey, Object value, long time);

    /**
     * 向Hash结构中放入一个属性
     *
     * @param key 键
     * @param hashKey hashKey
     * @param value 值
     */
    void hSet(String key, String hashKey, Object value);

    /**
     * 直接获取整个Hash结构
     *
     * @param key 键
     * @return Hash结构
     */
    Map<Object, Object> hGetAll(String key);

    /**
     * 直接设置整个Hash结构
     *
     * @param key 键
     * @param map map
     * @param time 过期时间
     * @return 是否成功
     */
    Boolean hSetAll(String key, Map<String, Object> map, long time);

    /**
     * 直接设置整个Hash结构
     *
     * @param key 键
     * @param map map
     */
    void hSetAll(String key, Map<String, ?> map);

    /**
     * 删除Hash结构中的属性
     *
     * @param key 键
     * @param hashKey hashKey
     */
    void hDel(String key, Object... hashKey);

    /**
     * 判断Hash结构中是否有该属性
     *
     * @param key 键
     * @param hashKey hashKey
     * @return 是否有
     */
    Boolean hHasKey(String key, String hashKey);

    /**
     * Hash结构中属性递增
     *
     * @param key 键
     * @param hashKey hashKey
     * @param delta delta
     * @return 递增数
     */
    Long hIncr(String key, String hashKey, Long delta);

    /**
     * Hash结构中属性递减
     *
     * @param key 键
     * @param hashKey hashKey
     * @param delta delta
     * @return 递减数
     */
    Long hDecr(String key, String hashKey, Long delta);

    /**
     * 获取Set结构
     *
     * @param key 键
     * @return Set结构
     */
    Set<Object> sMembers(String key);

    /**
     * 向Set结构中添加属性
     *
     * @param key 键
     * @param values 值
     * @return 添加个数
     */
    Long sAdd(String key, Object... values);

    /**
     * 向Set结构中添加属性
     *
     * @param key 键
     * @param time 过期时间
     * @param values 值
     * @return 添加个数
     */
    Long sAdd(String key, long time, Object... values);

    /**
     * 是否为Set中的属性
     *
     * @param key 键
     * @param value 值
     * @return 是否存在
     */
    Boolean sIsMember(String key, Object value);

    /**
     * 获取Set结构的长度
     *
     * @param key 键
     * @return Set长度
     */
    Long sSize(String key);

    /**
     * 删除Set结构中的属性
     *
     * @param key 键
     * @param values 值
     * @return 删除个数
     */
    Long sRemove(String key, Object... values);

    /**
     * 获取List结构中的属性
     *
     * @param key 键
     * @param start 开始
     * @param end 结束
     * @return 属性
     */
    List<Object> lRange(String key, long start, long end);

    /**
     * 获取List结构的长度
     *
     * @param key 键
     * @return 列表长度
     */
    Long lSize(String key);

    /**
     * 根据索引获取List中的属性
     *
     * @param key 键
     * @param index 索引
     * @return 属性
     */
    Object lIndex(String key, long index);

    /**
     * 向List结构中添加属性
     *
     * @param key 键
     * @param value 值
     * @return 列表长度
     */
    Long lPush(String key, Object value);

    /**
     * 向List结构中添加属性
     *
     * @param key 键
     * @param value 值
     * @param time 过期时间
     * @return 列表长度
     */
    Long lPush(String key, Object value, long time);

    /**
     * 向List结构中批量添加属性
     *
     * @param key 键
     * @param values 值
     * @return 列表长度
     */
    Long lPushAll(String key, Object... values);

    /**
     * 向List结构中批量添加属性
     *
     * @param key 键
     * @param time 过期时间
     * @param values 值
     * @return 列表长度
     */
    Long lPushAll(String key, Long time, Object... values);

    /**
     * 从List结构中移除属性
     *
     * @param key 键
     * @param count 数量
     * @param value 值
     * @return 列表长度
     */
    Long lRemove(String key, long count, Object value);
}
