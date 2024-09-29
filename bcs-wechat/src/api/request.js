import axios from '@/libs/api.request'
import qs from 'qs'

/**
 * 公共请求方法
 * @param {String} url 请求路径
 * @param {String} method 请求方式 "get/post"
 * @param {Object} data 请求参数
 * @param {Function} callback 回调函数
 */
export default function request(url, method, contentType, data, callback) {
    if (method === 'get' || method === 'GET') {
        axios.request({
            url: url,
            method: method,
            params: data,
            responseEncoding: 'utf-8'
        }).then(function (res) {
            callback(res.data)
        })
    } else if (method === 'post' || method === 'POST') {
        let con = 'application/x-www-form-urlencoded;charset=UTF-8'
        if (contentType !== null) {
            if (contentType === 'file') {
                con = 'multipart/form-data'
            } else {
                con = 'application/json;charset=utf-8'
            }
        } else {
            data = qs.stringify(data)
        }
        axios.request({
            url: url,
            method: method,
            data: data,
            responseEncoding: 'utf-8',
            headers: {
                'Content-Type': con
            }
        }).then(function (res) {
            callback(res.data)
        })
    }
}