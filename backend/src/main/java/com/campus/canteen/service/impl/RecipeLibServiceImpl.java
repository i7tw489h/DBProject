package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.RecipeLib;
import com.campus.canteen.mapper.RecipeLibMapper;
import com.campus.canteen.service.RecipeLibService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RecipeLibServiceImpl extends ServiceImpl<RecipeLibMapper, RecipeLib> implements RecipeLibService {

    private static final Logger logger = LoggerFactory.getLogger(RecipeLibServiceImpl.class);

}
