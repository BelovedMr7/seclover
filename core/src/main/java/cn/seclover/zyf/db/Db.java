package cn.seclover.zyf.db;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.seclover.zyf.util.SpringContextHolder;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 便捷数据库操作类
 * <p>
 * <p>
 * 本类的使用情景：
 * <p>
 * 1.单纯想创建现有的Mapper
 * <p>例如:
 * Db.getMapper(UserLoginMapper.class).selectById("14779707158513204");<br>
 */
@SuppressWarnings("all")
public class Db<T> {

    /**
     * 每个Db类，包装一个Mapper接口,这个clazz就是接口的类类型，例如UserMapper.class
     */
    private Class<T> clazz;

    /**
     * Mapper的父类接口
     */
    private BaseMapper<?> baseMapper;

    /**
     * 私有构造方法，不允许自己创建
     */
    private Db(Class clazz) {
        this.clazz = clazz;
        this.baseMapper = (BaseMapper<?>) SpringContextHolder.getBean(clazz);
    }

    /**
     * <p>
     * 创建包含指定mapper的Db工具类,使用本类的第一种用法
     */
    public static <T> Db<T> create(Class<T> clazz) {
        return new Db<T>(clazz);
    }

    /**
     * <p>
     * 获取一个mapper的快捷方法
     */
    public BaseMapper<?> getMapper() {
        return this.baseMapper;
    }

    /**
     * <p>
     * 获取一个mapper的快捷方法
     */
    public static <T> T getMapper(Class<T> clazz) {
        return SpringContextHolder.getBean(clazz);
    }

    /**
     * 通过一个条件获取数据库中的一条记录(会返回null)
     */
    public <E> E selectOneByCon(String condition, Object value) {
        List<?> results = selectOneByConList(condition, value);
        if (results != null && results.size() > 0) {
            return (E) results.get(0);
        } else {
            return null;
        }
    }

    /**
     * 通过一个条件获取一堆记录(会返回null)
     */
    public <E> List<E> selectOneByConList(String condition, Object value) {
        HashMap<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.put(condition, value);

        List<E> results = (List<E>) this.baseMapper.selectByMap(conditionMap);
        if (results == null || results.size() == 0) {
            return null;
        } else {
            return results;
        }
    }
}
