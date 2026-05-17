<template>
  <div class="register-container">
    <div class="register-card">
      <div class="logo">
        <span class="logo-icon">🍽️</span>
        <h1>校园AI食堂</h1>
        <p>智能点餐与营养推荐系统</p>
      </div>
      
      <el-form :model="form" ref="formRef" label-width="80px" class="register-form">
        <el-form-item label="账号" prop="username">
          <el-input 
            v-model="form.username" 
            placeholder="请输入账号"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="请输入密码"
            prefix-icon="Lock"
          />
        </el-form-item>
        
        <el-form-item label="姓名" prop="name">
          <el-input 
            v-model="form.name" 
            placeholder="请输入姓名"
            prefix-icon="UserFilled"
          />
        </el-form-item>
        
        <el-form-item label="学院" prop="college">
          <el-input 
            v-model="form.college" 
            placeholder="请输入学院"
            prefix-icon="Building"
          />
        </el-form-item>
        
        <el-form-item label="手机号" prop="phone">
          <el-input 
            v-model="form.phone" 
            placeholder="请输入手机号"
            prefix-icon="Phone"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" class="register-btn" @click="handleRegister">注册</el-button>
        </el-form-item>
        
        <div class="login-link">
          <span>已有账号？</span>
          <a href="/login">立即登录</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { userApi } from '@/api'
import { useRouter } from 'vue-router'

const router = useRouter()

const formRef = ref(null)
const form = reactive({
  username: '',
  password: '',
  name: '',
  college: '',
  phone: ''
})

const handleRegister = async () => {
  if (!form.username || !form.password || !form.name) {
    ElMessage.warning('请填写必填项（账号、密码、姓名）')
    return
  }
  
  try {
    await userApi.register(form)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (error) {
    ElMessage.error(error.message || '注册失败')
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.register-card {
  background: white;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  width: 100%;
  max-width: 450px;
}

.logo {
  text-align: center;
  margin-bottom: 30px;
}

.logo-icon {
  font-size: 60px;
  display: block;
  margin-bottom: 10px;
}

.logo h1 {
  color: #333;
  margin-bottom: 5px;
}

.logo p {
  color: #999;
  font-size: 14px;
}

.register-form {
  margin-top: 20px;
}

.register-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
}

.login-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.login-link a {
  color: #667eea;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>