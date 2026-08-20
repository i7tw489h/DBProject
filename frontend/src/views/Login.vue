<template>
  <div class="login-page">
    <div class="login-box">
      <h1>家庭生活健康辅助管理系统</h1>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm">
            <el-form-item label="账号">
              <el-input v-model="loginForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="loginForm.password" type="password"></el-input>
            </el-form-item>
            <el-button type="primary" class="login-btn" @click="handleLogin">登录</el-button>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <el-form :model="regForm">
            <el-form-item label="账号">
              <el-input v-model="regForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="regForm.password" type="password"></el-input>
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="regForm.name"></el-input>
            </el-form-item>
            <el-form-item label="学院">
              <el-input v-model="regForm.college"></el-input>
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="regForm.phone"></el-input>
            </el-form-item>
            <el-button type="success" class="login-btn" @click="handleRegister">注册</el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <div class="tip-text">健康打卡 · 饮食记录 · 萌宠激励</div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { userApi } from '@/api'
import { ElMessage } from 'element-plus'
import { loginApi } from '@/api/user'
import { getProfile } from '@/api/healthProfile'

const router = useRouter()
const activeTab = ref('login')

const loginForm = ref({ username:'',password:'' })
const regForm = ref({
  username:'',
  password:'',
  name:'',
  college:'',
  phone:''
})

// 登录
const handleLogin = async () => {
  try {
    const res = await loginApi(loginForm.value)
    console.log("后端返回res：", res)
    // 判断业务code等于200才算成功
    if(res.code === 200){
      const user = res.data
      localStorage.setItem("userId", user.id)
      localStorage.setItem("username", user.username)
      ElMessage.success("登录成功")
      console.log("登录成功，userId：", user.id)
      router.push("/home")
    }else{
      ElMessage.error(res.msg || "登录失败")
    }
  } catch (err) {
    ElMessage.error("请求异常，请检查后端服务")
    console.error("登录失败", err)
  }
}

const handleRegister = async ()=>{
  try{
    await userApi.register(regForm.value)
    ElMessage.success('注册成功，请登录')
    activeTab.value = 'login'
  }catch(err){
    ElMessage.error('注册失败')
  }
}
</script>

<style scoped>
.login-page{
  width:100vw;
  height:100vh;
  background:#6366f1;
  display:flex;
  justify-content:center;
  align-items:center;
}
.login-box{
  width:480px;
  background:#fff;
  padding:36px;
  border-radius:16px;
}
h1{
  text-align:center;
  margin-bottom:20px;
}
.login-btn{
  width:100%;
}
.tip-text{
  text-align:center;
  margin-top:20px;
  color:#666;
  font-size:15px;
}
</style>