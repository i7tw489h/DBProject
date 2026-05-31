package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<Category> getAllCategories();
}