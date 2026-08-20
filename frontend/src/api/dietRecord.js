import request from '@/utils/request'

//提交三餐打卡
export function addDietRecord(data){
  return request({
    url:'/dietRecord/add',
    method:'post',
    data:data
  })
}

//查询用户全部打卡
export function getDietList(userId){
  return request({
    url:`/dietRecord/list/${userId}`,
    method:'get'
  })
}

//删除打卡记录
export function delDietRecord(id){
  return request({
    url:`/dietRecord/delete/${id}`,
    method:'delete'
  })
}