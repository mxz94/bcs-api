import request from '@/utils/request'

// 查询提现记录列表
export function listWithdrawRecord(query) {
    return request({
        url: '/withdrawRecord/list',
        method: 'get',
        params: query
    })
}

// 查询提现记录详细
export function getWithdrawRecord(id) {
    return request({
        url: '/withdrawRecord/' + id,
        method: 'get'
    })
}

// 新增提现记录
export function addWithdrawRecord(data) {
    return request({
        url: '/withdrawRecord',
        method: 'post',
        data: data
    })
}

// 修改提现记录
export function updateWithdrawRecord(data) {
    return request({
        url: '/withdrawRecord',
        method: 'put',
        data: data
    })
}

// 删除提现记录
export function delWithdrawRecord(id) {
    return request({
        url: '/withdrawRecord/' + id,
        method: 'delete'
    })
}
