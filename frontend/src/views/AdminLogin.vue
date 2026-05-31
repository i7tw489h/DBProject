<template>
  <div class="admin-login-container">
    <div class="admin-login-card">
      <div class="logo">
        <span class="logo-icon">🏪</span>
        <h1>食堂管理系统</h1>
        <p>商家登录入口</p>
      </div>

      <el-form :model="form" ref="formRef" label-width="80px" class="login-form">
        <el-form-item label="账号" prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入食堂账号"
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

        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin">登录</el-button>
        </el-form-item>

        <div class="back-link">
          <router-link to="/login">返回学生登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()

const formRef = ref(null)
const form = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请填写账号和密码')
    return
  }

  // 简单的食堂管理员验证（这里可以对接后端验证接口）
  // 默认食堂管理员账号：admin / admin
  if (form.username === 'admin' && form.password === 'admin') {
    localStorage.setItem('canteenAdmin', JSON.stringify({
      username: 'admin',
      name: '食堂管理员',
      role: 'canteen'
    }))
    ElMessage.success('登录成功')
    router.push('/admin')
  } else {
    ElMessage.error('账号或密码错误')
  }
}
</script>

<style scoped>
.admin-login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.admin-login-card {
  background: white;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  width: 100%;
  max-width: 400px;
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

.login-form {
  margin-top: 20px;
}

.login-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
}

.back-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.back-link a {
  color: #667eea;
  text-decoration: none;
}

.back-link a:hover {
  text-decoration: underline;
}
</style>