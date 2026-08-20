<template>
  <div style="max-width:600px;margin:60px auto;padding:20px;">
    <el-card>
      <h2>我的健康萌宠</h2>
      <div v-if="petInfo.petStatus === 'pet_health'" style="font-size:32px;"> 🐱【健康萌宠】</div>
      <div v-if="petInfo.petStatus === 'normal'" style="font-size:32px;"> 🐱【普通状态】</div>
      <div v-if="petInfo.petStatus === 'pet_sick'" style="font-size:32px;"> 🐱【萎靡肥胖】</div>

      <el-divider></el-divider>
      <p>当前健康积分：{{petInfo.healthScore}}</p>
      <p>规则：健康打卡加分，吸烟饮酒扣减积分</p>
      <ul>
        <li>score &gt;=120 → 健康萌宠</li>
        <li>60 &lt;= score &lt;120 → 普通状态</li>
        <li>score &lt;60 → 萎靡肥胖</li>
      </ul>
    </el-card>
  </div>
</template>

<script setup>
import {ref,onMounted} from 'vue'
import {getPetInfo} from '@/api/pet'

const userId = localStorage.getItem("userId")
const petInfo = ref({})

const loadPet = async ()=>{
  let res = await getPetInfo(userId)
  petInfo.value = res.data
}

onMounted(()=>{
  loadPet()
})
</script>