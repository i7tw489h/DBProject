<template>
  <div class="init-box" style="max-width:600px;margin:80px auto;padding:20px;">
    <el-card>
      <h2>请选择你的健康管理目标</h2>
      <el-checkbox-group v-model="form.targetTags">
        <el-checkbox label="减脂"></el-checkbox>
        <el-checkbox label="胃部养胃调理"></el-checkbox>
        <el-checkbox label="术后饮食忌口"></el-checkbox>
        <el-checkbox label="高血压"></el-checkbox>
        <el-checkbox label="糖尿病"></el-checkbox>
      </el-checkbox-group>

      <el-divider></el-divider>
      <el-form>
        <el-form-item label="身高(cm)">
          <el-input v-model.number="form.height" placeholder="身高cm"></el-input>
        </el-form-item>
        <el-form-item label="体重(kg)">
          <el-input v-model.number="form.weight" placeholder="体重kg"></el-input>
        </el-form-item>
      </el-form>

      <el-button type="primary" @click="submit">保存进入系统</el-button>
    </el-card>
  </div>
</template>

<script setup>
import {ref} from "vue"
import {saveProfile} from "@/api/healthProfile"
import {useRouter} from "vue-router"
import {ElMessage} from "element-plus"
const router = useRouter()

const form = ref({
  targetTags:[],
  height:null,
  weight:null
})

const submit = async ()=>{
  let params = {
    userId:localStorage.getItem("userId"),
    targetTags:form.value.targetTags.join(","),
    height:form.value.height,
    weight:form.value.weight
  }
  await saveProfile(params)
  ElMessage.success("档案保存成功")
  router.push("/")
}
</script>