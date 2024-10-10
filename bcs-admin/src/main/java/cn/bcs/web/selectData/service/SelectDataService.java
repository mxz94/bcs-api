package cn.bcs.web.selectData.service;

import java.util.ArrayList;
import java.util.List;

import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.common.utils.StringUtils;
import cn.bcs.web.selectData.domain.SelectData;
import cn.bcs.web.selectData.domain.vo.SelectDataQuery;
import cn.bcs.web.selectData.domain.vo.SelectDataVO;
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
                .eq(SelectData::getTenantId, query.getTenantId() != null ? query.getTenantId() : SecurityUtils.getTenantId())
                .eq(StringUtils.isNotEmpty(query.getStatus()), SelectData::getStatus, query.getStatus())
                .orderByDesc(SelectData::getSortNum)
                .list();
    }

    public List<SelectDataVO> listAll(SelectDataQuery query) {
        List<SelectData> list = this.lambdaQuery().like(StringUtils.isNotEmpty(query.getName()), SelectData::getName, query.getName())
                .eq(StringUtils.isNotEmpty(query.getType()), SelectData::getType, query.getType())
                .eq(SelectData::getTenantId, SecurityUtils.getTenantId())
                .eq(SelectData::getStatus, 0)
                .orderByDesc(SelectData::getSortNum)
                .list();
        List<SelectDataVO> selectDataVOList = new ArrayList<>();

        for (SelectData selectData : list) {
            SelectDataVO vo = new SelectDataVO();
            vo.setText(selectData.getName());
            vo.setValue(selectData.getId().toString()); // 假设 `value` 字段与 `name` 相同，可以根据实际需求调整
            selectDataVOList.add(vo);
        }

        return selectDataVOList;
    }
}
