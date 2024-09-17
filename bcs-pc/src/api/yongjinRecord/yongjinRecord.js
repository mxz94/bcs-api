import request from '@/utils/request'

// 查询佣金分成记录列表
export function listYongjinRecord(query) {
    return request({
        url: '/yongjinRecord/list',
        method: 'get',
        params: query
    })
}

// 查询佣金分成记录详细
export function getYongjinRecord(id) {
    return request({
        url: '/yongjinRecord/' + id,
        method: 'get'
    })
}

// 新增佣金分成记录
export function addYongjinRecord(data) {
    return request({
        url: '/yongjinRecord',
        method: 'post',
        data: data
    })
}

// 修改佣金分成记录
export function updateYongjinRecord(data) {
    return request({
        url: '/yongjinRecord',
        method: 'put',
        data: data
    })
}

// 删除佣金分成记录
export function delYongjinRecord(id) {
    return request({
        url: '/yongjinRecord/' + id,
        method: 'delete'
    })
}
