package com.atguigu.orderservice.constant;

public class OrderConstant {

    /** 未支付 */
    public static final Integer PAY_STATUS_NO= 0;
    /** 已支付 */
    public static final Integer PAY_STATUS_YES= 1;

    /** 支付类型-微信支付 */
    public static final Integer PAY_TYPE_WX= 1;
    /** 支付类型-支付宝支付 */
    public static final Integer PAY_TYPE_ZFB= 2;

    /*********************微信支付相关参数 start**************************/

    /** 关联公众号appid */
    public static final String APP_ID= "wx74862e0dfcf69954";
    /** 商户号 */
    public static final String MCH_ID= "1558950191";
    /** 商户key */
    public static final String PARTNER_KEY= "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb";
    /** 支付成功标识 */
    public static final String PAY_SUCCESS= "SUCCESS";


    /*********************微信支付相关参数 end**************************/
}
