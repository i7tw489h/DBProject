package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.SmokeAlcoholRecord;
import java.util.List;

public interface SmokeAlcoholRecordService extends IService<SmokeAlcoholRecord> {
    List<SmokeAlcoholRecord> listByUserId(Long userId);
}