/**
 * 对axios重新进行封装
 */
import axios from 'axios'
// import store from '@/store'
import * as util from '@/libs/util'
// import { Spin } from 'iview'
const addErrorLog = errorInfo => {
    const {
        statusText,
        status,
        request: {
            responseURL
        }
    } = errorInfo
    let info = {
        type: 'ajax',
        code: status,
        mes: statusText,
        url: responseURL
    }

    console.log(info)
    Toast(info)
    // if (!responseURL.includes('save_error_logger')) store.dispatch('addErrorLog', info)
}

/**
 * 封装axios
 */
class HttpRequest {
    constructor(baseUrl) {
        this.baseUrl = baseUrl
        this.queue = {}
    }
    // 内部配置
    getInsideConfig() {
        const config = {
            baseURL: this.baseUrl,
            headers: {
                //
            }
        }
        return config
    }
    destroy(url) {
        delete this.queue[url]
        if (!Object.keys(this.queue).length) {
            // Spin.hide()
        }
    }
    // 拦截器
    interceptors(instance, url) {
        // 请求拦截
        instance.interceptors.request.use(config => {
            // // 添加全局的loading...
            // if (!Object.keys(this.queue).length) {
            //   // Spin.show() // 不建议开启，因为界面不友好
            // }
            // this.queue[url] = true
            // ***********************自定义：添加token************************
            // const userToken = window.localStorage.getItem("token")
            // 从Cookies中读取token,并添加到请求头中
            const userToken = util.getToken()
            if (userToken) {
                config.headers.Authorization = 'Bearer ' + userToken
                // config.headers['X-Access-Token'] = userToken;
            }
            return config
        }, error => {
            return Promise.reject(error)
        })
        // 响应拦截
        instance.interceptors.response.use(res => {
            this.destroy(url)
            const {
                data,
                status
            } = res
            return {
                data,
                status
            }
        }, error => {
            this.destroy(url)
            let errorInfo = error.response
            console.log(errorInfo+"========")
            if (!errorInfo) {
                const {
                    request: {
                        statusText,
                        status
                    },
                    config
                } = JSON.parse(JSON.stringify(error))
                errorInfo = {
                    statusText,
                    status,
                    request: {
                        responseURL: config.url
                    }
                }
            }
            addErrorLog(errorInfo)
            return Promise.reject(error)
        })
    }
    request(options) {
        const instance = axios.create()
        options = Object.assign(this.getInsideConfig(), options)
        this.interceptors(instance, options.url)
        return instance(options)
    }
}
export default HttpRequest