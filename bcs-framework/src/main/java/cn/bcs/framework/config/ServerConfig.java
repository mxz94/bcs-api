package cn.bcs.framework.config;

import cn.bcs.common.utils.ServletUtils;
import cn.bcs.common.utils.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 服务相关配置
 *
 * @author ruoyi
 */
@Component
public class ServerConfig {
    public static String getDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return StringUtils.replace(url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString(), "http:", "https:");
    }

    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     *
     * @return 服务地址
     */
    public String getUrl() {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }
}
