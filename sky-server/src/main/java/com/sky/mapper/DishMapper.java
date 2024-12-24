package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 添加dish
     */
    @AutoFill // 自动填充公共字段
    @Insert("insert into dish values (null,#{name},#{categoryId},#{price},#{image},#{description},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    // 设置主键回填
    void add(Dish dish);

    /**
     * 菜品分页
     */
    Page<DishVO> page(DishPageQueryDTO dto);

    /**
     * 菜品启停
     */
    @Update("update sky_take_out.dish set status = #{status} where id = #{id}")
    void startOrStop(Integer status, Integer id);

    /**
     * 根据id查询所有起售的菜品
     */
    @Select("select * from dish where id = #{id}")
    Dish findById(Long id);

    /**
     * 根据id删除菜品
     */
    void deleteDishById(List<Long> ids);

    /**
     * 更新菜品
     */
    @AutoFill
    @Update("update dish set name = #{name},category_id = #{categoryId},price = #{price}" +
            ",image = #{image},description = #{description},status=#{status}" +
            ",update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    void update(Dish dish);
}
