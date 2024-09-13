package cn.bcs.system.service;

import cn.bcs.common.core.domain.entity.SysDictData;
import cn.bcs.common.utils.DictUtils;
import cn.bcs.system.mapper.SysDictDataMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author ruoyi
 */
@Service
public class SysDictDataService extends ServiceImpl<SysDictDataMapper, SysDictData> {
    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @SuppressWarnings({"unchecked"})
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysDictData data = this.getById(dictCode);
            this.removeById(dictCode);
            List<SysDictData> list = this.lambdaQuery().eq(SysDictData::getDictType, data.getDictType()).orderByAsc(SysDictData::getDictSort).list();
            DictUtils.setDictCache(data.getDictType(), list);
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @SuppressWarnings({"unchecked"})
    public int insertDictData(SysDictData data) {
        int row = baseMapper.insert(data);
        if (row > 0) {
            List<SysDictData> list = this.lambdaQuery().eq(SysDictData::getDictType, data.getDictType()).orderByAsc(SysDictData::getDictSort).list();
            DictUtils.setDictCache(data.getDictType(), list);
        }
        return row;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @SuppressWarnings({"unchecked"})
    public int updateDictData(SysDictData data) {
        int row = baseMapper.updateById(data);
        if (row > 0) {
            List<SysDictData> list = this.lambdaQuery().eq(SysDictData::getDictType, data.getDictType()).orderByAsc(SysDictData::getDictSort).list();
            DictUtils.setDictCache(data.getDictType(), list);
        }
        return row;
    }
}
