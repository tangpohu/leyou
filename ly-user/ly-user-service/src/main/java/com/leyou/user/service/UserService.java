package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.Md5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public Boolean checkData(String data, Integer type) {
        User user = new User();
        switch (type){
            case 1:user.setUsername(data);
                break;
            case 2:user.setPhone(data);
                break;
            default:
                    throw  new  LyException(ExceptionEnum.TYPE_ERROR);
        }
        return  userMapper.selectCount(user)==0;

    }

    public Boolean register(User user, String code) {
        //注册的验证码
        if (!code.equals("8888")) {
            // 不正确，返回
            return false;
        }
        user.setId(null);
        user.setCreated(new Date());
        // 生成盐  05b0f203987e49d2b72b20b95e0e57d9
        //这个是原生的md5
        //String s = DigestUtils.md5Hex(user.getPassword());
        String salt = Md5Utils.generate();
        user.setSalt(salt);
        // 对密码进行加密
        String s = Md5Utils.encryptPassword(user.getPassword(), user.getSalt());
        user.setPassword(s);
        // 写入数据库
        boolean boo = this.userMapper.insertSelective(user) == 1;

        // 如果注册成功，删除redis中的code

        return boo;
    }

    public User queryUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        User user1 = userMapper.selectOne(user);
        //说明用户存在
        if (user1!=null)
        {
            //再来判断密码
            String s = Md5Utils.encryptPassword(password, user1.getSalt());

            if (StringUtils.equals(s, user1.getPassword())){
                //此时则验证为登陆成功
                return user1;
            }
            else {
                throw new LyException(ExceptionEnum.PASSWORD_IS_ERROR);
            }
        }else {
            throw new LyException(ExceptionEnum.USER_DONT_EXSIST);
        }


    }
}
