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
          <el-menu-item index="prediction" @click="activeMenu = 'prediction'">🤖 AI销量预测</el-menu-item>
        </el-menu>
      </aside>

      <main class="content">
        <div v-if="activeMenu === 'dishes'" class="page-container">
          <div class="section-header">
            <h2>🍳 菜品管理</h2>
            <div class="header-actions">
              <el-button type="success" @click="exportDishExcel">导出Excel</el-button>
              <el-button type="primary" @click="showAddModal = true">添加菜品</el-button>
            </div>
          </div>
          
          <div class="filter-bar">
            <div class="search-box">
              <el-input 
                v-model="searchKeyword" 
                placeholder="搜索菜品名称" 
                class="search-input"
                @keyup.enter="loadDishes">
                <template #append>
                  <el-button @click="loadDishes">搜索</el-button>
                </template>
              </el-input>
            </div>
            
            <div class="filter-group">
              <el-select v-model="filterCategory" placeholder="选择分类" @change="loadDishes">
                <el-option label="全部" :value="''"></el-option>
                <el-option v-for="cat in categories" :key="cat.categoryId" :label="cat.name" :value="cat.categoryId"></el-option>
              </el-select>
              
              <el-select v-model="filterWindow" placeholder="选择窗口" @change="loadDishes">
                <el-option label="全部" :value="''"></el-option>
                <el-option v-for="win in windows" :key="win.windowId" :label="win.name" :value="win.windowId"></el-option>
              </el-select>
              
              <el-select v-model="filterStatus" placeholder="选择状态" @change="loadDishes">
                <el-option label="全部" :value="''"></el-option>
                <el-option label="上架" :value="true"></el-option>
                <el-option label="下架" :value="false"></el-option>
              </el-select>
            </div>
            
            <div class="sort-group">
              <span class="sort-label">排序：</span>
              <el-select v-model="sortField" placeholder="选择字段" @change="loadDishes">
                <el-option label="默认" value=""></el-option>
                <el-option label="价格" value="price"></el-option>
                <el-option label="库存" value="stock"></el-option>
                <el-option label="销量" value="salesCount"></el-option>
              </el-select>
              <el-select v-model="sortOrder" placeholder="排序方式" @change="loadDishes">
                <el-option label="升序" value="asc"></el-option>
                <el-option label="降序" value="desc"></el-option>
              </el-select>
            </div>
            
            <div class="batch-actions" v-if="selectedDishes.length > 0">
              <span class="selected-count">已选择 {{ selectedDishes.length }} 项</span>
              <el-button type="success" @click="batchShelf(true)" :disabled="selectedDishes.length === 0">批量上架</el-button>
              <el-button type="warning" @click="batchShelf(false)" :disabled="selectedDishes.length === 0">批量下架</el-button>
              <el-button type="danger" @click="batchDelete" :disabled="selectedDishes.length === 0">批量删除</el-button>
            </div>
          </div>
          
          <el-table :data="dishList" border class="dish-table" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="dishId" label="ID"></el-table-column>
            <el-table-column prop="name" label="菜品名称">
              <template #default="scope">
                <span class="dish-name" @click="showDishDetail(scope.row)">{{ scope.row.name }}</span>
              </template>
            </el-table-column>
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
            <el-table-column label="操作" width="220">
              <template #default="scope">
                <el-button size="small" @click="editDish(scope.row)">修改</el-button>
                <el-button size="small" :type="scope.row.isActive ? 'warning' : 'success'" @click="toggleDish(scope.row)">
                  {{ scope.row.isActive ? '下架' : '上架' }}
                </el-button>
                <el-button size="small" type="danger" @click="deleteDish(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <div class="pagination-container">
            <span class="total-text">共 {{ dishTotal }} 道菜品</span>
            <el-pagination
              @size-change="handleDishPageSizeChange"
              @current-change="handleDishCurrentChange"
              :current-page="dishCurrentPage"
              :page-sizes="[5, 10, 20, 50]"
              :page-size="dishPageSize"
              :total="dishTotal"
              layout="sizes, prev, pager, next, jumper"
              prev-text="上一页"
              next-text="下一页"
              page-size-text="条/页"
              jumper-text="跳转到第"
              jumper-suffix="页">
            </el-pagination>
          </div>
        </div>
        
        <el-dialog v-model="showDetailModal" title="菜品详情" width="500px" center>
          <div class="dish-detail">
            <div class="dish-image" v-if="currentDish.imageUrl">
              <img :src="currentDish.imageUrl" alt="菜品图片" class="detail-image">
            </div>
            <div class="dish-image placeholder" v-else>
              <span class="no-image">暂无图片</span>
            </div>
            <div class="dish-info">
              <h3 class="dish-title">{{ currentDish.name }}</h3>
              <div class="dish-price">¥{{ currentDish.price }}</div>
              <div class="info-row">
                <span class="label">分类：</span>
                <span>{{ currentDish.categoryName }}</span>
              </div>
              <div class="info-row">
                <span class="label">窗口：</span>
                <span>{{ currentDish.windowName }}</span>
              </div>
              <div class="info-row">
                <span class="label">库存：</span>
                <span :class="{ 'low-stock': currentDish.stock < 20 }">{{ currentDish.stock }} 份</span>
              </div>
              <div class="info-row">
                <span class="label">销量：</span>
                <span>{{ currentDish.salesCount }} 份</span>
              </div>
              <div class="info-row">
                <span class="label">状态：</span>
                <el-tag :type="currentDish.isActive ? 'success' : 'warning'">
                  {{ currentDish.isActive ? '上架中' : '已下架' }}
                </el-tag>
              </div>
              <div class="info-row" v-if="currentDish.description">
                <span class="label">描述：</span>
                <span>{{ currentDish.description }}</span>
              </div>
              <div class="info-row" v-if="currentDish.ingredients">
                <span class="label">食材：</span>
                <span>{{ currentDish.ingredients }}</span>
              </div>
            </div>
          </div>
        </el-dialog>

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
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button v-if="scope.row.status === 1" size="small" type="primary" @click="acceptOrder(scope.row)">接单</el-button>
                <el-button v-if="scope.row.status === 2" size="small" type="success" @click="serveOrder(scope.row)">出餐</el-button>
                <el-button v-if="scope.row.status === 3" size="small" type="info" disabled>待取餐</el-button>
                <el-button v-if="scope.row.status === 4" size="small" type="info" disabled>已完成</el-button>
                <el-button v-if="scope.row.status === 5" size="small" type="danger" disabled>已取消</el-button>
                <el-button v-if="scope.row.status === 4 || scope.row.status === 5" size="small" type="danger" @click="deleteOrder(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <div class="pagination-container" v-if="orderTotal > orderPageSize">
            <el-pagination
              v-model:current-page="orderCurrentPage"
              :page-size="orderPageSize"
              :total="orderTotal"
              layout="prev, pager, next"
              @current-change="handleOrderPageChange"
              prev-text="上一页"
              next-text="下一页"
            />
          </div>
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

        <!-- AI销量预测 -->
        <div v-if="activeMenu === 'prediction'">
          <div class="section-header">
            <h2>🤖 AI销量预测</h2>
            <div class="prediction-actions">
              <span class="prediction-date-info">📅 预测日期：明天（{{ getTomorrowDate() }}）</span>
              <el-button type="primary" @click="generatePrediction" :loading="predictionLoading">
                生成预测
              </el-button>
              <el-button type="success" @click="getSuggestions">
                获取备餐建议
              </el-button>
            </div>
          </div>

          <!-- 预测统计卡片 -->
          <div class="stats-grid">
            <div class="stats-card">
              <span class="stats-icon">🎯</span>
              <p class="stats-value">{{ predictionStats.accuracyRate || 0 }}%</p>
              <p class="stats-label">预测准确率</p>
            </div>
            <div class="stats-card">
              <span class="stats-icon">📊</span>
              <p class="stats-value">{{ predictionStats.totalPredictions || 0 }}</p>
              <p class="stats-label">总预测数</p>
            </div>
            <div class="stats-card">
              <span class="stats-icon">⚠️</span>
              <p class="stats-value">{{ predictionStats.averageError || 0 }}</p>
              <p class="stats-label">平均误差</p>
            </div>
            <div class="stats-card">
              <span class="stats-icon">✅</span>
              <p class="stats-value">{{ predictionStats.evaluatedPredictions || 0 }}</p>
              <p class="stats-label">已评估</p>
            </div>
          </div>

          <!-- 预测结果表格 -->
          <div class="prediction-section">
            <h3>📈 预测结果</h3>
            <el-alert
              v-if="predictionDate"
              :title="'预测日期: ' + predictionDate + ' (' + getWeekday(predictionDate) + ')'"
              type="info"
              :closable="false"
              style="margin-bottom: 15px;"
            ></el-alert>
            <el-table :data="predictionList" border class="prediction-table" v-loading="predictionLoading">
              <el-table-column label="排名" width="80">
                <template #default="scope">
                  <span v-if="scope.$index === 0" class="rank gold">🥇</span>
                  <span v-else-if="scope.$index === 1" class="rank silver">🥈</span>
                  <span v-else-if="scope.$index === 2" class="rank bronze">🥉</span>
                  <span v-else>{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="dishName" label="菜品名称"></el-table-column>
              <el-table-column prop="price" label="价格">
                <template #default="scope">¥{{ scope.row.price }}</template>
              </el-table-column>
              <el-table-column prop="predictedSales" label="预测销量">
                <template #default="scope">
                  <el-tag type="primary">{{ scope.row.predictedSales }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="confidence" label="置信度">
                <template #default="scope">
                  <el-progress 
                    :percentage="(scope.row.confidence * 100).toFixed(0)" 
                    :status="getConfidenceStatus(scope.row.confidence)"
                  ></el-progress>
                </template>
              </el-table-column>
              <el-table-column prop="actualSales" label="实际销量">
                <template #default="scope">
                  <span v-if="scope.row.actualSales">{{ scope.row.actualSales }}</span>
                  <span v-else class="text-muted">-</span>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 备餐建议 -->
          <div class="suggestions-section" v-if="suggestions.length > 0">
            <h3>🍽️ 备餐建议</h3>
            <el-alert
              title="基于AI预测生成的建议，帮助您合理备餐，减少浪费"
              type="success"
              :closable="false"
              style="margin-bottom: 15px;"
            ></el-alert>
            <el-table :data="suggestions" border class="suggestions-table">
              <el-table-column prop="dishName" label="菜品名称"></el-table-column>
              <el-table-column prop="minQuantity" label="最低备餐量">
                <template #default="scope">
                  <el-tag type="warning">{{ scope.row.minQuantity }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="recommendedQuantity" label="建议备餐量">
                <template #default="scope">
                  <el-tag type="success" size="large">{{ scope.row.recommendedQuantity }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="maxQuantity" label="最大备餐量">
                <template #default="scope">
                  <el-tag type="info">{{ scope.row.maxQuantity }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="priority" label="优先级">
                <template #default="scope">
                  <el-tag :type="scope.row.priority === 'high' ? 'danger' : scope.row.priority === 'medium' ? 'warning' : 'info'">
                    {{ scope.row.priority === 'high' ? '高' : scope.row.priority === 'medium' ? '中' : '低' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="confidence" label="置信度">
                <template #default="scope">
                  <span>{{ (scope.row.confidence * 100).toFixed(0) }}%</span>
                </template>
              </el-table-column>
            </el-table>

            <div class="summary-section">
              <h4>📋 备餐汇总</h4>
              <div class="summary-grid">
                <div class="summary-item">
                  <span class="summary-label">总菜品数:</span>
                  <span class="summary-value">{{ suggestions.length }}</span>
                </div>
                <div class="summary-item">
                  <span class="summary-label">建议总份数:</span>
                  <span class="summary-value highlight">{{ totalRecommendedQuantity }}</span>
                </div>
                <div class="summary-item">
                  <span class="summary-label">预计销售额:</span>
                  <span class="summary-value highlight">¥{{ estimatedSales }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>

    <el-dialog title="添加菜品" v-model="showAddModal" width="600px">
      <el-form :model="dishForm" class="dish-form">
        <el-form-item label="菜品名称">
          <el-input v-model="dishForm.name"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="dishForm.price" type="number"></el-input>
        </el-form-item>
        <el-form-item label="菜品图片">
          <div v-if="dishForm.imageUrl" class="image-preview">
            <img :src="dishForm.imageUrl" alt="菜品图片" style="max-width:200px;max-height:200px;" />
          </div>
          <el-input v-model="dishForm.imageUrl" placeholder="请输入图片URL"></el-input>
          <span class="image-hint">提示：可输入图片网址，如 https://example.com/dish.jpg</span>
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
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import { adminApi, dishApi, orderApi, predictionApi } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const activeMenu = ref('dishes')

// AI销量预测相关数据
const predictionList = ref([])
const predictionStats = ref({})
const suggestions = ref([])
const predictionLoading = ref(false)

// 计算属性
const totalRecommendedQuantity = computed(() => {
  return suggestions.value.reduce((sum, item) => sum + item.recommendedQuantity, 0)
})

const estimatedSales = computed(() => {
  return suggestions.value.reduce((sum, item) => {
    const price = item.price || 0
    return sum + (item.recommendedQuantity * price)
  }, 0).toFixed(2)
})

// AI销量预测方法

const generatePrediction = async () => {
  predictionLoading.value = true
  try {
    // 使用明天的日期
    const tomorrow = new Date()
    tomorrow.setDate(tomorrow.getDate() + 1)
    const targetDate = `${tomorrow.getFullYear()}-${String(tomorrow.getMonth() + 1).padStart(2, '0')}-${String(tomorrow.getDate()).padStart(2, '0')}`
    
    const result = await predictionApi.predictSales(targetDate)
    predictionList.value = result
    ElMessage.success('预测生成成功')
  } catch (error) {
    console.error('生成预测失败:', error)
    ElMessage.error('生成预测失败：' + (error.response?.data?.message || error.message))
  } finally {
    predictionLoading.value = false
  }
}

const getSuggestions = async () => {
  predictionLoading.value = true
  try {
    // 使用明天的日期
    const tomorrow = new Date()
    tomorrow.setDate(tomorrow.getDate() + 1)
    const targetDate = `${tomorrow.getFullYear()}-${String(tomorrow.getMonth() + 1).padStart(2, '0')}-${String(tomorrow.getDate()).padStart(2, '0')}`
    
    // 先确保有预测数据
    await predictionApi.predictSales(targetDate)
    // 然后获取备餐建议
    suggestions.value = await predictionApi.getSuggestions()
    ElMessage.success('备餐建议生成成功')
  } catch (error) {
    console.error('获取备餐建议失败:', error)
    ElMessage.error('获取备餐建议失败：' + (error.response?.data?.message || error.message))
  } finally {
    predictionLoading.value = false
  }
}

const getWeekday = (dateStr) => {
  const date = new Date(dateStr)
  const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  return weekdays[date.getDay()]
}

const getTomorrowDate = () => {
  const tomorrow = new Date()
  tomorrow.setDate(tomorrow.getDate() + 1)
  const year = tomorrow.getFullYear()
  const month = String(tomorrow.getMonth() + 1).padStart(2, '0')
  const day = String(tomorrow.getDate()).padStart(2, '0')
  const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  return `${year}-${month}-${day}（${weekdays[tomorrow.getDay()]}）`
}

const getConfidenceStatus = (confidence) => {
  if (confidence >= 0.8) return 'success'
  if (confidence >= 0.6) return 'warning'
  return 'exception'
}

const showAddModal = ref(false)
const orderStatus = ref('all')
const pickupTimeFilter = ref('all')
const windowFilter = ref('all')
const orderCurrentPage = ref(1)
const orderPageSize = ref(6)
const orderTotal = ref(0)

const dishCurrentPage = ref(1)
const dishPageSize = ref(10)
const dishTotal = ref(0)

const searchKeyword = ref('')
const filterCategory = ref('')
const filterWindow = ref('')
const filterStatus = ref('')
const sortField = ref('')
const sortOrder = ref('asc')
const selectedDishes = ref([])
const showDetailModal = ref(false)
const currentDish = ref({})

const orderCurrentPage = ref(1)
const orderPageSize = ref(6)
const orderTotal = ref(0)

const dishList = ref([])
const orderList = ref([])
const lowStockItems = ref([])
const statistics = ref({})
const salesRanking = ref([])
const categories = ref([])
const windows = ref([])

const dishForm = reactive({
  dishId: '',
  name: '',
  price: '',
  imageUrl: '',
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

// 导出菜品 Excel
const exportDishExcel = async () => {
  try {
    const params = {}
    
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    if (filterCategory.value) {
      params.categoryId = filterCategory.value
    }
    if (filterWindow.value) {
      params.windowId = filterWindow.value
    }
    if (filterStatus.value !== '') {
      params.isActive = filterStatus.value
    }
    if (sortField.value) {
      params.sortField = sortField.value
      params.sortOrder = sortOrder.value
    } else {
      params.sortField = 'dishId'
      params.sortOrder = 'asc'
    }
    
    // 直接使用 window.open 触发下载
    const queryParams = new URLSearchParams(params).toString()
    const url = `/api/admin/dishes/export${queryParams ? '?' + queryParams : ''}`
    window.open(url, '_blank')
    
    ElMessage.success('导出成功，请查看下载文件')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败：' + (error.message || '未知错误'))
  }
}

const loadDishes = async () => {
  try {
    const params = {
      pageNum: dishCurrentPage.value,
      pageSize: dishPageSize.value
    }
    
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    if (filterCategory.value) {
      params.categoryId = filterCategory.value
    }
    if (filterWindow.value) {
      params.windowId = filterWindow.value
    }
    if (filterStatus.value !== '') {
      params.isActive = filterStatus.value
    }
    if (sortField.value) {
      params.sortField = sortField.value
      params.sortOrder = sortOrder.value
    } else {
      params.sortField = 'dishId'
      params.sortOrder = 'asc'
    }
    
    const result = await adminApi.getDishList(params)
    dishList.value = result.records || result || []
    dishTotal.value = result.total || dishList.value.length
    selectedDishes.value = []
  } catch (error) {
    console.error('加载菜品失败:', error)
  }
}

const handleSelectionChange = (val) => {
  selectedDishes.value = val
}

const batchShelf = async (isActive) => {
  try {
    const ids = selectedDishes.value.map(dish => dish.dishId)
    await adminApi.batchToggleStatus(ids, isActive)
    ElMessage.success(isActive ? '批量上架成功' : '批量下架成功')
    loadDishes()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

const batchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedDishes.value.length} 个菜品吗？删除后将无法恢复。`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const ids = selectedDishes.value.map(dish => dish.dishId)
    await adminApi.batchDeleteDishes(ids)
    ElMessage.success('批量删除成功')
    loadDishes()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

const showDishDetail = (dish) => {
  currentDish.value = dish
  showDetailModal.value = true
}

const loadOrders = async () => {
  try {
    const params = {
      page: orderCurrentPage.value,
      pageSize: orderPageSize.value
    }
    if (orderStatus.value !== 'all') {
      params.status = orderStatus.value
    }
    if (pickupTimeFilter.value !== 'all') {
      params.pickupTime = pickupTimeFilter.value
    }
    if (windowFilter.value !== 'all') {
      params.windowId = windowFilter.value
    }
    
    const result = await orderApi.getAllOrders(params)
    
    // 处理分页返回结果
    if (result && typeof result === 'object') {
      if (Array.isArray(result.records)) {
        orderList.value = result.records
        orderTotal.value = result.total || result.records.length
      } else if (Array.isArray(result)) {
        orderList.value = result
        orderTotal.value = result.length
      } else {
        orderList.value = []
        orderTotal.value = 0
      }
    } else {
      orderList.value = []
      orderTotal.value = 0
    }
  } catch (error) {
    console.error('加载订单失败:', error)
  }
}

const handleOrderPageChange = (page) => {
  orderCurrentPage.value = page
  loadOrders()
}

const handleOrderSizeChange = (size) => {
  orderPageSize.value = size
  orderCurrentPage.value = 1
  loadOrders()
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
    if (dishForm.dishId) {
      await adminApi.updateDish(dishForm)
      ElMessage.success('修改成功')
    } else {
      await adminApi.addDish(dishForm)
      ElMessage.success('添加成功')
    }
    showAddModal.value = false
    resetDishForm()
    loadDishes()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

const resetDishForm = () => {
  dishForm.dishId = ''
  dishForm.name = ''
  dishForm.price = ''
  dishForm.imageUrl = ''
  dishForm.categoryId = ''
  dishForm.windowId = ''
  dishForm.stock = 100
  dishForm.calories = 0
  dishForm.protein = 0
  dishForm.fat = 0
  dishForm.carbs = 0
}

const editDish = (dish) => {
  resetDishForm()
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

const deleteOrder = async (order) => {
  try {
    await ElMessageBox.confirm('确定要删除这个订单吗？删除后将无法恢复。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await orderApi.deleteOrder(order.orderId)
    ElMessage.success('删除成功')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

const handleOrderPageChange = (page) => {
  orderCurrentPage.value = page
  loadOrders()
}

const handleDishCurrentChange = (page) => {
  dishCurrentPage.value = page
  loadDishes()
}

const handleDishPageSizeChange = (size) => {
  dishPageSize.value = size
  dishCurrentPage.value = 1
  loadDishes()
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

// 监听筛选条件变化，重置页码
watch([orderStatus, pickupTimeFilter, windowFilter], () => {
  orderCurrentPage.value = 1
})
</script>

<style scoped>
.admin-container {
  min-height: 100vh;
  background: #f5f7fa;
  position: relative;
  z-index: 1;
}

:deep(.el-dialog) {
  z-index: 9999 !important;
}

:deep(.el-overlay) {
  z-index: 9998 !important;
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

.header-actions {
  display: flex;
  gap: 10px;
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

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 10px 0;
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

.page-container {
  min-height: calc(100vh - 200px);
  display: flex;
  flex-direction: column;
}

.filter-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  padding: 15px 20px;
  background: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 20px;
  align-items: center;
}

.search-box {
  flex: 1;
  min-width: 200px;
  max-width: 300px;
}

.search-input {
  width: 100%;
}

.filter-group {
  display: flex;
  gap: 10px;
}

.filter-group .el-select {
  width: 140px;
}

.sort-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.sort-label {
  color: #666;
  font-size: 14px;
}

.sort-group .el-select {
  width: 100px;
}

.batch-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  padding-left: 15px;
  border-left: 1px solid #ddd;
}

.selected-count {
  color: #666;
  font-size: 14px;
}

.dish-name {
  color: #409eff;
  cursor: pointer;
  text-decoration: underline;
}

.dish-name:hover {
  color: #67c23a;
}

.dish-detail {
  padding: 20px;
}

.dish-image {
  text-align: center;
  margin-bottom: 20px;
}

.dish-image.placeholder {
  height: 200px;
  line-height: 200px;
  background: #f5f5f5;
  border-radius: 8px;
}

.no-image {
  color: #999;
}

.detail-image {
  max-width: 100%;
  max-height: 200px;
  border-radius: 8px;
}

.dish-info {
  line-height: 2;
}

.dish-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
}

.dish-price {
  font-size: 24px;
  color: #f56c6c;
  font-weight: bold;
  margin-bottom: 15px;
}

.info-row {
  margin-bottom: 10px;
}

.info-row .label {
  color: #999;
  font-size: 14px;
}

.info-row .low-stock {
  color: #f56c6c;
  font-weight: bold;
}

.pagination-container {
  margin-top: auto;
  padding: 20px 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid #e0e0e0;
}

.total-text {
  color: #666;
  font-size: 14px;
}

/* AI销量预测样式 */
.prediction-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.prediction-section {
  background: white;
  border-radius: 10px;
  padding: 20px;
  margin-top: 20px;
}

.prediction-section h3 {
  margin-bottom: 20px;
  color: #333;
}

.prediction-table {
  margin-top: 15px;
}

.suggestions-section {
  background: white;
  border-radius: 10px;
  padding: 20px;
  margin-top: 20px;
}

.suggestions-section h3 {
  margin-bottom: 20px;
  color: #333;
}

.suggestions-table {
  margin-top: 15px;
}

.summary-section {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 20px;
  margin-top: 20px;
}

.summary-section h4 {
  margin-bottom: 15px;
  color: #333;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  padding: 10px;
  background: white;
  border-radius: 5px;
}

.summary-label {
  color: #666;
}

.summary-value {
  font-weight: bold;
  color: #333;
}

.summary-value.highlight {
  color: #409eff;
  font-size: 18px;
}

.text-muted {
  color: #999;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stats-card {
  background: white;
  border-radius: 10px;
  padding: 25px;
  text-align: center;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.stats-icon {
  font-size: 36px;
  display: block;
  margin-bottom: 10px;
}

.stats-value {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin: 10px 0;
}

.stats-label {
  color: #666;
  font-size: 14px;
}
</style>