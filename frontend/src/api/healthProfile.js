import request from '@/utils/request'

//保存/更新健康档案
export function saveProfile(params){
  return request({
    url:'/healthProfile/save',
    method:'post',
    data:params
  })
}

//获取用户健康档案
export function getProfile(userId){
  return request({
    url:`/healthProfile/get/${userId}`,
    method:'get'
  })
}