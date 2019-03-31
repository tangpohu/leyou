package com.leyou.auth.controller;

import com.auth.entity.UserInfo;
import com.auth.utils.JwtUtils;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Resource
    private AuthService authService;

    @Resource
    private JwtProperties prop;

    /**
     * 登录授权
     *     也就是首先判断登陆，如果登陆成功了就生成token
     *     然后将token写入cookies  并且设置cookie的过期时间，
     *     并且设置 httponly使得前台js无法对其操作，为了安全
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<Void> authentication(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) {
        // 登录校验
        String token = this.authService.authentication(username, password);
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
        CookieUtils.setCookie(request, response, prop.getCookieName(), token, prop.getCookieMaxAge(), true);
        return ResponseEntity.ok().build();
    }

    /**
     * 验证用户信息
     * @param token
     * @return
     */
    @GetMapping("verify")                      //此处获取请求中携带的cookie信息，也就是tmdtoken
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("LY_TOKEN")String token,
                                               HttpServletRequest request,HttpServletResponse response){
        if (StringUtils.isBlank(token))
        {
            throw  new LyException(ExceptionEnum.USER_DONT_EXSIST);
        }
        try {
            // 从token中解析token信息 通过公钥进行解析  判断登陆的信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.prop.getPublicKey());
            // 解析成功返回用户信息
            String newtoken = JwtUtils.generateToken(userInfo, prop.getPrivateKey(), prop.getExpire());
            //当我们认证通过之后就说明我们还是处于登陆状态所以此处要刷新token 这样就可以保证我们如果一值操作我们的页面则永远不会过期
            CookieUtils.setCookie(request,response ,prop.getCookieName() ,newtoken , prop.getCookieMaxAge(), true);
            //返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            //TOKEN过期或者被串改
            e.printStackTrace();
        }
        // 出现异常则，响应500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
