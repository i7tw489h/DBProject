import request from '@/utils/request'

//获取萌宠积分状态
export function getPetInfo(userId){
  return request({
    url:`/pet/get/${userId}`,
    method:'get'
  })
}