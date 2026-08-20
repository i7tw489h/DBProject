import request from '@/utils/request'

//绑定亲属
export function bindFamily(data){
  return request({
    url:'/family/bind',
    method:'post',
    data:data
  })
}

//获取绑定亲属列表
export function getBindList(masterUserId){
  return request({
    url:`/family/relation/list/${masterUserId}`,
    method:'get'
  })
}

//发送家人留言
export function sendMessage(data){
  return request({
    url:'/family/msg/send',
    method:'post',
    data:data
  })
}

//获取收到的留言
export function getMessageList(receiverUserId){
  return request({
    url:`/family/msg/list/${receiverUserId}`,
    method:'get'
  })
}