package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.Window;

import java.util.List;

public interface WindowService extends IService<Window> {
    List<Window> getAllWindows();
    List<Window> getWindowsByFloor(Integer floor);
}