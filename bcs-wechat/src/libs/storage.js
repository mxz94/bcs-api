// 统一处理缓存数据
const STORAGE = window.localStorage
// const STORAGE = window.sessionStorage

/**
 * 获取数据
 */
export const getData = (key)=>{
    return STORAGE.getItem(key)
}

/**
 * 设置数据
 */
export const setData = (key,value) =>{
    STORAGE.setItem(key,value)
} 


/**
 * 删除数据
 */
export const delData = (key)=>{
    STORAGE.removeItem(key)
}

/**
 * 清除所有数据
 */
export const clear = ()=>{
    STORAGE.clear()
}