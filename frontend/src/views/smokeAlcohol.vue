<template>
  <div style="max-width:600px;margin:60px auto;padding:20px;">
    <el-card>
      <h3>烟酒行为填报</h3>
      <el-form>
        <el-form-item label="今日吸烟数量(支)">
          <el-input-number v-model="form.smokeNum" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="今日饮酒(两)">
          <el-input-number v-model="form.alcoholNum" :min="0" step="0.5"></el-input-number>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="submit">提交填报</el-button>
    </el-card>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import {addSmokeAlcohol} from '@/api/smokeAlcohol'
import {ElMessage} from 'element-plus'

const router = useRouter()
const userId = localStorage.getItem("userId")

const form = ref({
  smokeNum:0,
  alcoholNum:0
})

const submit = async ()=>{
  let params = {
    userId:userId,
    smokeNum:form.value.smokeNum,
    alcoholNum:form.value.alcoholNum
  }
  await addSmokeAlcohol(params)
  ElMessage.success("填报成功")
  router.push("/")
}
</script>