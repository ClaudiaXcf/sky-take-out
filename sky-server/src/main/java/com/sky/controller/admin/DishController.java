package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品接口")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 添加菜品
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result add(@RequestBody DishDTO dto) {
        dishService.add(dto);
        return Result.success();
    }

    /**
     * 菜品分页
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页")
    public Result page(DishPageQueryDTO dto) {
        PageResult pageResult = dishService.page(dto);
        return Result.success(pageResult);
    }

    /**
     * 菜品启用
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品状态更新")
    public Result startOrStop(@PathVariable Integer status, Integer id) {
        dishService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 删除菜品
     */
    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete(@RequestParam List<Long> ids) { //用来接收多个参数
        dishService.delete(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result findById(@PathVariable Long id) {
        DishVO dish = dishService.findById(id);
        return Result.success(dish);
    }

    /**
     * 更新菜品信息
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dto) {
        dishService.update(dto);
        return Result.success();
    }
}
