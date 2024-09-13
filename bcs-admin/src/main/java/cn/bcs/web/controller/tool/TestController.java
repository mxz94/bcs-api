package cn.bcs.web.controller.tool;

import cn.bcs.common.core.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * swagger 用户测试方法
 *
 * @author ruoyi
 */
@RestController
public class TestController extends BaseController {

    @GetMapping(value = "/ok.html")
    public String ok() {
        return "success";
    }
}
