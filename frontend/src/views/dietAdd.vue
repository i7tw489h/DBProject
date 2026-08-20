<template>
  <div style="max-width:600px;margin:60px auto;padding:20px;">
    <el-card>
      <h3>一日三餐打卡</h3>
      <el-form>
        <el-form-item label="餐次">
          <el-radio-group v-model="form.mealType">
            <el-radio :label="1">早餐</el-radio>
            <el-radio :label="2">午餐</el-radio>
            <el-radio :label="3">晚餐</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="饮食内容">
          <el-input v-model="form.foodContent" type="textarea" rows="4" placeholder="录入今天吃了什么"></el-input>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="submitRecord">提交打卡</el-button>
    </el-card>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import {addDietRecord} from '@/api/dietRecord'
import {ElMessage} from 'element-plus'

const router = useRouter()
const userId = localStorage.getItem("userId")

const form = ref({
  mealType:1,
  foodContent:""
})

const submitRecord = async ()=>{
  let params = {
    userId:userId,
    mealType:form.value.mealType,
    foodContent:form.value.foodContent
  }
  await addDietRecord(params)
  ElMessage.success("打卡提交成功")
  router.push("/")
}
</script>