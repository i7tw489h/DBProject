<template>
  <div style="max-width:700px;margin:40px auto;padding:16px;">
    <el-card>
      <h3>家人留言提醒</h3>
      <el-form>
        <el-form-item label="接收人ID">
          <el-input v-model="sendForm.receiverUserId"></el-input>
        </el-form-item>
        <el-form-item label="留言内容">
          <el-input v-model="sendForm.content" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="send">发送留言</el-button>

      <el-divider></el-divider>
      <h4>收到的留言</h4>
      <el-timeline>
        <el-timeline-item v-for="item in msgList" :key="item.id">
          {{item.content}}
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup>
import {ref,onMounted} from 'vue'
import {sendMessage,getMessageList} from '@/api/family'
import {ElMessage} from 'element-plus'

const userId = localStorage.getItem('userId')
const sendForm = ref({
  receiverUserId:"",
  content:""
})
const msgList = ref([])

const loadMsg = async ()=>{
  let res = await getMessageList(userId)
  msgList.value = res.data
}

const send = async ()=>{
  let params = {
    senderUserId:userId,
    receiverUserId:sendForm.value.receiverUserId,
    content:sendForm.value.content
  }
  await sendMessage(params)
  ElMessage.success("留言发送成功")
  loadMsg()
}

onMounted(()=>{
  loadMsg()
})
</script>