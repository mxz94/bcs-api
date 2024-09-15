package cn.bcs.web.third.service;

import cn.bcs.web.third.domain.ThirdRequestRecord;
import cn.bcs.web.third.mapper.ThirdRequestRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author m
 * @date 2022/5/26 11:43
 */
@Service
public class ThirdRequestRecordService extends ServiceImpl<ThirdRequestRecordMapper, ThirdRequestRecord> {
    /**
     * 保存记录
     *
     * @param url         请求地址
     * @param description 描述
     * @param param       发送参数
     * @param result      返回结果
     */
    public void saveRecords(String url, String description, String param, String result) {
        this.save(new ThirdRequestRecord().setUrl(url).setDescription(description).setParam(param).setResult(result));
    }
}
