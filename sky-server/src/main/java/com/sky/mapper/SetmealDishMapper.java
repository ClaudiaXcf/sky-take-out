package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询含有该道菜品的套餐
     * 并返回 SetmealDish 实体类
     */

    List<SetmealDish> findByDishId(List<Long> ids);
}
