package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.Window;
import com.campus.canteen.mapper.WindowMapper;
import com.campus.canteen.service.WindowService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WindowServiceImpl extends ServiceImpl<WindowMapper, Window> implements WindowService {

    @Override
    public List<Window> getAllWindows() {
        LambdaQueryWrapper<Window> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Window::getIsActive, 1);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<Window> getWindowsByFloor(Integer floor) {
        LambdaQueryWrapper<Window> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Window::getFloor, floor)
               .eq(Window::getIsActive, 1);
        return this.baseMapper.selectList(wrapper);
    }
}