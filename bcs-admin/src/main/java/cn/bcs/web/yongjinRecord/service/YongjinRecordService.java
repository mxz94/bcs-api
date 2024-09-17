package cn.bcs.web.yongjinRecord.service;
import java.util.Date;

import java.math.BigDecimal;
import java.util.List;

import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.utils.BigDecimalUtils;
import cn.bcs.web.yongjinRecord.domain.YongjinRecord;
import cn.bcs.web.yongjinRecord.mapper.YongjinRecordMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 佣金分成记录Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-17
 */
@Service
public class  YongjinRecordService extends ServiceImpl<YongjinRecordMapper, YongjinRecord> {

    public void addRecord(SysUser user, BigDecimal fee, Long recordId) {
        YongjinRecord record = new YongjinRecord();
        record.setRecordId(recordId);
        record.setFee(fee);
        record.setUserId(user.getUserId());
        record.setOldBalance(BigDecimalUtils.add(user.getBalance(), user.getWaitInBalance()));
        record.setNewBalance(BigDecimalUtils.add(user.getBalance(), user.getWaitInBalance(), fee));
        this.save(record);
    }
}
