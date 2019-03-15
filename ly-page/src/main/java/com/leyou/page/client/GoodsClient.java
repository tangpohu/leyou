package com.leyou.page.client;

import com.leyou.item.api.CoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GoodsClient extends CoodsApi {


}
