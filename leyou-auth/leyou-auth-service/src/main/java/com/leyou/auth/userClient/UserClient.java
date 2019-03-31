package com.leyou.auth.userClient;

import com.leyou.user.UserApi.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface UserClient extends UserApi {
}
