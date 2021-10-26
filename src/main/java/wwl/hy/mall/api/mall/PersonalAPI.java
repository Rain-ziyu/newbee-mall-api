/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package wwl.hy.mall.api.mall;

import io.swagger.annotations.*;
import wwl.hy.mall.api.mall.param.MallUserLoginParam;
import wwl.hy.mall.api.mall.param.MallUserRegisterParam;
import wwl.hy.mall.api.mall.param.MallUserUpdateParam;
import wwl.hy.mall.api.mall.param.UserHistoryParam;
import wwl.hy.mall.api.mall.vo.UserHisInfoVO;
import wwl.hy.mall.common.Constants;
import wwl.hy.mall.common.ServiceResultEnum;
import wwl.hy.mall.config.annotation.TokenToMallUser;
import wwl.hy.mall.api.mall.vo.NewBeeMallUserVO;
import wwl.hy.mall.entity.MallUser;
import wwl.hy.mall.entity.UserHistory;
import wwl.hy.mall.service.NewBeeMallUserService;
import wwl.hy.mall.service.UserHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import wwl.hy.mall.util.BeanUtil;
import wwl.hy.mall.util.NumberUtil;
import wwl.hy.mall.util.Result;
import wwl.hy.mall.util.ResultGenerator;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "v1", tags = "2.用户操作相关接口")
@RequestMapping("/api/v1")
public class PersonalAPI {

    @Resource
    private NewBeeMallUserService newBeeMallUserService;
    @Resource
    private UserHistoryService userHistoryService;
    private static final Logger logger = LoggerFactory.getLogger(PersonalAPI.class);

    @PostMapping("/user/login")
    @ApiOperation(value = "登录接口", notes = "返回token")
    public Result<String> login(@RequestBody @Valid MallUserLoginParam mallUserLoginParam) {
        if (!NumberUtil.isPhone(mallUserLoginParam.getLoginName())){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String loginResult = newBeeMallUserService.login(mallUserLoginParam.getLoginName(), mallUserLoginParam.getPasswordMd5());

        logger.info("login api,loginName={},loginResult={}", mallUserLoginParam.getLoginName(), loginResult);

        //登录成功
        if (!StringUtils.isEmpty(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        //登录失败
        return ResultGenerator.genFailResult(loginResult);
    }


    @PostMapping("/user/logout")
    @ApiOperation(value = "登出接口", notes = "清除token")
    public Result<String> logout(@TokenToMallUser MallUser loginMallUser) {
        Boolean logoutResult = newBeeMallUserService.logout(loginMallUser.getUserId());

        logger.info("logout api,loginMallUser={}", loginMallUser.getUserId());

        //登出成功
        if (logoutResult) {
            return ResultGenerator.genSuccessResult();
        }
        //登出失败
        return ResultGenerator.genFailResult("logout error");
    }


    @PostMapping("/user/register")
    @ApiOperation(value = "用户注册", notes = "")
    public Result register(@RequestBody @Valid MallUserRegisterParam mallUserRegisterParam) {
        if (!NumberUtil.isPhone(mallUserRegisterParam.getLoginName())){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String registerResult = newBeeMallUserService.register(mallUserRegisterParam.getLoginName(), mallUserRegisterParam.getPassword());

        logger.info("register api,loginName={},loginResult={}", mallUserRegisterParam.getLoginName(), registerResult);

        //注册成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //注册失败
        return ResultGenerator.genFailResult(registerResult);
    }

    @PutMapping("/user/info")
    @ApiOperation(value = "修改用户信息", notes = "")
    public Result updateInfo(@RequestBody @ApiParam("用户信息") MallUserUpdateParam mallUserUpdateParam, @TokenToMallUser MallUser loginMallUser) {
        Boolean flag = newBeeMallUserService.updateUserInfo(mallUserUpdateParam, loginMallUser.getUserId());
        if (flag) {
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
            //返回失败
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        }
    }

    @GetMapping("/user/info")
    @ApiOperation(value = "获取用户信息", notes = "")
    public Result<NewBeeMallUserVO> getUserDetail(@TokenToMallUser MallUser loginMallUser) {
        //已登录则直接返回
        NewBeeMallUserVO mallUserVO = new NewBeeMallUserVO();
        BeanUtil.copyProperties(loginMallUser, mallUserVO);
        return ResultGenerator.genSuccessResult(mallUserVO);
    }
    @GetMapping("/user/hisInfo")
    @ApiOperation(value = "获取用户历史搜索信息", notes = "")
    public Result<UserHisInfoVO> getUserHisInfo(@TokenToMallUser MallUser loginMallUser) {
        UserHisInfoVO userHisInfoVO = new UserHisInfoVO();
        List<UserHistory> userHistory = userHistoryService.getUserHistory(loginMallUser);
        userHisInfoVO.setUserHistories(userHistory);
        return ResultGenerator.genSuccessResult(userHisInfoVO);
    }
    @PutMapping("/user/insertHisInfo")
    @ApiOperation(value = "为用户增加用户历史搜索信息", notes = "")
    public Result insertHisInfo(@RequestBody @ApiParam("用户搜索记录信息")UserHistoryParam userHistoryParam, @TokenToMallUser MallUser loginMallUser) {
        UserHistory userHistory = new UserHistory();
        userHistory.setHistory(userHistoryParam.getHistory());
        userHistory.setUserId(loginMallUser.getUserId());
        userHistoryService.insertUserHistory(userHistory);
        return ResultGenerator.genSuccessResult();
    }

}
