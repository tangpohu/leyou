package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  ExceptionEnum {
    PRICE_CANNOT_BE_NULL( 400 , "价格不能空！"),
    CATEGORY_NOT_FOND(404,"商品分类没查到"),
    BRAND_NOT_FOUND(404,"品牌不存在"),
    GOODS_NOT_FOUND(404,"商品不存在"),
    GOODS_SKU_NOT_FOUND(404,"商品SKU不存在"),
    GOODS_STOCK_NOT_FOUND(404,"商品库存不存在"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    CATEGORY_BRAND_SAVE_ERROR(500,"新增品牌分类中间表失败"),
    UPLOAD_ERROR(500,"文件上传失败"),
    INVALID_FILE_TYPE(500,"上传失败，文件类型不匹配"),
    SPEC_GROUP_NOT_FOND(404,"商品规格组没查到"),
    SPEC_PARAM_NOT_FOND(404,"商品规格参数没查到"),
    GOODS_SAVE_ERROR(500,"新增商品失败"),
    GOODS_UPDATE_ERROR(500,"修改商品失败"),
    GOODS_ID_CANNOT_BE_NULL(400,"商品id不能为空"),
    USER_DATA_TYPE_ERROR(400,"用户的数据类型不正确"),
    PASSWORD_IS_ERROR(500,"密码错误"),
    USER_DONT_EXSIST(500,"用户错误"),

    ;
    private int code;
    private String msg;

}
