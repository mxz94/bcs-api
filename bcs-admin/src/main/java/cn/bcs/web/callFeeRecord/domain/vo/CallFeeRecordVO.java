package cn.bcs.web.callFeeRecord.domain.vo;

import cn.bcs.web.callFeeRecord.domain.CallFeeRecord;
import lombok.Data;

@Data
public class CallFeeRecordVO extends CallFeeRecord {
    private String recordNickName;

    private String nickName;
}
