package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    PRICE_CANNOT_BE_NULL( 400 , "价格不能空！"),
    CATEGORY_NOT_FOND(404,"商品分类没查到"),
    BRAND_NOT_FOUND(404,"品牌不存在"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    CATEGORY_BRAND_SAVE_ERROR(500,"新增品牌分类中间表失败"),

    ;
    private int code;
    private String msg;

}
