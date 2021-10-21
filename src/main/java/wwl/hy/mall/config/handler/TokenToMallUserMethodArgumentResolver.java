/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package wwl.hy.mall.config.handler;

import wwl.hy.mall.common.Constants;
import wwl.hy.mall.common.NewBeeMallException;
import wwl.hy.mall.common.ServiceResultEnum;
import wwl.hy.mall.config.annotation.TokenToMallUser;
import wwl.hy.mall.dao.MallUserMapper;
import wwl.hy.mall.dao.NewBeeMallUserTokenMapper;
import wwl.hy.mall.entity.MallUser;
import wwl.hy.mall.entity.MallUserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
//通过该类可以将controller里的@TokenToMallUser MallUser loginMallUser参数通过dao包里的对应的mapper方法将数据库里的token对应的用户查询出来。
//详细原理见：https://blog.csdn.net/songzehao/article/details/99641594
@Component
public class TokenToMallUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private MallUserMapper mallUserMapper;
    @Autowired
    private NewBeeMallUserTokenMapper newBeeMallUserTokenMapper;

    public TokenToMallUserMethodArgumentResolver() {
    }

    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(TokenToMallUser.class)) {
            return true;
        }
        return false;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getParameterAnnotation(TokenToMallUser.class) instanceof TokenToMallUser) {
            MallUser mallUser = null;
            String token = webRequest.getHeader("token");
            if (null != token && !"".equals(token) && token.length() == Constants.TOKEN_LENGTH) {
                MallUserToken mallUserToken = newBeeMallUserTokenMapper.selectByToken(token);
                if (mallUserToken == null || mallUserToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
                    NewBeeMallException.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
                }
                mallUser = mallUserMapper.selectByPrimaryKey(mallUserToken.getUserId());
                if (mallUser == null) {
                    NewBeeMallException.fail(ServiceResultEnum.USER_NULL_ERROR.getResult());
                }
                if (mallUser.getLockedFlag().intValue() == 1) {
                    NewBeeMallException.fail(ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult());
                }
                return mallUser;
            } else {
                NewBeeMallException.fail(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            }
        }
        return null;
    }

    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

}
