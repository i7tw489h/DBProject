<template>
  <div class="profile-container">
    <header class="header">
      <div class="header-left">
        <span class="logo">👤</span>
        <span class="title">个人中心</span>
      </div>
      <div class="header-right">
        <el-button @click="goBack">返回首页</el-button>
      </div>
    </header>

    <main class="profile-content">
      <div class="user-info-section">
        <div class="avatar-section">
          <div class="avatar">
            <span class="avatar-icon">👨‍🎓</span>
          </div>
          <div class="user-details">
            <h2>{{ user.name || '未设置姓名' }}</h2>
            <p class="account">学号：{{ user.account || '未绑定' }}</p>
            <p class="college">{{ user.college || '未设置学院' }}</p>
          </div>
        </div>
      </div>

      <div class="form-section">
        <h3>📝 个人信息</h3>
        <el-form :model="form" label-width="100px">
          <el-form-item label="姓名">
            <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
          </el-form-item>
          <el-form-item label="学院">
            <el-select v-model="form.college" placeholder="请选择学院">
              <el-option label="计算机学院" value="计算机学院"></el-option>
              <el-option label="电子学院" value="电子学院"></el-option>
              <el-option label="机械学院" value="机械学院"></el-option>
              <el-option label="土木学院" value="土木学院"></el-option>
              <el-option label="材料学院" value="材料学院"></el-option>
              <el-option label="经管学院" value="经管学院"></el-option>
              <el-option label="外语学院" value="外语学院"></el-option>
              <el-option label="文法学院" value="文法学院"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <el-button type="primary" @click="updateBasicInfo" :loading="loading.basic">保存修改</el-button>
      </div>

      <div class="form-section">
        <h3>🏋️ 健康信息</h3>
        <el-form :model="form" label-width="100px">
          <el-form-item label="性别">
            <el-radio-group v-model="form.gender">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="2">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input-number v-model="form.age" :min="10" :max="100"></el-input-number>
          </el-form-item>
          <el-form-item label="身高(cm)">
            <el-input-number v-model="form.height" :min="100" :max="250"></el-input-number>
          </el-form-item>
          <el-form-item label="体重(kg)">
            <el-input-number v-model="form.weight" :min="30" :max="200"></el-input-number>
          </el-form-item>
        </el-form>
        <el-button type="primary" @click="updateHealthInfo" :loading="loading.health">保存修改</el-button>
      </div>

      <div class="form-section">
        <h3>🎯 饮食目标</h3>
        <el-form label-width="100px">
          <el-form-item label="当前目标">
            <el-radio-group v-model="form.dietGoal">
              <el-radio :label="0">保持健康</el-radio>
              <el-radio :label="1">减脂</el-radio>
              <el-radio :label="2">增肌</el-radio>
              <el-radio :label="3">养胃</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <div class="goal-description">
          <p v-if="form.dietGoal === 0">保持健康：均衡饮食，维持当前体重</p>
          <p v-if="form.dietGoal === 1">减脂：低热量饮食，减少脂肪摄入</p>
          <p v-if="form.dietGoal === 2">增肌：高蛋白饮食，配合适量运动</p>
          <p v-if="form.dietGoal === 3">养胃：温和饮食，减少刺激性食物</p>
        </div>
        <el-button type="primary" @click="updateDietGoal" :loading="loading.goal">保存修改</el-button>
      </div>

      <div class="form-section">
        <h3>🚫 个人忌口</h3>
        <div class="restrictions-list">
          <div v-for="item in restrictions" :key="item.restrictionId" class="restriction-item">
            <el-tag :type="getRestrictionTypeTag(item.restrictionType)" effect="dark">
              {{ getRestrictionTypeLabel(item.restrictionType) }}
            </el-tag>
            <span class="restriction-desc">{{ item.restrictionDesc }}</span>
            <el-button type="danger" size="small" circle @click="deleteRestriction(item.restrictionId)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </div>
          <el-empty v-if="restrictions.length === 0" description="暂无忌口设置" />
        </div>
        <div class="add-restriction">
          <el-select v-model="newRestriction.type" placeholder="选择忌口类型" style="width: 150px">
            <el-option label="不吃辣" :value="0" />
            <el-option label="不吃香菜" :value="1" />
            <el-option label="清真" :value="2" />
            <el-option label="过敏" :value="3" />
            <el-option label="素食" :value="4" />
          </el-select>
          <el-input v-model="newRestriction.desc" placeholder="请输入具体忌口内容（如：花生、牛肉等）" style="width: 250px; margin: 0 10px" />
          <el-button type="primary" @click="addRestriction" :loading="loading.restriction">添加</el-button>
        </div>
      </div>

      <div class="form-section">
        <h3>🔐 账户安全</h3>
        <div class="password-form">
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码"></el-input>
            </el-form-item>
          </el-form>
          <el-button type="warning" @click="changePassword" :loading="loading.password">修改密码</el-button>
        </div>
      </div>

      <div class="actions-section">
        <el-button type="danger" size="large" @click="logout">退出登录</el-button>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores'
import { userApi, restrictionApi } from '@/api'
import { Delete } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const form = ref({
  name: '',
  phone: '',
  college: '',
  gender: 1,
  age: null,
  height: null,
  weight: null,
  dietGoal: 0
})

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordFormRef = ref(null)

const loading = reactive({
  basic: false,
  health: false,
  goal: false,
  password: false,
  restriction: false
})

const restrictions = ref([])
const newRestriction = ref({
  type: null,
  desc: ''
})

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const user = ref({
  name: '',
  account: '',
  college: '',
  phone: '',
  gender: null,
  age: null,
  height: null,
  weight: null,
  dietGoal: null
})

const loadUserInfo = async () => {
  try {
    const userId = userStore.user?.userId
    if (!userId) return

    const userInfo = await userApi.getUserInfo(userId)
    user.value = { ...userInfo }

    form.value = {
      name: userInfo.name || '',
      phone: userInfo.phone || '',
      college: userInfo.college || '',
      gender: userInfo.gender || 1,
      age: userInfo.age || null,
      height: userInfo.height || null,
      weight: userInfo.weight || null,
      dietGoal: userInfo.dietGoal || 0
    }
  } catch (error) {
    console.error('加载用户信息失败:', error)
  }
}

const updateBasicInfo = async () => {
  loading.basic = true
  try {
    await userApi.updateUser({
      userId: userStore.user.userId,
      name: form.value.name,
      phone: form.value.phone,
      college: form.value.college
    })
    user.value.name = form.value.name
    user.value.phone = form.value.phone
    user.value.college = form.value.college
    ElMessage.success('基本信息更新成功')
  } catch (error) {
    ElMessage.error('更新失败')
  } finally {
    loading.basic = false
  }
}

const updateHealthInfo = async () => {
  loading.health = true
  try {
    await userApi.updateUser({
      userId: userStore.user.userId,
      gender: form.value.gender,
      age: form.value.age,
      height: form.value.height,
      weight: form.value.weight
    })
    user.value.gender = form.value.gender
    user.value.age = form.value.age
    user.value.height = form.value.height
    user.value.weight = form.value.weight
    ElMessage.success('健康信息更新成功')
  } catch (error) {
    ElMessage.error('更新失败')
  } finally {
    loading.health = false
  }
}

const updateDietGoal = async () => {
  loading.goal = true
  try {
    await userApi.updateUser({
      userId: userStore.user.userId,
      dietGoal: form.value.dietGoal
    })
    user.value.dietGoal = form.value.dietGoal
    ElMessage.success('饮食目标更新成功')
  } catch (error) {
    ElMessage.error('更新失败')
  } finally {
    loading.goal = false
  }
}

const changePassword = async () => {
  try {
    await passwordFormRef.value.validate()
  } catch (error) {
    return
  }

  loading.password = true
  try {
    await userApi.updatePassword({
      userId: userStore.user.userId,
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword
    })
    ElMessage.success('密码修改成功，请重新登录')
    userStore.logout()
    router.push('/login')
  } catch (error) {
    ElMessage.error('密码修改失败')
  } finally {
    loading.password = false
  }
}

const logout = () => {
  userStore.logout()
  router.push('/login')
}

const goBack = () => {
  router.push('/')
}

const getRestrictionTypeLabel = (type) => {
  // 与数据库定义保持一致: 0-不吃辣, 1-不吃香菜, 2-清真, 3-过敏, 4-素食
  const labels = { 0: '不吃辣', 1: '不吃香菜', 2: '清真', 3: '过敏', 4: '素食' }
  return labels[type] || '其他'
}

const getRestrictionTypeTag = (type) => {
  // 标签颜色: danger-红色, warning-橙色, success-绿色, info-蓝色, ''-默认
  const tags = { 0: 'danger', 1: 'success', 2: 'warning', 3: 'danger', 4: 'success' }
  return tags[type] || ''
}

const loadRestrictions = async () => {
  try {
    const userId = userStore.user?.userId
    if (!userId) return
    const res = await restrictionApi.getUserRestrictions(userId)
    restrictions.value = res || []
  } catch (error) {
    console.error('加载忌口信息失败:', error)
  }
}

const addRestriction = async () => {
  if (!newRestriction.value.type || !newRestriction.value.desc.trim()) {
    ElMessage.warning('请选择忌口类型并输入具体内容')
    return
  }
  loading.restriction = true
  try {
    await restrictionApi.saveUserRestriction({
      userId: userStore.user.userId,
      restrictionType: newRestriction.value.type,
      restrictionDesc: newRestriction.value.desc.trim()
    })
    ElMessage.success('添加成功')
    newRestriction.value = { type: null, desc: '' }
    loadRestrictions()
  } catch (error) {
    ElMessage.error('添加失败')
  } finally {
    loading.restriction = false
  }
}

const deleteRestriction = async (restrictionId) => {
  try {
    await restrictionApi.deleteUserRestriction(restrictionId)
    ElMessage.success('删除成功')
    loadRestrictions()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

onMounted(() => {
  loadUserInfo()
  loadRestrictions()
})
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  background: white;
  padding: 15px 30px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo {
  font-size: 30px;
}

.title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.profile-content {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.user-info-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 30px;
  color: white;
  margin-bottom: 20px;
}

.avatar-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.avatar {
  width: 80px;
  height: 80px;
  background: rgba(255,255,255,0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-icon {
  font-size: 40px;
}

.user-details h2 {
  margin: 0 0 5px 0;
  font-size: 24px;
}

.account, .college {
  margin: 5px 0;
  opacity: 0.9;
  font-size: 14px;
}

.form-section {
  background: white;
  border-radius: 12px;
  padding: 25px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.form-section h3 {
  margin: 0 0 20px 0;
  font-size: 18px;
  color: #333;
  padding-bottom: 10px;
  border-bottom: 2px solid #f0f0f0;
}

.goal-description {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 15px;
}

.goal-description p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.password-form {
  padding: 10px 0;
}

.restrictions-list {
  margin-bottom: 20px;
}

.restriction-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 12px 15px;
  background: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 10px;
}

.restriction-desc {
  flex: 1;
  color: #333;
  font-size: 14px;
}

.add-restriction {
  display: flex;
  align-items: center;
  padding-top: 15px;
  border-top: 1px dashed #e0e0e0;
}

.actions-section {
  text-align: center;
  padding: 20px 0;
}

@media (max-width: 768px) {
  .profile-content {
    padding: 15px;
  }

  .avatar-section {
    flex-direction: column;
    text-align: center;
  }
}
</style>
