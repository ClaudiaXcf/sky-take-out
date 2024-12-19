package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    public void add(DishDTO dto) {
        Dish dish = new Dish();
        //对象拷贝
        BeanUtils.copyProperties(dto,dish);
        //先添加菜品到数据库
        dishMapper.add(dish);

        //得到口味集合
        List<DishFlavor> flavorList = dto.getFlavors();
        //判断口味集合是否为空
        if(flavorList != null && flavorList.size() > 0) {
            //遍历集合
            flavorList.forEach(flavor -> {
                flavor.setDishId(dish.getId()); //怎么获取到主键id？ 回调
            });
        }
        dishFlavorMapper.add(flavorList);
    }
}
