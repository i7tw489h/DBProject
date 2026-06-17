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
        <el-tabs v-model="activeTab" @tab-change="handleTabChange">
          <el-tab-pane label="全部订单" name="all"></el-tab-pane>
          <el-tab-pane label="待接单" name="pendingAccept"></el-tab-pane>
          <el-tab-pane label="待出餐" name="pendingServe"></el-tab-pane>
          <el-tab-pane label="待取餐" name="pendingPickup"></el-tab-pane>
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
          
          <div class="order-remark" v-if="order.remark">
            <span class="remark-label">备注:</span>
            <span class="remark-content">{{ order.remark }}</span>
          </div>
          
          <div class="order-footer">
            <div class="order-info">
              <span>取餐码: <strong class="pickup-code">{{ order.pickupCode }}</strong></span>
              <span>取餐时间: {{ order.pickupTime }}</span>
            </div>
            <div class="order-total">
              合计: <span class="total-price">¥{{ order.totalAmount }}</span>
            </div>
          </div>
          
          <div class="order-actions">
            <el-button v-if="order.status === 3" type="primary" size="small" @click="finishOrder(order.orderId)">去取餐</el-button>
            <el-button v-if="order.status === 1 || order.status === 2" type="danger" size="small" @click="cancelOrder(order.orderId)">取消订单</el-button>
          </div>
        </div>
      </div>

      <div class="pagination-container" v-if="totalOrders > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[6, 12, 18, 24]"
          :total="totalOrders"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>

      <div class="empty-state" v-if="orders.length === 0">
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
const currentPage = ref(1)
const pageSize = ref(6)
const totalOrders = ref(0)

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
    case 'pendingAccept':
      status = 1
      break
    case 'pendingServe':
      status = 2
      break
    case 'pendingPickup':
      status = 3
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
    const result = await orderApi.getOrders(userStore.user.userId, status, currentPage.value, pageSize.value)
    orders.value = result.records || result || []
    totalOrders.value = result.total || orders.value.length
  } catch (error) {
    console.error('加载订单失败:', error)
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadOrders()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadOrders()
}

const handleTabChange = () => {
  currentPage.value = 1
  loadOrders()
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

const finishOrder = async (orderId) => {
  try {
    await orderApi.finishOrder(orderId)
    ElMessage.success('取餐成功，订单已完成')
    loadOrders()
  } catch (error) {
    ElMessage.error(error.message || '取餐失败')
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

.order-remark {
  padding: 10px 20px;
  background: #fff8e1;
  border-left: 3px solid #ffc107;
  margin: 0 20px;
  border-radius: 4px;
}

.remark-label {
  color: #ff9800;
  font-weight: bold;
  margin-right: 5px;
}

.remark-content {
  color: #666;
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

.pickup-code {
  color: #e74c3c;
  font-weight: bold;
}

.total-price {
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

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 20px;
  background: white;
  border-radius: 10px;
}

.empty-icon {
  font-size: 80px;
  display: block;
  margin-bottom: 20px;
}
</style>