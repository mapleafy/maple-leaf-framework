package cn.maple.core.datasource.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.model.GXMyBatisModel;
import cn.maple.core.datasource.util.GXDBCommonUtils;
import cn.maple.core.framework.constant.GXBuilderConstant;
import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.dao.GXBaseDao;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.res.GXBaseResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXCommonUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GXMyBatisDao<M extends GXBaseMapper<T, R>, T extends GXMyBatisModel, R extends GXBaseResDto, ID extends Serializable> extends ServiceImpl<M, T> implements GXBaseDao<T, R, ID> {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GXMyBatisDao.class);

    /**
     * 保存数据
     *
     * @param entity 需要保存的数据
     * @return ID
     */
    @Override
    public ID create(T entity) {
        save(entity);
        String methodName = CharSequenceUtil.format("get{}", CharSequenceUtil.upperFirst(getPrimaryKeyName()));
        return Convert.convert(getIDClassType(), GXCommonUtils.reflectCallObjectMethod(entity, methodName));
    }

    /**
     * 分页  返回实体对象
     *
     * @param dbQueryParamInnerDto     查询条件
     * @param mapperPaginateMethodName Mapper中的分页方法名字
     * @return GXPagination
     */
    @Override
    public GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto, String mapperPaginateMethodName) {
        IPage<R> iPage = constructPageObject(dbQueryParamInnerDto.getPage(), dbQueryParamInnerDto.getPageSize());
        if (CharSequenceUtil.isEmpty(mapperPaginateMethodName)) {
            mapperPaginateMethodName = "paginate";
        }
        Set<String> fieldSet = dbQueryParamInnerDto.getColumns();
        if (Objects.isNull(fieldSet)) {
            dbQueryParamInnerDto.setColumns(CollUtil.newHashSet("*"));
        }
        Method mapperMethod = ReflectUtil.getMethod(baseMapper.getClass(), mapperPaginateMethodName, IPage.class, dbQueryParamInnerDto.getClass());
        if (Objects.nonNull(mapperMethod)) {
            final List<R> list = ReflectUtil.invoke(baseMapper, mapperMethod, iPage, dbQueryParamInnerDto);
            iPage.setRecords(list);
            return GXDBCommonUtils.convertPageToPaginationResDto(iPage);
        }
        Class<?>[] interfaces = baseMapper.getClass().getInterfaces();
        if (interfaces.length > 0) {
            String canonicalName = interfaces[0].getCanonicalName();
            throw new GXBusinessException(CharSequenceUtil.format("请在{}类中实现{}方法", canonicalName, mapperPaginateMethodName));
        }
        throw new GXBusinessException(CharSequenceUtil.format("请在Mapper类中申明{}方法", mapperPaginateMethodName));
    }

    /**
     * 通过条件获取分页数据列表
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return 列表
     */
    @Override
    public GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        IPage<R> iPage = constructPageObject(dbQueryParamInnerDto.getPage(), dbQueryParamInnerDto.getPageSize());
        if (Objects.isNull(dbQueryParamInnerDto.getColumns())) {
            dbQueryParamInnerDto.setColumns(CollUtil.newHashSet("*"));
        }
        List<R> paginate = baseMapper.paginate(iPage, dbQueryParamInnerDto);
        iPage.setRecords(paginate);
        return GXDBCommonUtils.convertPageToPaginationResDto(iPage);
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName 表名字
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return 影响的行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition) {
        if (Objects.isNull(condition) || condition.isEmpty()) {
            throw new GXBusinessException("更新数据需要指定条件");
        }
        return baseMapper.updateFieldByCondition(tableName, data, condition);
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    @Override
    public boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).condition(condition).build();
        Integer val = baseMapper.checkRecordIsExists(queryParamInnerDto);
        return Objects.nonNull(val);
    }

    /**
     * 通过SQL语句批量插入数据
     *
     * @param tableName 表名字
     * @param dataList  数据集合
     * @return int
     */
    @Override
    @SuppressWarnings("all")
    @Transactional(rollbackFor = Exception.class)
    public Integer saveBatch(String tableName, List<Dict> dataList) {
        return baseMapper.saveBatch(tableName, dataList);
    }

    /**
     * 保存数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    @Override
    public ID updateOrCreate(T entity, Table<String, String, Object> condition) {
        if (Objects.isNull(condition) || condition.isEmpty()) {
            saveOrUpdate(entity);
        } else {
            UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
            condition.columnMap().forEach((op, columnData) -> columnData.forEach((column, value) -> setUpdateWrapper(updateWrapper, Dict.create().set("op", op).set("column", column).set("value", value))));
            saveOrUpdate(entity, updateWrapper);
        }
        String methodName = CharSequenceUtil.format("get{}", CharSequenceUtil.upperFirst(getPrimaryKeyName()));
        return Convert.convert(getIDClassType(), GXCommonUtils.reflectCallObjectMethod(entity, methodName));
    }

    /**
     * 设置更新条件对象的值
     *
     * @param updateWrapper MyBatis更新条件对象
     * @param condition     条件
     */
    protected void setUpdateWrapper(UpdateWrapper<T> updateWrapper, Dict condition) {
        String op = condition.getStr("op");
        String column = condition.getStr("column");
        String value = condition.getStr("value");
        if (CharSequenceUtil.isBlank(value)) {
            return;
        }
        column = CharSequenceUtil.toUnderlineCase(column);
        Dict methodNameDict = Dict.create().set(GXBuilderConstant.EQ, "eq").set(GXBuilderConstant.STR_EQ, "eq").set(GXBuilderConstant.NOT_EQ, "ne").set(GXBuilderConstant.STR_NOT_EQ, "ne").set(GXBuilderConstant.NOT_IN, "notIn").set(GXBuilderConstant.STR_NOT_IN, "notIn").set(GXBuilderConstant.GE, "ge").set(GXBuilderConstant.GT, "gt").set(GXBuilderConstant.LE, "le").set(GXBuilderConstant.LT, "lt");
        String methodName = methodNameDict.getStr(op);
        if (Objects.nonNull(methodName)) {
            if (!GXCommonUtils.digitalRegularExpression(value)) {
                value = CharSequenceUtil.format("'{}'", value);
            }
            GXCommonUtils.reflectCallObjectMethod(updateWrapper, methodName, true, column, value);
        }
    }

    /**
     * 通过条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return 列表
     */
    @Override
    public R findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        return baseMapper.findOneByCondition(dbQueryParamInnerDto);
    }

    /**
     * 通过条件获取数据列表
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return 列表
     */
    @Override
    public List<R> findByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto) {
        return baseMapper.findByCondition(dbQueryParamInnerDto);
    }

    /**
     * 构造分页对象
     *
     * @param page     当前页
     * @param pageSize 每页大小
     * @return 分页对象
     */
    public IPage<R> constructPageObject(Integer page, Integer pageSize) {
        int defaultCurrentPage = GXCommonConstant.DEFAULT_CURRENT_PAGE;
        int defaultPageSize = GXCommonConstant.DEFAULT_PAGE_SIZE;
        int defaultMaxPageSize = GXCommonConstant.DEFAULT_MAX_PAGE_SIZE;
        if (Objects.isNull(page) || page < 0) {
            page = defaultCurrentPage;
        }
        if (Objects.isNull(pageSize) || pageSize > defaultMaxPageSize || pageSize <= 0) {
            pageSize = defaultPageSize;
        }
        return new Page<>(page, pageSize);
    }

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteSoftCondition(String tableName, Table<String, String, Object> condition) {
        return baseMapper.deleteSoftCondition(tableName, condition);
    }

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteCondition(String tableName, Table<String, String, Object> condition) {
        return baseMapper.deleteCondition(tableName, condition);
    }

    /**
     * 获取表名字
     *
     * @param clazz 实体的类型
     * @return 数据库表的名字
     */
    @Override
    public String getTableName(Class<T> clazz) {
        return GXDBCommonUtils.getTableName(clazz);
    }

    /**
     * 获取主键标识的类型
     *
     * @return Class
     */
    @SuppressWarnings("all")
    private Class<ID> getIDClassType() {
        return (Class<ID>) TypeUtil.getClass(((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3]);
    }

    /**
     * 获取实体的Class 对象
     *
     * @return Class
     */
    @SuppressWarnings("all")
    private String getPrimaryKeyName() {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(GXCommonUtils.getGenericClassType(getClass(), 1));
        return tableInfo.getKeyProperty();
    }
}