<template>
  <div style="max-width:700px;margin:30px auto;padding:0 16px;">
    <h2>健康管理首页</h2>
    <el-card>
      <h3>个人健康标签：{{profile?.targetTags||"暂无"}}</h3>
      <p>身高：{{profile?.height}} cm，体重：{{profile?.weight}} kg</p>
    </el-card>

    <el-card style="margin-top:16px">
      <h3>参考食谱推荐</h3>
      <p>根据你的健康标签生成适配食谱</p>
    </el-card>

    <el-card style="margin-top:16px">
      <h3>健康提示</h3>
      <p>按时三餐打卡，少抽烟少饮酒维护萌宠健康</p>
    </el-card>

    <el-card style="margin-top:16px">
      <h3>萌宠预览</h3>
      <p>当前积分：{{petInfo?.healthScore||0}}</p>
      <el-button @click="$router.push('/pet')">前往萌宠页面</el-button>
    </el-card>

    <el-divider></el-divider>
    <el-space>
      <el-button @click="$router.push('/dietAdd')">三餐打卡</el-button>
      <el-button @click="$router.push('/smokeAlcohol')">烟酒填报</el-button>
      <el-button @click="$router.push('/report')">健康周报</el-button>
    </el-space>
  </div>
</template>

<script setup>
import {ref,onMounted} from 'vue'
import {getProfile} from '@/api/healthProfile'
import {getPetInfo} from '@/api/pet'

const userId = localStorage.getItem('userId')
const profile = ref(null)
const petInfo = ref(null)

const loadData = async ()=>{
  let res1 = await getProfile(userId)
  profile.value = res1.data
  let res2 = await getPetInfo(userId)
  petInfo.value = res2.data
}

onMounted(()=>{
  loadData()
})
</script>