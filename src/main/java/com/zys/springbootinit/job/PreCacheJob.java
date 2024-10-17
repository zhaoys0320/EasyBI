package com.zys.springbootinit.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zys.springbootinit.model.entity.Chart;
import com.zys.springbootinit.model.entity.User;
import com.zys.springbootinit.service.ChartService;
import com.zys.springbootinit.service.UserService;
import org.checkerframework.checker.units.qual.C;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreCacheJob {

    @Resource
    UserService userService;
    @Resource
    ChartService chartService;
    @Resource
    RedissonClient redissonClient;

    @Resource
    private RedisTemplate redisTemplate;
    private List<Long> mainUserList = Arrays.asList(1L);

    @Scheduled(cron = "0 20 20 * * ? *")
    public void preCache() throws InterruptedException {
        //先加锁
        RLock rlock = redissonClient.getLock("preCacheJobLock");
        if(rlock.tryLock(0,30000L, TimeUnit.MILLISECONDS)){
            System.out.println("拿到锁了："+Thread.currentThread().getId());
            for(long userId:mainUserList){
                QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("userId",userId);
                String key = String.format("easybi:zys:%s",userId);
                Page<Chart> page = chartService.page(new Page<>(), queryWrapper);
                ValueOperations valueOperations = redisTemplate.opsForValue();
                try {
                    valueOperations.set(key,page,30000,TimeUnit.MILLISECONDS);
                }
                finally {
                    if(rlock.isHeldByCurrentThread()){
                        System.out.println("unLock: " + Thread.currentThread().getId());
                        rlock.unlock();
                    }
                }
            }
        }
    }
}
