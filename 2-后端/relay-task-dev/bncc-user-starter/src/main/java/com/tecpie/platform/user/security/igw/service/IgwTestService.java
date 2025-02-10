package com.tecpie.platform.user.security.igw.service;

import com.alibaba.fastjson.JSONObject;
import com.aostarit.mobile.client.message.api.client.MsgRequestClient;
import com.aostarit.mobile.client.message.api.client.MsgSendClient;
import com.aostarit.mobile.client.message.api.config.WxMsgConfig;
import com.aostarit.mobile.client.message.api.entity.SendMsgParams;
import com.aostarit.mobile.client.message.api.entity.WxSendMsgResult;
import com.aostarit.sgid.agent.EncryptHelper;
import com.aostarit.smcrypto.exception.InvalidKeyException;
import com.aostarit.smcrypto.exception.InvalidSourceDataException;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
public class IgwTestService {

    @Value("${igw.appId}")
    private String appId;

    @Value("${igw.secret}")
    private String clientSecret;

    @Value("${igw.signKey}")
    private String signKey;

    @Value("${igw.service}")
    private String service;

    @Value("${igw.baseUrl}")
    private String baseUrl;

    @Value("${igw.accessTokenPath}")
    private String accessTokenPath;

    @Value("${igw.ticketPath}")
    private String ticketPath;

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

    @Value("${tecpie.security.tokenExpireTime}")
    private int tokenExpireTime;

    private SysUserService userService;

    private SysUserUnitService userUnitService;

    private SysUserRoleService userRoleService;

    private SysRoleService roleService;

    private MsgSendClient msgSendClient;

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    public static final String APP_ID_KEY = "appId";

    @Autowired
    public void setIgwService(SysUserService userService, SysUserUnitService userUnitService, SysUserRoleService userRoleService, SysRoleService roleService) {
        this.userService = userService;
        this.userUnitService = userUnitService;
        this.userRoleService = userRoleService;
        this.roleService = roleService;
    }

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

    /**
     * 获取AccessToken
     */
    public String getAccessTokenForTest() {
        JSONObject requestParam = new JSONObject();
        String appIdKey = "appId";
        requestParam.put(APP_ID_KEY, appId);
        requestParam.put("clientSecret", clientSecret);
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add(APP_ID_KEY, appId);
        param.add("clientSecret", clientSecret);
        HttpHeaders headers = getIscHeadersForTest(paramSignForTest(requestParam));
        headers.setContentLength(requestParam.toJSONString().getBytes().length);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(requestParam, headers);
        RestTemplate restTemplate = new RestTemplate();
        log.info("发出单点登录请求:" + httpEntity.toString());
        JSONObject result = restTemplate
                .postForObject(baseUrl.concat(accessTokenPath), httpEntity, JSONObject.class);
        StringBuilder accessToken = new StringBuilder("");
        Optional.ofNullable(result).ifPresentOrElse(r -> {
            log.info("登录AccessToken获取" + r.toJSONString());
            Optional.ofNullable(r.getJSONObject("data")).ifPresentOrElse(
                    data -> accessToken.append("Client ").append(data.getString("accessToken")), () -> {
                        throw BusinessException.build("SSO获取AccessToken失败" + r.toJSONString());
                    });
        }, () -> {
            throw BusinessException.build("SSO获取AccessToken失败");
        });
        return accessToken.toString();
    }

    private HttpHeaders getIscHeadersForTest(String sign) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", CONTENT_TYPE);
        headers.add("Accept", CONTENT_TYPE);
        headers.add("X-Acloud-Data-Sign", sign);
        headers.add("X-Clientid", appId);
        headers.add("Accept-Encoding", "gzip, deflate, br");
        return headers;
    }

    private String paramSignForTest(JSONObject requestParam) {
        try {
            return EncryptHelper.sign(signKey, requestParam.toJSONString());
        } catch (InvalidSourceDataException | InvalidKeyException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return requestParam.toJSONString();
    }

    private IgwUser loginForTest(String accessToken, String serviceTicket) {
        JSONObject requestParam = new JSONObject();
        requestParam.put(APP_ID_KEY, appId);
        requestParam.put("service", service);
        requestParam.put("ticket", serviceTicket);
        HttpHeaders iscHeaders = getIscHeadersForTest(paramSignForTest(requestParam));
        iscHeaders.add("X-ISC-AccessToken", accessToken);
        iscHeaders.setContentLength(requestParam.toJSONString().getBytes().length);
//        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
//        param.add("appId", appId);
//        param.add("service", service);
//        param.add("ticket", serviceTicket);
        HttpEntity httpEntity = new HttpEntity<>(requestParam, iscHeaders);
        RestTemplate restTemplate = new RestTemplate();
        JSONObject result = restTemplate
                .postForObject(baseUrl.concat(ticketPath), httpEntity, JSONObject.class);
        log.info("login-result:" + result);
        if (result != null && "100001".equals(result.getString("code"))) {
            return result.getObject("data", IgwUser.class);
        } else {
            throw BusinessException.build(ResultCode.SSO_FAIL);
        }
    }

    /**
     * 授权
     */
    public Map<String, Object> oauthForTest(String serviceTicket) {
        // 获取accessToken
        String accessToken = getAccessTokenForTest();
        log.info("accessToken:" + accessToken);
        // 获取用户信息
        IgwUser user = loginForTest(accessToken, serviceTicket);
        // 用户数据对应系统用户
        SysUser sysUser = userService.selectAuthByCode(user.getNameCode());
        if (ObjectUtils.isEmpty(sysUser)) {
            sysUser = new SysUser();
            sysUser.setName(user.getName());
            sysUser.setCode(user.getNameCode());
            userService.save(sysUser);
            SysUserUnit userUnit = new SysUserUnit();
            userUnit.setUserId(sysUser.getId());
            userUnit.setUnitId(1L);
            userUnitService.save(userUnit);
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
        SysUserDetails sysUserDetail = new SysUserDetails(sysUser);
        // 整合数据并返回
        return IgwUtils.getSecurityUser(sysUserDetail, userService, tokenExpireTime);
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
