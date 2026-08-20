package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.DietRecord;
import java.util.List;

public interface DietRecordService extends IService<DietRecord> {
    List<DietRecord> listByUserId(Long userId);
}