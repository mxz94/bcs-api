package cn.bcs.framework.web.service;

import cn.bcs.common.constant.Constants;
import cn.bcs.common.constant.UserConstants;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.core.domain.model.LoginUser;
import cn.bcs.common.core.redis.RedisCache;
import cn.bcs.common.enums.CommonStatusEnum;
import cn.bcs.common.enums.SysUserType;
import cn.bcs.common.exception.ServiceException;
import cn.bcs.common.exception.user.BlackListException;
import cn.bcs.common.exception.user.UserNotExistsException;
import cn.bcs.common.exception.user.UserPasswordNotMatchException;
import cn.bcs.common.utils.MessageUtils;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.common.utils.StringUtils;
import cn.bcs.common.utils.ip.IpUtils;
import cn.bcs.framework.manager.AsyncManager;
import cn.bcs.framework.manager.factory.AsyncFactory;
import cn.bcs.framework.security.context.AuthenticationContextHolder;
import cn.bcs.system.domain.vo.LoginVO;
import cn.bcs.system.service.SysConfigService;
import cn.bcs.system.service.SysUserService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysConfigService configService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public String login(String username, String password) {
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, "用户不存在/密码错误"));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, "登录成功"));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //记录登录信息
        userService.updateLoginInfo(loginUser.getUserId(), IpUtils.getIpAddr(), new Date());
        // 生成token
        return tokenService.createToken(loginUser);
    }
    public Result<LoginVO> logRegister(String userName, String password, Long tenantId) {
        SysUser sysUser = userService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, userName));
        if (BeanUtil.isEmpty(sysUser)) {
            // 不存在，添加用户
            sysUser = new SysUser().setUserName(userName)
                    .setNickName(userName)
                    .setUserType(SysUserType.PUTONG.getCode())
                    .setTenantId(Long.valueOf(tenantId))
                    .setAvatar("")
                    .setPassword(SecurityUtils.encryptPassword(password))
                    .setStatus(CommonStatusEnum.NORMAL.getCode());
            userService.save(sysUser);
        }
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGIN_FAIL, "用户不存在/密码错误"));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGIN_SUCCESS, "登录成功"));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //记录登录信息
        userService.updateLoginInfo(loginUser.getUserId(), IpUtils.getIpAddr(), new Date());
        // 生成token
        String token = tokenService.createToken(loginUser);
        return Result.success(new LoginVO().setToken(token));
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, "* 必须填写"));
            throw new UserNotExistsException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, "很遗憾，访问IP已被列入系统黑名单"));
            throw new BlackListException();
        }
    }
}
