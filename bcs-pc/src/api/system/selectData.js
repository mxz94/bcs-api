import request from '@/utils/request'

// 查询选择内容列表
export function listSelectData(query) {
    return request({
        url: '/system/selectData/list',
        method: 'get',
        params: query
    })
}

// 查询选择内容详细
export function getSelectData(id) {
    return request({
        url: '/system/selectData/info/' + id,
        method: 'get'
    })
}

// 新增选择内容
export function addSelectData(data) {
    return request({
        url: '/system/selectData/add',
        method: 'post',
        data: data
    })
}

// 修改选择内容
export function updateSelectData(data) {
    return request({
        url: '/system/selectData/edit',
        method: 'post',
        data: data
    })
}

// 删除选择内容
export function delSelectData(id) {
    return request({
        url: '/system/selectData/delete/' + id,
        method: 'delete'
    })
}
