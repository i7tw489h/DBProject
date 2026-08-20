import request from '@/utils/request'

//提交烟酒填报
export function addSmokeAlcohol(data){
  return request({
    url:'/smokeAlcohol/add',
    method:'post',
    data:data
  })
}

export function getSmokeAlcoholList(userId){
  return request({
    url:`/smokeAlcohol/list/${userId}`,
    method:'get'
  })
}