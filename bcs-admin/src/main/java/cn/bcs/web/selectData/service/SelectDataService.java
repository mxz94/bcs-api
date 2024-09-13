package cn.bcs.web.selectData.service;

import java.util.List;

import cn.bcs.common.utils.StringUtils;
import cn.bcs.web.selectData.domain.SelectData;
import cn.bcs.web.selectData.domain.vo.SelectDataQuery;
import cn.bcs.web.selectData.mapper.SelectDataMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 选择内容Service业务层处理
 *
 * @author ruoyi
 * @date 2023-05-09
 */
@Service
public class  SelectDataService extends ServiceImpl<SelectDataMapper, SelectData> {

    public List<SelectData> selectList(SelectDataQuery query) {
        return this.lambdaQuery().like(StringUtils.isNotEmpty(query.getName()), SelectData::getName, query.getName())
                .eq(StringUtils.isNotEmpty(query.getType()), SelectData::getType, query.getType())
                .eq(StringUtils.isNotEmpty(query.getStatus()), SelectData::getStatus, query.getStatus())
                .eq(SelectData::getTenantId, query.getTenantId())
                .orderByDesc(SelectData::getCreateTime)
                .list();
    }
}
