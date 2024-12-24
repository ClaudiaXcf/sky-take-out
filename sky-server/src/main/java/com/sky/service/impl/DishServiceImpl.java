package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public void add(DishDTO dto) {
        Dish dish = new Dish();
        //对象拷贝
        BeanUtils.copyProperties(dto, dish);
        //先添加菜品到数据库
        dishMapper.add(dish);

        //得到口味集合
        List<DishFlavor> flavorList = dto.getFlavors();
        //判断口味集合是否为空
        if (flavorList != null && flavorList.size() > 0) {
            //遍历集合
            flavorList.forEach(flavor -> {
                flavor.setDishId(dish.getId()); //怎么获取到主键id？ 回调
            });
        }
        dishFlavorMapper.add(flavorList);
    }

    /**
     * 菜品分页
     *
     * @param dto
     */
    @Override
    public PageResult page(DishPageQueryDTO dto) {
        //1.设置查询第几页，每页多少条
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        //2.调用dao
        Page<DishVO> page = dishMapper.page(dto);
        //3.返回封装结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 菜品启停
     *
     * @param status
     */
    @Override
    public void startOrStop(Integer status, Integer id) {
        dishMapper.startOrStop(status, id);
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     */
    @Override
    public void delete(List<Long> ids) {
        // 1.先查看菜品是否启用了
        ids.forEach(id -> {
            Dish dish = dishMapper.findById(id);
            if (dish.getStatus().equals(StatusConstant.ENABLE)) {
                // 说明该菜品是启用的
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        });

        // 2.再看套餐中是否关联该道菜品
        List<SetmealDish> setmealDishList = setmealDishMapper.findByDishId(ids);
        if (setmealDishList != null && !setmealDishList.isEmpty()) {
            // 说明有套餐关联该道菜品
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 3.到这就可以删除菜品了
        // 3.1删除菜品
        dishMapper.deleteDishById(ids);

        // 3.2删除口味
        dishFlavorMapper.deleteFlavorByDishId(ids);
    }

    /**
     * 根据id查询菜品
     * 要将查询到的菜品和口味都封装到dishVo中去
     *
     * @param id
     */
    @Override
    public DishVO findById(Long id) {
        // 1.根据id查询菜品
        Dish dish = dishMapper.findById(id);
        // 2.根据菜品id查询口味
        List<DishFlavor> flavorList = dishFlavorMapper.findByDishId(id);
        // 3.封装到VO中
        DishVO dishVO = new DishVO();
        // 4.属性拷贝
        BeanUtils.copyProperties(dish, dishVO);
        // 5.设置口味
        dishVO.setFlavors(flavorList);
        return dishVO;
    }

    /**
     * 更新菜品信息
     * 1.前端带过来的菜品信息可能含有 口味
     * 2.更新口味复杂，直接删除前端带过来的口味信息，然后添加更新的口味信息即可
     */
    @Override
    public void update(DishDTO dto) {
        // 1.更新菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto, dish);
        dishMapper.update(dish);

        // 2.删除菜品口味
        dishFlavorMapper.deleteFlavorByDishId(Arrays.asList(dto.getId()));

        // 3.添加菜品口味
        List<DishFlavor> flavorList = dto.getFlavors();
        if (flavorList != null && !flavorList.isEmpty()) {
            //设置每个口味属于哪道菜
            flavorList.forEach(flavor ->
                    flavor.setDishId(dto.getId()));
            //再添加口味集合到该道菜品中
            dishFlavorMapper.add(flavorList);
        }
    }
}
