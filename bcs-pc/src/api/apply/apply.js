import request from '@/utils/request'

// 查询套餐申请记录列表
export function listApply(query) {
    return request({
        url: '/apply/list',
        method: 'get',
        params: query
    })
}

// 查询套餐申请记录详细
export function getApply(id) {
    return request({
        url: '/apply/' + id,
        method: 'get'
    })
}

// 新增套餐申请记录
export function addApply(data) {
    return request({
        url: '/apply',
        method: 'post',
        data: data
    })
}

export function handleStatus(data) {
  return request({
    url: '/apply/handleStatus',
    method: 'post',
    data: data
  })
}

// 修改套餐申请记录
export function updateApply(data) {
    return request({
        url: '/apply',
        method: 'put',
        data: data
    })
}

// 删除套餐申请记录
export function delApply(id) {
    return request({
        url: '/apply/' + id,
        method: 'delete'
    })
}
