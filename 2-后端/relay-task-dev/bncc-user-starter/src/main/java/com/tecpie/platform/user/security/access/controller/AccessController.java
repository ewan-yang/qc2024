package com.tecpie.platform.user.security.access.controller;

import cn.hutool.core.util.RandomUtil;
import com.google.code.kaptcha.Producer;
import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.library.common.util.cache.TecpieCacheUtil;
import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.platform.user.business.system.organization.service.SysUserService;
import com.tecpie.platform.user.constants.ResultCode;
import com.tecpie.platform.user.constants.SecurityConstant;
import com.tecpie.platform.user.security.auth.model.UserMoibleVerifyCommand;
import com.tecpie.platform.user.security.auth.model.ValidateCode;
import com.tecpie.platform.user.security.igw.service.IgwService;
import com.tecpie.platform.user.security.igw.service.IgwTestService;
import com.tecpie.platform.user.security.isc.service.IscService;
import com.tecpie.platform.user.utils.MsgUtils;
import com.tecpie.starter.webmvc.util.I18nUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * description 登录信息管理
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author ZhangYH
 * @date 2020/3/4
 */

@Slf4j
@Tag(name = "登录信息管理")
@RestController
@RequestMapping("/api/v1/access")
public class AccessController {

    @Value("${tecpie.security.kaptcha.expireTime:0}")
    private Long kaptchaExpireTime;

    @Value("${tecpie.security.msgVerify.expireTime:0}")
    private Long msgVerifyCodeExpireTime;

    @Value("${tecpie.security.msgVerify.length:0}")
    private Integer msgVerifyCodeLength;

    public static final String VERIFY_MSG_TEMPLATE_CODE = "VERIFY_MSG_TEMPLATE";

    @Autowired
    private SysUserService userService;

    @Resource
    private Producer kaptchaProducer;

    @Resource
    private IgwService igwService;

    @Resource
    private IscService iscService;

    @Resource
    private IgwTestService igwTestService;

    /**
     * 产生验证码
     *
     * @param request  request
     * @param response response
     */
    @GetMapping("/kaptcha")
    @Operation(summary = "获取图片验证码")
    public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) {
        // Set to expire far in the past
        response.setDateHeader("Expires", 0);
        // Set Standard HTTP/1.1 no-cache headers
        response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
        // Set IE Extended HTTP/1.1 no-cache headers
        response.addHeader("Cache-Control", "post-check=0,pre-check=0");
        // Set Standard HTTP/1.0 no-cache headers
        response.setHeader("Pragma", "no-cache");
        // Return a image
        response.setContentType("image/jpeg");
        String kapText = kaptchaProducer.createText();
        // Store the text in the Session
        ValidateCode validateCode = new ValidateCode(kapText);

        // 将Kaptcha信息存放至缓存中
        String cacheKey = String.format("%s:%s", SecurityConstant.CACHE_KEY_OF_KAPTCHA, Base64Utils.encodeToString(request.getRemoteAddr().getBytes()));
        TecpieCacheUtil.put(SecurityConstant.CACHE_NAME_USER_STARTER, cacheKey, validateCode, kaptchaExpireTime, TimeUnit.SECONDS);

        // Create the image with the text
        BufferedImage bi = kaptchaProducer.createImage(kapText);
        // Write the data out
        try (ServletOutputStream output = response.getOutputStream()) {
            ImageIO.write(bi, "jpg", output);
            output.flush();
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 产生短信验证码
     *
     * @param mobile 手机号
     */
    @GetMapping("/msgVerifyCode")
    @Operation(summary = "获取短信验证码")
    public Result getMsgVerifyCode(@RequestParam("mobile") String mobile,
                                   @RequestParam(value = "pwdVerifyCode", required = false, defaultValue = "false") Boolean pwdVerifyCode) {
        // 将Kaptcha信息存放至缓存中
        String cacheKey = String.format("%s:%s",
                Boolean.TRUE.equals(pwdVerifyCode) ? SecurityConstant.CACHE_KEY_OF_PWD_VERIFY_CODE
                        : SecurityConstant.CACHE_KEY_OF_MSG_VERIFY_CODE, mobile);
        if (TecpieCacheUtil.get(SecurityConstant.CACHE_NAME_USER_STARTER, cacheKey) != null) {
            return Result.error(ResultCode.VERIFY_CODE_SENDED);
        }
        String verifyCode = RandomUtil.randomNumbers(msgVerifyCodeLength);
        TecpieCacheUtil.put(SecurityConstant.CACHE_NAME_USER_STARTER, cacheKey, verifyCode,
                msgVerifyCodeExpireTime, TimeUnit.SECONDS);
        return Result.success(MsgUtils
                .sendMsg(mobile, I18nUtil.getMessage(VERIFY_MSG_TEMPLATE_CODE,
                        new String[]{verifyCode, String.valueOf(msgVerifyCodeExpireTime / 60)})));
    }

    /**
     * 重置密码
     */
    @PutMapping("/resetPwd")
    @Operation(summary = "重置密码")
    public Result resetPwd(@RequestBody UserMoibleVerifyCommand resetPwdCommand) {
        // 验证码校验
        String cacheKey = String
                .format("%s:%s", SecurityConstant.CACHE_KEY_OF_PWD_VERIFY_CODE,
                        resetPwdCommand.getMobile());
        String verifyCode = (String) TecpieCacheUtil
                .get(SecurityConstant.CACHE_NAME_USER_STARTER, cacheKey);
        if (!resetPwdCommand.getVerifyCode().equalsIgnoreCase(verifyCode)) {
            return Result.error(ResultCode.KAPTCHA_ERROR);
        }
        TecpieCacheUtil.remove(SecurityConstant.CACHE_NAME_USER_STARTER, cacheKey);
        //下线已登录用户
        userService.lambdaQuery()
                .eq(SysUser::getMobile, resetPwdCommand.getMobile()).list().forEach(user -> {
            String tokenCacheKey = String
                    .format("%s:%s", SecurityConstant.CACHE_KEY_OF_TOKEN, user.getId());
            TecpieCacheUtil.removeCommon(SecurityConstant.CACHE_NAME_USER_STARTER, tokenCacheKey);
        });
        //修改密码
        return Result.success(
                userService.lambdaUpdate().eq(SysUser::getMobile, resetPwdCommand.getMobile())
                        .set(SysUser::getPassword, resetPwdCommand.getPassword())
                        .set(SysUser::getPasswordDate, LocalDateTime.now()).update());
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    @Operation(summary = "注册")
    public Result register(@RequestBody UserMoibleVerifyCommand resetPwdCommand) {
        // 验证码校验
        String cacheKey = String
                .format("%s:%s", SecurityConstant.CACHE_KEY_OF_PWD_VERIFY_CODE,
                        resetPwdCommand.getMobile());
        String verifyCode = (String) TecpieCacheUtil
                .get(SecurityConstant.CACHE_NAME_USER_STARTER, cacheKey);
        if (!resetPwdCommand.getVerifyCode().equalsIgnoreCase(verifyCode)) {
            return Result.error(ResultCode.KAPTCHA_ERROR);
        }
        TecpieCacheUtil.remove(SecurityConstant.CACHE_NAME_USER_STARTER, cacheKey);
        if (userService.lambdaQuery().eq(SysUser::getCode, resetPwdCommand.getMobile()).or()
                .eq(SysUser::getMobile, resetPwdCommand.getMobile()).exists()) {
            return Result.error(ResultCode.ACCOUNT_HAS_REGISTERED);
        }
        //新建用户
        SysUser user = new SysUser();
        user.setCode(resetPwdCommand.getMobile());
        user.setName(user.getCode());
        user.setMobile(resetPwdCommand.getMobile());
        user.setPassword(resetPwdCommand.getPassword());
        user.setPasswordDate(LocalDateTime.now());
        return Result.success(userService.save(user));
    }

    /**
     * 用户被踢出
     *
     * @return Result
     */
    @GetMapping(value = "/kickout")
    @Operation(summary = "用户被踢出")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<String> kickout(@RequestParam("id") Serializable userId) {
        String tokenCacheKey = String.format("%s:%s", SecurityConstant.CACHE_KEY_OF_TOKEN, userId);
        TecpieCacheUtil.removeCommon(SecurityConstant.CACHE_NAME_USER_STARTER, tokenCacheKey);
        return Result.success();
    }

    /**
     * i国网登录
     *
     * @param serviceTicket
     * @return Result<Map < String, Object>>
     */
    @GetMapping("igwLogin")
    @Operation(summary = "i国网登录")
    public Result<Map<String, Object>> igwLogin(@RequestHeader(value = "Client", required = false) String client,@RequestParam String serviceTicket) throws UnsupportedEncodingException {
        return Result.success(igwService.oauth(client,serviceTicket));
    }

    /**
     * isc登录
     *
     * @param accessToken
     * @return Result<Map < String, Object>>
     */
    @GetMapping("iscLogin")
    @Operation(summary = "isc登录")
    public Result<Map<String, Object>> iscSso(@RequestHeader(value = "Client", required = false) String client,@RequestParam("accessToken") String accessToken) {
        return Result.success(iscService.oauth(client,accessToken));
    }

    /**
     * 测试方法
     */
    @GetMapping("testMethod")
    @Operation(summary = "测试方法")
    public Result<String> testMethod() {
        log.info("测试方法");
        return Result.success("测试方法");
    }
}
