package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 增加菜品
     * @param dto
     */
    void add(DishDTO dto);

    /**
     * 菜品分页
     */
    PageResult page(DishPageQueryDTO dto);

    /**
     * 菜品启停
     * @param status
     */
    void startOrStop(Integer status,Integer id);

    /**
     * 删除菜品
     */
    void delete(List<Long> ids);

    /**
     * 根据id查询菜品
     */
    DishVO findById(Long id);

    /**
     * 更新菜品
     */
    void update(DishDTO dto);

}
