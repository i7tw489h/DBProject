<template>
  <div class="admin-container">
    <header class="header">
      <div class="header-left">
        <span class="logo">⚙️</span>
        <span class="title">食堂管理后台</span>
      </div>
      <div class="header-right">
        <el-button @click="goBack">返回首页</el-button>
      </div>
    </header>

    <div class="admin-content">
      <aside class="sidebar">
        <el-menu :default-active="activeMenu" class="admin-menu">
          <el-menu-item index="dishes" @click="activeMenu = 'dishes'">🍳 菜品管理</el-menu-item>
          <el-menu-item index="orders" @click="activeMenu = 'orders'">📋 订单管理</el-menu-item>
          <el-menu-item index="inventory" @click="activeMenu = 'inventory'">📦 库存管理</el-menu-item>
          <el-menu-item index="statistics" @click="activeMenu = 'statistics'">📊 数据统计</el-menu-item>
        </el-menu>
      </aside>

      <main class="content">
        <div v-if="activeMenu === 'dishes'">
          <div class="section-header">
            <h2>🍳 菜品管理</h2>
            <el-button type="primary" @click="showAddModal = true">添加菜品</el-button>
          </div>
          
          <el-table :data="dishList" border class="dish-table">
            <el-table-column prop="dishId" label="ID"></el-table-column>
            <el-table-column prop="name" label="菜品名称"></el-table-column>
            <el-table-column prop="price" label="价格"></el-table-column>
            <el-table-column prop="categoryName" label="分类"></el-table-column>
            <el-table-column prop="windowName" label="窗口"></el-table-column>
            <el-table-column prop="stock" label="库存"></el-table-column>
            <el-table-column prop="isActive" label="状态">
              <template #default="scope">
                <el-tag :type="scope.row.isActive ? 'success' : 'danger'">
                  {{ scope.row.isActive ? '上架' : '下架' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button size="small" @click="editDish(scope.row)">编辑</el-button>
                <el-button size="small" :type="scope.row.isActive ? 'warning' : 'success'" @click="toggleDish(scope.row)">
                  {{ scope.row.isActive ? '下架' : '上架' }}
                </el-button>
                <el-button size="small" type="danger" @click="deleteDish(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div v-if="activeMenu === 'orders'">
          <div class="section-header">
            <h2>📋 订单管理</h2>
            <div class="filter-group">
              <div class="filter-item">
                <span class="filter-label">订单状态:</span>
                <el-select v-model="orderStatus" value="all" @change="loadOrders" style="width: 120px;">
                  <el-option label="全部状态" value="all"></el-option>
                  <el-option label="待接单" :value="1"></el-option>
                  <el-option label="待出餐" :value="2"></el-option>
                  <el-option label="待取餐" :value="3"></el-option>
                </el-select>
              </div>
              <div class="filter-item">
                <span class="filter-label">取餐时间:</span>
                <el-select v-model="pickupTimeFilter" value="all" @change="loadOrders" style="width: 120px;">
                  <el-option label="全部时间" value="all"></el-option>
                  <el-option label="11:30-12:00" value="11:30-12:00"></el-option>
                  <el-option label="12:00-12:30" value="12:00-12:30"></el-option>
                  <el-option label="12:30-13:00" value="12:30-13:00"></el-option>
                </el-select>
              </div>
              <div class="filter-item">
                <span class="filter-label">窗口:</span>
                <el-select v-model="windowFilter" value="all" @change="loadOrders" style="width: 120px;">
                  <el-option label="全部窗口" value="all"></el-option>
                  <el-option v-for="win in windows" :key="win.windowId" :label="win.name" :value="win.windowId"></el-option>
                </el-select>
              </div>
            </div>
          </div>
          
          <el-table :data="orderList" border class="order-table">
            <el-table-column prop="orderId" label="订单号" width="180"></el-table-column>
            <el-table-column label="用户信息" width="200">
              <template #default="scope">
                <div class="user-info">
                  <span class="user-name">{{ scope.row.userName }}</span>
                  <span class="user-phone">{{ scope.row.phone }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="pickupTime" label="取餐时间" width="120"></el-table-column>
            <el-table-column prop="pickupCode" label="取餐码" width="100">
              <template #default="scope">
                <span class="pickup-code">{{ scope.row.pickupCode }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="totalAmount" label="金额" width="100">
              <template #default="scope">¥{{ scope.row.totalAmount }}</template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getOrderStatusType(scope.row.status)">
                  {{ getOrderStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="备注" width="150">
              <template #default="scope">
                <span v-if="scope.row.remark" class="remark-text" :title="scope.row.remark">
                  {{ scope.row.remark.length > 10 ? scope.row.remark.substring(0, 10) + '...' : scope.row.remark }}
                </span>
                <span v-else class="no-remark">-</span>
              </template>
            </el-table-column>
            <el-table-column label="下单时间" width="150">
              <template #default="scope">{{ formatDate(scope.row.createdAt) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="scope">
                <el-button v-if="scope.row.status === 1" size="small" type="primary" @click="acceptOrder(scope.row)">接单</el-button>
                <el-button v-if="scope.row.status === 2" size="small" type="success" @click="serveOrder(scope.row)">出餐</el-button>
                <el-button v-if="scope.row.status === 3" size="small" type="info" disabled>待取餐</el-button>
                <el-button v-if="scope.row.status === 4" size="small" type="info" disabled>已完成</el-button>
                <el-button v-if="scope.row.status === 5" size="small" type="danger" disabled>已取消</el-button>
              </template>
            </el-table-column>
          </el-table>


        </div>

        <div v-if="activeMenu === 'inventory'">
          <div class="section-header">
            <h2>📦 库存管理</h2>
          </div>
          
          <div class="alert-section">
            <h3>⚠️ 库存不足提醒</h3>
            <div v-if="lowStockItems.length > 0" class="low-stock-list">
              <div class="low-stock-item" v-for="item in lowStockItems" :key="item.dishId">
                <span>{{ item.name }}</span>
                <span class="stock">库存: {{ item.stock }}</span>
              </div>
            </div>
            <p v-else class="no-alert">暂无库存不足的菜品</p>
          </div>
        </div>

        <div v-if="activeMenu === 'statistics'">
          <div class="section-header">
            <h2>📊 数据统计</h2>
          </div>
          
          <div class="stats-grid">
            <div class="stats-card">
              <span class="stats-icon">💰</span>
              <p class="stats-value">¥{{ statistics.totalSales || 0 }}</p>
              <p class="stats-label">总销售额</p>
            </div>
            <div class="stats-card">
              <span class="stats-icon">📈</span>
              <p class="stats-value">{{ statistics.totalOrders || 0 }}</p>
              <p class="stats-label">总订单数</p>
            </div>
            <div class="stats-card">
              <span class="stats-icon">🍽️</span>
              <p class="stats-value">{{ statistics.totalDishes || 0 }}</p>
              <p class="stats-label">菜品总数</p>
            </div>
            <div class="stats-card">
              <span class="stats-icon">👥</span>
              <p class="stats-value">{{ statistics.totalUsers || 0 }}</p>
              <p class="stats-label">用户总数</p>
            </div>
          </div>

          <div class="ranking-section">
            <h3>🏆 销量排行榜</h3>
            <el-table :data="salesRanking" border class="ranking-table">
              <el-table-column label="排名">
                <template #default="scope">
                  <span v-if="scope.$index === 0" class="rank gold">🥇</span>
                  <span v-else-if="scope.$index === 1" class="rank silver">🥈</span>
                  <span v-else-if="scope.$index === 2" class="rank bronze">🥉</span>
                  <span v-else>{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="菜品名称"></el-table-column>
              <el-table-column prop="salesCount" label="销量"></el-table-column>
              <el-table-column prop="salesAmount" label="销售额"></el-table-column>
            </el-table>
          </div>
        </div>
      </main>
    </div>

    <el-dialog title="添加菜品" :visible.sync="showAddModal">
      <el-form :model="dishForm" class="dish-form">
        <el-form-item label="菜品名称">
          <el-input v-model="dishForm.name"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="dishForm.price" type="number"></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="dishForm.categoryId">
            <el-option v-for="cat in categories" :key="cat.categoryId" :label="cat.name" :value="cat.categoryId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="窗口">
          <el-select v-model="dishForm.windowId">
            <el-option v-for="win in windows" :key="win.windowId" :label="win.name" :value="win.windowId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="库存">
          <el-input v-model="dishForm.stock" type="number"></el-input>
        </el-form-item>
        <el-form-item label="热量">
          <el-input v-model="dishForm.calories" type="number"></el-input>
        </el-form-item>
        <el-form-item label="蛋白质">
          <el-input v-model="dishForm.protein" type="number"></el-input>
        </el-form-item>
        <el-form-item label="脂肪">
          <el-input v-model="dishForm.fat" type="number"></el-input>
        </el-form-item>
        <el-form-item label="碳水">
          <el-input v-model="dishForm.carbs" type="number"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="saveDish">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { adminApi, dishApi, orderApi } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()

const activeMenu = ref('dishes')
const showAddModal = ref(false)
const orderStatus = ref('all')
const pickupTimeFilter = ref('all')
const windowFilter = ref('all')

const dishList = ref([])
const orderList = ref([])
const lowStockItems = ref([])
const statistics = ref({})
const salesRanking = ref([])
const categories = ref([])
const windows = ref([])

const dishForm = reactive({
  name: '',
  price: '',
  categoryId: '',
  windowId: '',
  stock: 100,
  calories: 0,
  protein: 0,
  fat: 0,
  carbs: 0
})

const orderStatusMap = {
  0: '待支付',
  1: '待接单',
  2: '待出餐',
  3: '待取餐',
  4: '已完成',
  5: '已取消'
}

const orderStatusTypeMap = {
  0: 'warning',
  1: 'info',
  2: 'primary',
  3: 'success',
  4: 'default',
  5: 'danger'
}

const getOrderStatusText = (status) => orderStatusMap[status] || '未知'
const getOrderStatusType = (status) => orderStatusTypeMap[status] || 'default'

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const loadDishes = async () => {
  try {
    const result = await adminApi.getDishList()
    dishList.value = result.records || result || []
  } catch (error) {
    console.error('加载菜品失败:', error)
  }
}

const loadOrders = async () => {
  try {
    const params = {}
    if (orderStatus.value !== 'all') {
      params.status = orderStatus.value
    }
    if (pickupTimeFilter.value !== 'all') {
      params.pickupTime = pickupTimeFilter.value
    }
    if (windowFilter.value !== 'all') {
      params.windowId = windowFilter.value
    }
    orderList.value = await orderApi.getAllOrders(params)
  } catch (error) {
    console.error('加载订单失败:', error)
  }
}

const loadLowStock = async () => {
  try {
    lowStockItems.value = await adminApi.getLowStockAlert()
  } catch (error) {
    console.error('加载库存失败:', error)
  }
}

const loadStatistics = async () => {
  try {
    statistics.value = await adminApi.getSalesStatistics()
  } catch (error) {
    console.error('加载统计失败:', error)
  }
}

const loadSalesRanking = async () => {
  try {
    salesRanking.value = await adminApi.getSalesRanking()
  } catch (error) {
    console.error('加载销量排行失败:', error)
  }
}

const loadCategories = async () => {
  try {
    categories.value = await dishApi.getCategories()
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

const loadWindows = async () => {
  try {
    windows.value = await dishApi.getWindows()
  } catch (error) {
    console.error('加载窗口失败:', error)
  }
}

const saveDish = async () => {
  try {
    if (!dishForm.name || !dishForm.price) {
      ElMessage.warning('请填写必填项')
      return
    }
    await adminApi.addDish(dishForm)
    ElMessage.success('添加成功')
    showAddModal.value = false
    loadDishes()
  } catch (error) {
    ElMessage.error(error.message || '添加失败')
  }
}

const editDish = (dish) => {
  Object.assign(dishForm, dish)
  showAddModal.value = true
}

const toggleDish = async (dish) => {
  try {
    await adminApi.toggleDishStatus(dish.dishId)
    ElMessage.success(dish.isActive ? '已下架' : '已上架')
    loadDishes()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

const deleteDish = async (dish) => {
  try {
    await adminApi.deleteDish(dish.dishId)
    ElMessage.success('删除成功')
    loadDishes()
  } catch (error) {
    ElMessage.error(error.message || '删除失败')
  }
}

const acceptOrder = async (order) => {
  try {
    await orderApi.acceptOrder(order.orderId)
    ElMessage.success('接单成功')
    loadOrders()
  } catch (error) {
    ElMessage.error(error.message || '接单失败')
  }
}

const serveOrder = async (order) => {
  try {
    await orderApi.serveOrder(order.orderId)
    ElMessage.success('出餐成功')
    loadOrders()
  } catch (error) {
    ElMessage.error(error.message || '出餐失败')
  }
}

const goBack = () => {
  router.push('/')
}

onMounted(() => {
  loadDishes()
  loadOrders()
  loadLowStock()
  loadStatistics()
  loadSalesRanking()
  loadCategories()
  loadWindows()
})
</script>

<style scoped>
.admin-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  background: #2c3e50;
  padding: 15px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
  color: white;
}

.logo {
  font-size: 30px;
}

.title {
  font-size: 20px;
  font-weight: bold;
}

.admin-content {
  display: flex;
  height: calc(100vh - 65px);
}

.sidebar {
  width: 200px;
  background: white;
  border-right: 1px solid #e0e0e0;
}

.admin-menu {
  border: none;
}

.admin-menu :deep(.el-menu-item) {
  color: #333;
  border-radius: 0;
}

.admin-menu :deep(.el-menu-item:hover),
.admin-menu :deep(.el-menu-item.is-active) {
  background: #f5f7fa;
  color: #409eff;
}

.content {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h2 {
  color: #333;
}

.filter-group {
  display: flex;
  gap: 20px;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  color: #666;
  font-size: 14px;
}

.dish-table, .order-table, .ranking-table {
  width: 100%;
}

.alert-section {
  background: #fff3cd;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
}

.alert-section h3 {
  margin-bottom: 15px;
  color: #856404;
}

.low-stock-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.low-stock-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 15px;
  background: white;
  border-radius: 8px;
}

.stock {
  color: #dc3545;
  font-weight: bold;
}

.no-alert {
  text-align: center;
  color: #856404;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.stats-card {
  background: white;
  border-radius: 10px;
  padding: 20px;
  text-align: center;
}

.stats-icon {
  font-size: 40px;
  display: block;
  margin-bottom: 10px;
}

.stats-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.stats-label {
  color: #999;
}

.ranking-section {
  background: white;
  border-radius: 10px;
  padding: 20px;
}

.ranking-section h3 {
  margin-bottom: 20px;
  color: #333;
}

.rank {
  font-size: 24px;
}

.dish-form {
  padding: 20px;
}

.filter-group {
  display: flex;
  gap: 15px;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  color: #606266;
  font-size: 14px;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-name {
  font-weight: 500;
  color: #303133;
}

.user-phone {
  font-size: 12px;
  color: #909399;
}

.pickup-code {
  color: #409eff;
  font-weight: bold;
  font-size: 16px;
}

.remark-text {
  color: #e6a23c;
  font-size: 13px;
  cursor: pointer;
}

.no-remark {
  color: #c0c4cc;
}

.order-detail {
  padding: 10px;
}

.detail-section {
  margin-bottom: 20px;
}

.detail-section h4 {
  margin-bottom: 12px;
  color: #303133;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 8px;
}

.detail-row {
  display: flex;
  margin-bottom: 10px;
  align-items: center;
}

.detail-label {
  color: #606266;
  width: 80px;
  flex-shrink: 0;
}

.detail-value {
  color: #303133;
}

.remark-content {
  color: #e6a23c;
  font-weight: 500;
  padding: 4px 8px;
  background: #fdf6ec;
  border-radius: 4px;
  border: 1px solid #f5dab1;
}

.total-section {
  border-top: 2px solid #ebeef5;
  padding-top: 15px;
  margin-top: 20px;
}

.total-amount {
  font-size: 18px;
  font-weight: bold;
  color: #f56c6c;
}
</style>