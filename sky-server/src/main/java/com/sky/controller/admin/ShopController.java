package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
@Api(tags = "店铺接口")
public class ShopController {
    //设置一个常量 防止下面名称不一致
    //private final static String SHOT_STATUS = "status";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺状态
     *
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺状态")
    public Result setStatus(@PathVariable Integer status) {
        //将店铺状态设置到redis中
        redisTemplate.opsForValue().set(MessageConstant.SHOP_STATUS, status);
        return Result.success();
    }

    /**
     * 获取店铺状态
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺状态")
    public Result getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(MessageConstant.SHOP_STATUS);
        return Result.success(status == null ? 0 : status);
    }
}
