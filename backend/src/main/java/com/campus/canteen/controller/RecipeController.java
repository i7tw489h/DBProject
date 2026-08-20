package com.campus.canteen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.campus.canteen.entity.RecipeLib;
import com.campus.canteen.service.RecipeLibService;
import com.campus.canteen.common.Result;
import java.util.List;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @Autowired
    private RecipeLibService recipeLibService;

    //查询全部食谱
    @GetMapping("/list")
    public Result<List<RecipeLib>> listAll(){
        List<RecipeLib> list = recipeLibService.list();
        return Result.success(list);
    }

    //新增食谱
    @PostMapping("/add")
    public Result add(@RequestBody RecipeLib recipeLib){
        recipeLibService.save(recipeLib);
        return Result.success();
    }

    //根据id查询单条食谱
    @GetMapping("/get/{id}")
    public Result<RecipeLib> getById(@PathVariable Long id){
        RecipeLib recipeLib = recipeLibService.getById(id);
        return Result.success(recipeLib);
    }
}
