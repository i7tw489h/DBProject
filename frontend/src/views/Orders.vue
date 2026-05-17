<template>
  <div class="orders-container">
    <header class="header">
      <div class="header-left">
        <span class="logo">📋</span>
        <span class="title">我的订单</span>
      </div>
      <div class="header-right">
        <el-button @click="goBack">返回首页</el-button>
      </div>
    </header>

    <main class="orders-content">
      <div class="tabs">
        <el-tabs v-model="activeTab" @tab-change="loadOrders">
          <el-tab-pane label="全部订单" name="all"></el-tab-pane>
          <el-tab-pane label="待取餐" name="pending"></el-tab-pane>
          <el-tab-pane label="已完成" name="completed"></el-tab-pane>
          <el-tab-pane label="已取消" name="cancelled"></el-tab-pane>
        </el-tabs>
      </div>

      <div class="orders-list" v-if="orders.length > 0">
        <div class="order-card" v-for="order in orders" :key="order.orderId">
          <div class="order-header">
            <span class="order-id">订单号: {{ order.orderId }}</span>
            <span :class="['status', getStatusClass(order.status)]">{{ getStatusText(order.status) }}</span>
          </div>
          
          <div class="order-items">
            <div class="order-item" v-for="item in order.items" :key="item.dishId">
              <img :src="item.imageUrl || '/images/dishes/default.jpg'" alt="菜品图片" class="item-image" />
              <div class="item-info">
                <h4>{{ item.name }}</h4>
                <p>¥{{ item.price }} x {{ item.quantity }}</p>
              </div>
              <div class="item-total">¥{{ (item.price * item.quantity).toFixed(2) }}</div>
            </div>
          </div>
          
          <div class="order-footer">
            <div class="order-info">
              <span>取餐码: <strong>{{ order.pickupCode }}</strong></span>
              <span>取餐时间: {{ order.pickupTime }}</span>
            </div>
            <div class="order-total">
              合计: <span>¥{{ order.totalAmount }}</span>
            </div>
          </div>
          
          <div class="order-actions">
            <el-button v-if="order.status === 3" type="primary" size="small">去取餐</el-button>
            <el-button v-if="order.status === 0 || order.status === 1 || order.status === 2" type="danger" size="small" @click="cancelOrder(order.orderId)">取消订单</el-button>
          </div>
        </div>
      </div>

      <div class="empty-state" v-else>
        <span class="empty-icon">📋</span>
        <p>暂无订单</p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores'
import { orderApi } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('all')
const orders = ref([])

const statusMap = {
  0: '待支付',
  1: '待接单',
  2: '待出餐',
  3: '待取餐',
  4: '已完成',
  5: '已取消'
}

const statusClassMap = {
  0: 'status-pending',
  1: 'status-pending',
  2: 'status-processing',
  3: 'status-ready',
  4: 'status-completed',
  5: 'status-cancelled'
}

const getStatusText = (status) => {
  return statusMap[status] || '未知'
}

const getStatusClass = (status) => {
  return statusClassMap[status] || ''
}

const loadOrders = async () => {
  if (!userStore.user) return
  
  let status = null
  switch (activeTab.value) {
    case 'pending':
      status = [1, 2, 3]
      break
    case 'completed':
      status = 4
      break
    case 'cancelled':
      status = 5
      break
    default:
      status = null
  }
  
  try {
    orders.value = await orderApi.getOrders(userStore.user.userId, status)
  } catch (error) {
    console.error('加载订单失败:', error)
  }
}

const cancelOrder = async (orderId) => {
  try {
    await orderApi.cancelOrder(orderId)
    ElMessage.success('取消成功')
    loadOrders()
  } catch (error) {
    ElMessage.error(error.message || '取消失败')
  }
}

const goBack = () => {
  router.push('/')
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.orders-container {
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

.orders-content {
  padding: 30px;
  max-width: 800px;
  margin: 0 auto;
}

.tabs {
  margin-bottom: 20px;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card {
  background: white;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.order-header {
  display: flex;
  justify-content: space-between;
  padding: 15px 20px;
  background: #f8f9fa;
}

.order-id {
  color: #666;
  font-size: 14px;
}

.status {
  padding: 5px 15px;
  border-radius: 20px;
  font-size: 12px;
}

.status-pending {
  background: #fff3cd;
  color: #856404;
}

.status-processing {
  background: #cce5ff;
  color: #004085;
}

.status-ready {
  background: #d4edda;
  color: #155724;
}

.status-completed {
  background: #e7e7e7;
  color: #666;
}

.status-cancelled {
  background: #f8d7da;
  color: #721c24;
}

.order-items {
  padding: 15px 20px;
}

.order-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.order-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  object-fit: cover;
}

.item-info {
  flex: 1;
  padding: 0 15px;
}

.item-info h4 {
  margin-bottom: 5px;
  color: #333;
}

.item-info p {
  font-size: 14px;
  color: #999;
}

.item-total {
  color: #e74c3c;
  font-weight: bold;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  padding: 15px 20px;
  background: #f8f9fa;
}

.order-info {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #666;
}

.order-info strong {
  color: #e74c3c;
}

.order-total span {
  color: #e74c3c;
  font-weight: bold;
  font-size: 18px;
}

.order-actions {
  padding: 15px 20px;
  text-align: right;
}

.empty-state {
  text-align: center;
  padding: 100px;
  background: white;
  border-radius: 10px;
}

.empty-icon {
  font-size: 80px;
  display: block;
  margin-bottom: 20px;
}
</style>