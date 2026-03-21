package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.userMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.userService;
import com.sky.utils.HttpClientUtil;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class userServiceImpl implements userService {
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private userMapper userMapper;

    //微信服务地址
  private static final  String WX_login =  "https://api.weixin.qq.com/sns/jscode2session";
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {

        //调用微信接口服务，获取用户的openid
        Map<String, String> map  = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code",userLoginDTO.getCode());
        map.put("grant_type","authorization_code");
        //传递的是json包，需要解析
     String json=   HttpClientUtil.doGet(WX_login,map);

        JSONObject jsonObject = JSONObject.parseObject(json);
        String openid = jsonObject.getString("openid");
        //判断是否为空，空报错
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }


        //判断用户是否是新用户
        User user = userMapper.getByopenid(openid);

        //如果是新用户，完成注册
        if(user==null){

           user=  User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
           userMapper.insert(user);
        }

        //返回对象
        return  user;

    }
}
