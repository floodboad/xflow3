package org.xsnake.cloud.dao;


/***
 *  基础的一些查询方法
 * */
public interface IBaseDao<T> {

    /**
     * 根据ID 查询对象
     * */
    T queryById(Long id);

    /**
     * 根据ID 查询对象
     * */
    T queryById(T t);

    /**
     * 新增或者更新数据
     * */
    int addOrUpdate(T t);

    /**
     * 更具ID 删除数据
     * */
    int deleteById(Long id);


    /**
     * 根据 对象里的条件删除数据
     * */
    int delete(T t);

    /**
     * 更具ID 删除数据
     * */
    int deleteById(T t);

    /**
     * 更新数据
     *
     * @param isUpdateEmpty 是否把空指端更新到数据库
     * */
    int update(T t, boolean isUpdateEmpty);


    /**
     * 插入数据
     * */
    int insert(T t );


}
