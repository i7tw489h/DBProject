package com.campus.canteen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.campus.canteen.entity.PetScore;
import com.campus.canteen.service.PetScoreService;
import com.campus.canteen.common.Result;

@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetScoreService petScoreService;

    //获取用户宠物积分信息
    @GetMapping("/get/{userId}")
    public Result<PetScore> getPetByUserId(@PathVariable Long userId){
        PetScore petScore = petScoreService.getByUserId(userId);
        return Result.success(petScore);
    }

    //更新宠物积分（打卡之后调用）
    @PostMapping("/update")
    public Result updatePetScore(@RequestBody PetScore petScore){
        petScoreService.saveOrUpdate(petScore);
        return Result.success();
    }
}
