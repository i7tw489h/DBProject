package com.campus.canteen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.campus.canteen.entity.SmokeAlcoholRecord;
import com.campus.canteen.service.SmokeAlcoholRecordService;
import com.campus.canteen.common.Result;
import java.util.List;

@RestController
@RequestMapping("/smokeAlcohol")
public class SmokeAlcoholController {

    @Autowired
    private SmokeAlcoholRecordService smokeAlcoholRecordService;

    //新增烟酒记录
    @PostMapping("/add")
    public Result add(@RequestBody SmokeAlcoholRecord record){
        smokeAlcoholRecordService.save(record);
        return Result.success();
    }

    //查询该用户全部烟酒记录
    @GetMapping("/list/{userId}")
    public Result<List<SmokeAlcoholRecord>> listByUserId(@PathVariable Long userId){
        List<SmokeAlcoholRecord> list = smokeAlcoholRecordService.listByUserId(userId);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        smokeAlcoholRecordService.removeById(id);
        return Result.success();
    }
}