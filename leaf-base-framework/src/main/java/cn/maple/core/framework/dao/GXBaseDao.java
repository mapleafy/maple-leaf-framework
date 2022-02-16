package cn.maple.core.framework.dao;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.GXBaseData;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import com.google.common.collect.Table;

import java.util.List;

public interface GXBaseDao<T extends GXBaseData, R extends GXBaseResDto, ID> {
    /**
     * 保存数据
     *
     * @param entity    需要保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    default ID create(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity 需要保存的数据
     * @return ID
     */
    default ID create(T entity) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    default ID update(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    default ID updateOrCreate(T entity, Table<String, String, Object> condition) {
        throw new GXBusinessException("自定义实现");
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName 表名字
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return 影响的行数
     */
    Integer updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition);

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition);

    /**
     * 通过SQL语句批量插入数据
     *
     * @param tableName 表名字
     * @param dataList  数据集合
     * @return int
     */
    Integer batchInsert(String tableName, List<Dict> dataList);

    /**
     * 通过条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return 列表
     */
    R findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 通过条件获取数据列表
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return 列表
     */
    List<R> findByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteSoftCondition(String tableName, Table<String, String, Object> condition);

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteCondition(String tableName, Table<String, String, Object> condition);

    /**
     * 分页
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return 分页数据
     */
    GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 分页  返回实体对象
     *
     * @param dbQueryParamInnerDto 查询条件
     * @param mapperMethodName     Mapper方法
     * @return GXPagination
     */
    GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto, String mapperMethodName);

    /**
     * 获取表名字
     *
     * @param clazz 实体的类型
     * @return 数据库表的名字
     */
    String getTableName(Class<T> clazz);
}
