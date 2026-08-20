<template>
  <div style="max-width:600px;margin:60px auto;padding:20px;">
    <el-card>
      <h3>亲属绑定授权</h3>
      <el-form>
        <el-form-item label="对方用户ID">
          <el-input v-model="form.bindUserId" placeholder="输入对方userId"></el-input>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="bind">发起绑定</el-button>

      <el-divider></el-divider>
      <h4>已绑定亲属列表</h4>
      <el-table :data="bindList">
        <el-table-column prop="bindUserId" label="绑定用户ID"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import {ref,onMounted} from 'vue'
import {bindFamily,getBindList} from '@/api/family'
import {ElMessage} from 'element-plus'

const userId = localStorage.getItem('userId')
const form = ref({
  bindUserId:""
})
const bindList = ref([])

const loadList = async ()=>{
  let res = await getBindList(userId)
  bindList.value = res.data
}

const bind = async ()=>{
  let params = {
    masterUserId:userId,
    bindUserId:form.value.bindUserId
  }
  await bindFamily(params)
  ElMessage.success("绑定成功")
  loadList()
}

onMounted(()=>{
  loadList()
})
</script>