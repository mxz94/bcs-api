import request from '@/utils/request'

// 查询话费分成记录列表
export function listCallFeeRecord(query) {
    return request({
        url: '/callFeeRecord/list',
        method: 'get',
        params: query
    })
}

// 查询话费分成记录详细
export function getCallFeeRecord(id) {
    return request({
        url: '/callFeeRecord/' + id,
        method: 'get'
    })
}

// 新增话费分成记录
export function addCallFeeRecord(data) {
    return request({
        url: '/callFeeRecord',
        method: 'post',
        data: data
    })
}

// 修改话费分成记录
export function updateCallFeeRecord(data) {
    return request({
        url: '/callFeeRecord',
        method: 'put',
        data: data
    })
}

// 删除话费分成记录
export function delCallFeeRecord(id) {
    return request({
        url: '/callFeeRecord/' + id,
        method: 'delete'
    })
}
