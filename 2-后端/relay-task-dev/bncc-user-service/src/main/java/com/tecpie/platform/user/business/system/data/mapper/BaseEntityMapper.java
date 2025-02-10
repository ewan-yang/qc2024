package com.tecpie.platform.user.business.system.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.library.common.business.entity.BaseEntity;

/**
 * 因为MyBatis3.2+之后不会缓存实体类的父类字段信息，所以在使用泛型的Lambda表达式时会报错
 * {@code MybatisPlusException: can not find lambda cache for this entity[BaseEntity]}
 * 原因是在执行{@link com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl#logicRemoveById(BaseEntity)}时
 * {@code COLUMN_CACHE_MAP}中没有{@link BaseEntity}的信息
 * 在{@link com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl}中其他logicRemove*方法均存在同样的问题
 * <p>
 * 根据源码
 * {@link com.baomidou.mybatisplus.core.MybatisMapperRegistry#addMapper(Class)}
 * {@link com.baomidou.mybatisplus.core.MybatisMapperAnnotationBuilder#parse()}方法的加载逻辑
 * <p>
 * 他会将所有扫描到的mapper中的泛型({@link BaseMapper<Class>}中的Class，即实体类)的字段信息缓存到
 * {@link com.baomidou.mybatisplus.core.toolkit.LambdaUtils}中的{@code COLUMN_CACHE_MAP}中。
 * 但是MP3.2+之后不会加载父类的信息，所以{@code COLUMN_CACHE_MAP}中没有相关缓存，就报错了。
 * <p>
 * 因此我们单独为{@link BaseEntity}添加一个的Mapper类，这样MyBatis就会缓存该类的信息了。
 */
public interface BaseEntityMapper extends BaseMapper<BaseEntity> {

}
