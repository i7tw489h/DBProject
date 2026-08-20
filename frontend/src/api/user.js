import request from '@/utils/request'

//用户登录注册
export function loginApi(data){
  return request({
    url:'/user/login',
    method:'post',
    data:data
  })
}

export function registerApi(data){
  return request({
    url:'/user/register',
    method:'post',
    data:data
  })
}

export function getUserInfoApi(userId){
  return request({
    url:`/user/info/${userId}`,
    method:'get'
  })
}

export function updatePwdApi(data){
  return request({
    url:'/user/updatePwd',
    method:'post',
    data:data
  })
}