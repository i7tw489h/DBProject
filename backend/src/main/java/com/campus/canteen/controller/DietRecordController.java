package com.campus.canteen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.campus.canteen.entity.DietRecord;
import com.campus.canteen.service.DietRecordService;
import com.campus.canteen.common.Result;
import java.util.List;

@RestController
@RequestMapping("/dietRecord")
public class DietRecordController {

    @Autowired
    private DietRecordService dietRecordService;

    //新增饮食打卡
    @PostMapping("/add")
    public Result add(@RequestBody DietRecord dietRecord){
        dietRecordService.save(dietRecord);
        return Result.success();
    }

    //根据用户id查询全部打卡记录
    @GetMapping("/list/{userId}")
    public Result<List<DietRecord>> listByUserId(@PathVariable Long userId){
        List<DietRecord> list = dietRecordService.listByUserId(userId);
        return Result.success(list);
    }

    //删除打卡记录
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        dietRecordService.removeById(id);
        return Result.success();
    }
}