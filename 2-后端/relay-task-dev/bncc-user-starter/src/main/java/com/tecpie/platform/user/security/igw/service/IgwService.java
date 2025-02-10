package com.tecpie.platform.user.security.igw.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aostarit.mobile.client.message.api.client.MsgRequestClient;
import com.aostarit.mobile.client.message.api.client.MsgSendClient;
import com.aostarit.mobile.client.message.api.config.WxMsgConfig;
import com.aostarit.mobile.client.message.api.entity.SendMsgParams;
import com.aostarit.mobile.client.message.api.entity.WxSendMsgResult;
import com.aostarit.mobile.client.message.api.utils.EnDecryptUtil;
import com.aostarit.sgid.agent.EncryptHelper;
import com.aostarit.smcrypto.exception.InvalidKeyException;
import com.aostarit.smcrypto.exception.InvalidSourceDataException;
import com.google.common.collect.Lists;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.platform.user.business.system.organization.entity.SysRole;
import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.platform.user.business.system.organization.entity.SysUserRole;
import com.tecpie.platform.user.business.system.organization.entity.SysUserUnit;
import com.tecpie.platform.user.business.system.organization.service.SysRoleService;
import com.tecpie.platform.user.business.system.organization.service.SysUserRoleService;
import com.tecpie.platform.user.business.system.organization.service.SysUserService;
import com.tecpie.platform.user.business.system.organization.service.SysUserUnitService;
import com.tecpie.platform.user.constants.ResultCode;
import com.tecpie.platform.user.security.auth.model.SysUserDetails;
import com.tecpie.platform.user.security.igw.entity.IgwUser;
import com.tecpie.platform.user.security.igw.util.IgwUtils;
import com.tecpie.platform.user.utils.SecurityUtils;
import com.tecpie.starter.feign.entity.SecurityToken;
import com.tecpie.starter.feign.entity.SecurityUser;
import com.tecpie.starter.security.support.cache.UserCacheHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * i国网登录授权Service类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @Author Tanzj
 * @Date 2023/06/01
 */
@Service
@Slf4j
public class IgwService {

    @Value("${igw.secret}")
    private String clientSecret;

    @Value("${igw.signKey}")
    private String signKey;

    @Value("${igw.applicationAppId}")
    private String applicationAppId;

    @Value("${igw.messageAppId:}")
    private String messageAppId;

    @Value("${igw.producerId}")
    private String producerId;

    @Value("${igw.sm4Key}")
    private String sm4Key;

    @Value("${igw.sm2PubKey}")
    private String sm2PubKey;

    @Value("${igw.msgBaseUrl}")
    private String msgBaseUrl;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysUserUnitService userUnitService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SecurityUtils securityUtils;

    private MsgSendClient msgSendClient;

    /**
     * 初始化消息发送
     */
    private void init() {
        if (msgSendClient != null) {
            return;
        }

        WxMsgConfig wxMsgConfig = new WxMsgConfig();
        wxMsgConfig.setBaseUrl(msgBaseUrl);
        wxMsgConfig.setProducerId(producerId);
        wxMsgConfig.setSm4Key(sm4Key);
        wxMsgConfig.setSm2PublicKey(sm2PubKey);
        // 以下三个Bean 建议在项目中以单例形式存在
        MsgRequestClient requestClient = new MsgRequestClient(wxMsgConfig);
        // 消息中心发送client
        msgSendClient = new MsgSendClient(requestClient);
    }

    private IgwUser login(String serviceTicket) {
        try {
            String ret = EnDecryptUtil.transferDencrypt(serviceTicket, clientSecret, signKey);
            return JSON.parseObject(ret, IgwUser.class);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw BusinessException.build(ResultCode.SSO_FAIL);
        }
    }

    public Map<String, Object> oauth(String client, String serviceTicket) {
        log.info("into igw oath method ...");
        // 获取用户信息
        IgwUser user = login(serviceTicket);
        log.info("i国网用户信息(igw user):{}", JSON.toJSONString(user));
        // 用户数据对应系统用户
        SysUser sysUser = userService.selectAuthByCode(user.getNameCode());
        if (ObjectUtils.isEmpty(sysUser)) {
            sysUser = new SysUser();
            // 设置默认名称
            sysUser.setName(user.getName());
            // 设置默认账号
            sysUser.setCode(user.getNameCode());
            // 设置默认密码
            sysUser.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");
            // 设置默认密码过期时间
            sysUser.setPasswordDate(LocalDateTime.now().plusMonths(6));
            // 设置默认性别：男
            sysUser.setSex("男");
            // 默认A工程队
            sysUser.setEngineersTeamId(1);
            userService.save(sysUser);
            log.info("sys user保存成功");
            log.info("系统用户信息(sys user):{}", JSON.toJSONString(sysUser));
            SysUserUnit userUnit = new SysUserUnit();
            userUnit.setUserId(sysUser.getId());
            userUnit.setUnitId(1L);
            userUnitService.save(userUnit);
            log.info("user unit save success");
            log.info("用户单位信息(user unit):{}", JSON.toJSONString(user));
            SysUser finalSysUser = sysUser;
            roleService.lambdaQuery().eq(SysRole::getCode, "sn_admin").oneOpt().ifPresent(ptyh -> {
                // 默认角色权限
                SysRole sysRole = new SysRole();
                sysRole.setCode("sn_admin");
                finalSysUser.setRoleList(Collections.singletonList(sysRole));
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(ptyh.getId());
                sysUserRole.setUserId(finalSysUser.getId());
                userRoleService.save(sysUserRole);
            });

        }
        log.info("生成userDetail");
        SysUserDetails sysUserDetail = new SysUserDetails(sysUser);
        log.info("userDetail生成结束:{}", JSON.toJSONString(sysUserDetail));
        log.info("生成SecurityToken");
        SecurityToken securityToken = securityUtils.generateTokenPair(client, sysUser.getId());
        log.info("SecurityToken生成结束:{}", JSON.toJSONString(securityToken));
        UserCacheHelper.removeSecurityUser(sysUser.getId().toString());
        SecurityUser securityUser = userService.selectSecurityUser(sysUser.getId());
        log.info("获取SecurityUser:{}", JSON.toJSONString(securityUser));
        UserCacheHelper.setSecurityUser(securityUser);
        //整合数据并返回
        sysUserDetail.setToken(securityToken.getToken());
        sysUserDetail.setRefreshToken(securityToken.getRefreshToken());
        log.info("开始构建最终Map");
        return SecurityUtils.convertUser(sysUserDetail);
    }

    public void sendMsg2Users(List<String> userCodes, String content) {
        try {
            init();
            // 构建发送消息参数
            SendMsgParams wechatMsg = new SendMsgParams();
            //发送目标用户
            wechatMsg.setTouser(String.join("|", userCodes));
            // 跳转链接地址
            wechatMsg.setUrl("zipapp://appid" + "." + applicationAppId + "/index.html");
            wechatMsg.setTitle("停电通知新消息");
            wechatMsg.setDescription(content);
            wechatMsg.setType("textcard");
            // 目标应用appId
            wechatMsg.setMessageAppId(messageAppId);

            WxSendMsgResult wxSendMsgResult = msgSendClient.sendMsg(wechatMsg);
            log.info("i国网消息发送结果" + wxSendMsgResult.getErrCode() + ":" + wxSendMsgResult.getErrMsg());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

}