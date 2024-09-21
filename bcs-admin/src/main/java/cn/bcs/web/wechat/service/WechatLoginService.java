package cn.bcs.web.wechat.service;

import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.core.domain.model.LoginUser;
import cn.bcs.common.enums.CommonStatusEnum;
import cn.bcs.common.enums.SysUserType;
import cn.bcs.common.utils.BigDecimalUtils;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.common.utils.ip.IpUtils;
import cn.bcs.framework.web.service.TokenService;
import cn.bcs.system.domain.vo.LoginVO;
import cn.bcs.system.service.SysUserService;
import cn.bcs.web.third.domain.bo.WechatAccessTokenBO;
import cn.bcs.web.third.domain.bo.WechatUserInfoBO;
import cn.bcs.web.third.support.WechatSupport;
import cn.bcs.web.wechat.domain.vo.WechatUserInfo;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class WechatLoginService {
    @Resource
    private TokenService tokenService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private SysUserService userService;
    @Resource
    private WechatSupport wechatSupport;

    /**
     * 微信登录
     *
     * @return 123
     */
    public Result<LoginVO> wechatLogin1(WechatAccessTokenBO accessTokenBO, String tenantId) {
        if (BeanUtil.isEmpty(accessTokenBO)) {
            return Result.error("微信授权失败");
        }
        WechatUserInfoBO wechatUserInfoBO = wechatSupport.getUserinfo(accessTokenBO.getAccess_token(), accessTokenBO.getOpenid());
        if (BeanUtil.isEmpty(wechatUserInfoBO) || StringUtils.isEmpty(wechatUserInfoBO.getOpenid())) {
            return Result.error("微信授权失败");
        }
        String userName = wechatUserInfoBO.getOpenid() + ":" + tenantId;
        SysUser sysUser = userService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, userName));
        if (BeanUtil.isEmpty(sysUser)) {
            // 不存在，添加用户
            sysUser = new SysUser().setUserName(userName)
                    .setNickName(wechatUserInfoBO.getNickname())
                    .setUserType(SysUserType.PUTONG.getCode())
                    .setTenantId(Long.valueOf(tenantId))
                    .setAvatar(wechatUserInfoBO.getHeadimgurl())
                    .setPassword(SecurityUtils.encryptPassword(userName))
                    .setStatus(CommonStatusEnum.NORMAL.getCode());
            sysUser.setRemark(wechatUserInfoBO.getUnionid());
            userService.save(sysUser);
        }
        // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(sysUser.getUserName(), sysUser.getUserName()));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUser());
        // 生成token
        String token = tokenService.createToken(loginUser);
        return Result.success(new LoginVO().setToken(token));
    }

    /**
     * 微信登录
     *
     * @param code 微信code
     * @return 123
     */
    public Result<LoginVO> wechatLogin(String code) {
        WechatUserInfoBO wechatUserInfoBO = wechatSupport.codeGetUserinfo(code);
        if (BeanUtil.isEmpty(wechatUserInfoBO) || StringUtils.isEmpty(wechatUserInfoBO.getOpenid())) {
            return Result.error("微信授权失败");
        }
        SysUser sysUser = userService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, wechatUserInfoBO.getOpenid()));
        if (BeanUtil.isEmpty(sysUser)) {
            // 不存在，添加用户
            sysUser = new SysUser().setUserName(wechatUserInfoBO.getOpenid())
                    .setNickName(wechatUserInfoBO.getNickname())
                    .setUserType(SysUserType.PUTONG.getCode())
                    .setAvatar(wechatUserInfoBO.getHeadimgurl())
                    .setTenantId(1L)
                    .setPassword(SecurityUtils.encryptPassword(wechatUserInfoBO.getOpenid()))
                    .setStatus(CommonStatusEnum.NORMAL.getCode());
            sysUser.setRemark(wechatUserInfoBO.getUnionid());
            userService.save(sysUser);
        }
        // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(sysUser.getUserName(), sysUser.getUserName()));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUser());
        // 生成token
        String token = tokenService.createToken(loginUser);
        return Result.success(new LoginVO().setToken(token));
    }
    /**
     * 记录登录信息
     */
    public void recordLoginInfo(SysUser user) {
        user.setLoginIp(IpUtils.getIpAddr());
        user.setLoginDate(new Date());
        userService.updateById(user);
    }

    public Result<WechatUserInfo> userInfo() {
        SysUser byId = userService.getById(SecurityUtils.getUserId());
        WechatUserInfo wechatUserInfo = BeanUtil.copyProperties(byId, WechatUserInfo.class);
        if (byId.getFromUserId() != null) {
            SysUser byId1 = userService.getById(byId.getFromUserId());
            if (byId1 != null) wechatUserInfo.setFromUserNickName(byId1.getNickName());
        }
        wechatUserInfo.setShareUrl("http://www.baidu.com");
        wechatUserInfo.setAllBalance(BigDecimalUtils.add(wechatUserInfo.getBalance(), wechatUserInfo.getWaitInBalance()));
        wechatUserInfo.setAllCallBalance(BigDecimalUtils.add(wechatUserInfo.getCallBalance(), wechatUserInfo.getTeamBuildBalance()));
        wechatUserInfo.setPassword(null);
        return Result.success(wechatUserInfo);
    }
}
