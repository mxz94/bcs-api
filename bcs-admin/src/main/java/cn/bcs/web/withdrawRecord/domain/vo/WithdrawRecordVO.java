package cn.bcs.web.withdrawRecord.domain.vo;

import cn.bcs.web.withdrawRecord.domain.WithdrawRecord;
import lombok.Data;

@Data
public class WithdrawRecordVO extends WithdrawRecord {
    private String shoukuanUrl;

    private String nickName;
}
