// import {
//     AUTH_URL
// } from '../config/index'

/**
 * 判断打开页面的浏览器是不是微信浏览器
 * 是：使用JSAPI调用微信支付
 * 否：使用H5支付
 * @returns {boolean}
 */
export function isWeiXinBrowser() {
    let ua = navigator.userAgent.toLowerCase()
    if (ua.match(/MicroMessenger/i) == 'micromessenger') {
        return true
    } else {
        return false
    }
}

/**
 * 隐式授权（使用哪个公众号授权，要和Java后台使用的一致，否则会报获取openId异常）
 */
export function getWeiXinCode(userId, prefix) {
    let APP_ID = null
    if (prefix == "wx") {
        APP_ID = "wx0605e4222a42c44b"
    } else if (prefix == "wx1") {
        APP_ID = "wxa70669801c82f469"
    } else if (prefix == "wx2") {
        APP_ID = "wx0605e4222a42c44b"
    } else if (prefix == "wx3") {
        APP_ID = "wx0605e4222a42c44b"
    }

    let baseApi = 'https://'+prefix+'.yj.malanxi.top';
    //环境切换
    // const AUTH_URL = !type ? baseApi + `/h5/#/${routerName}?deviceId=${localStorage.getItem("deviceId")}` : baseApi + `/h5/#/${routerName}?type=${type}`
    const AUTH_URL = userId ? baseApi + `/#/login?userId=`+ userId : baseApi + `/#/login`
    const REDIRECT_URL = encodeURIComponent(AUTH_URL)
    const url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=' + APP_ID + '&redirect_uri=' + REDIRECT_URL + '&response_type=code&scope=snsapi_userinfo&state='+userId+'&connect_redirect=1#wechat_redirect'
    // console.log(localStorage.getItem("deviceId"))
    // alert(JSON.stringify(REDIRECT_URL))
    // return
    window.location.href = url

}
