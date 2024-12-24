package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 添加口味
     * @param flavorList
     */
    void add(List flavorList);

    /**
     * 根据菜品id删除口味
     */
    void deleteFlavorByDishId(List<Long> ids);

    /**
     * 根据菜品id查询口味
     */
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> findByDishId(Long id);
}
