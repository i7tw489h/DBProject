<template>
  <div class="dish-detail-container">
    <header class="header">
      <div class="header-left">
        <el-button @click="goBack">← 返回</el-button>
        <span class="title">菜品详情</span>
      </div>
    </header>

    <main class="dish-content" v-if="dishData">
      <div class="dish-main">
        <img :src="dishData.dish.imageUrl || '/images/dishes/default.jpg'" alt="菜品图片" class="dish-image" />
        <div class="dish-info">
          <h1>{{ dishData.dish.name }}</h1>
          <p class="price">¥{{ dishData.dish.price }}</p>
          <p class="description">{{ dishData.dish.description || '暂无描述' }}</p>
          <div class="tags">
            <el-tag>{{ categoryName }}</el-tag>
            <el-tag>{{ windowName }}</el-tag>
          </div>
          <div class="nutrition-info">
            <h3>营养信息</h3>
            <div class="nutrition-grid">
              <div class="nutrition-item">
                <span class="icon">🔥</span>
                <span class="label">热量</span>
                <span class="value">{{ dishData.nutrition?.calories || 0 }}kcal</span>
              </div>
              <div class="nutrition-item">
                <span class="icon">💪</span>
                <span class="label">蛋白质</span>
                <span class="value">{{ dishData.nutrition?.protein || 0 }}g</span>
              </div>
              <div class="nutrition-item">
                <span class="icon">🥑</span>
                <span class="label">脂肪</span>
                <span class="value">{{ dishData.nutrition?.fat || 0 }}g</span>
              </div>
              <div class="nutrition-item">
                <span class="icon">🍞</span>
                <span class="label">碳水</span>
                <span class="value">{{ dishData.nutrition?.carbs || 0 }}g</span>
              </div>
            </div>
          </div>
          <div class="ingredients">
            <h3>配料</h3>
            <p>{{ dishData.dish.ingredients || '暂无信息' }}</p>
          </div>
        </div>
      </div>

      <div class="action-section">
        <div class="quantity-control">
          <el-button size="large" @click="decreaseQty">-</el-button>
          <span class="quantity">{{ quantity }}</span>
          <el-button size="large" @click="increaseQty">+</el-button>
        </div>
        <el-button type="primary" size="large" class="add-btn" @click="addToCart">
          加入购物车
        </el-button>
      </div>
    </main>

    <div class="loading" v-else>
      <span class="loading-icon">🍽️</span>
      <p>加载中...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useCartStore } from '@/stores'
import { dishApi } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const cartStore = useCartStore()

const dishData = ref(null)
const quantity = ref(1)
const categories = ref([])
const windows = ref([])

const categoryName = computed(() => {
  if (!dishData.value) return ''
  const cat = categories.value.find(c => c.categoryId === dishData.value.dish.categoryId)
  return cat ? cat.name : ''
})

const windowName = computed(() => {
  if (!dishData.value) return ''
  const win = windows.value.find(w => w.windowId === dishData.value.dish.windowId)
  return win ? win.name : ''
})

const loadDish = async () => {
  const id = route.params.id
  try {
    dishData.value = await dishApi.getDishById(id)
  } catch (error) {
    console.error('加载菜品失败:', error)
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

const increaseQty = () => {
  quantity.value++
}

const decreaseQty = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

const addToCart = () => {
  for (let i = 0; i < quantity.value; i++) {
    cartStore.addItem(dishData.value.dish)
  }
  ElMessage.success(`已添加 ${quantity.value} 份 ${dishData.value.dish.name} 到购物车`)
  quantity.value = 1
}

const goBack = () => {
  router.push('/')
}

onMounted(() => {
  loadDish()
  loadCategories()
  loadWindows()
})
</script>

<style scoped>
.dish-detail-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  background: white;
  padding: 15px 30px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.dish-content {
  padding: 30px;
  max-width: 1000px;
  margin: 0 auto;
}

.dish-main {
  display: flex;
  gap: 30px;
  background: white;
  border-radius: 15px;
  padding: 30px;
  margin-bottom: 20px;
}

.dish-image {
  width: 300px;
  height: 300px;
  border-radius: 15px;
  object-fit: cover;
}

.dish-info {
  flex: 1;
}

.dish-info h1 {
  margin-bottom: 10px;
  color: #333;
}

.price {
  font-size: 32px;
  font-weight: bold;
  color: #e74c3c;
  margin-bottom: 15px;
}

.description {
  color: #666;
  margin-bottom: 15px;
}

.tags {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.nutrition-info, .ingredients {
  margin-bottom: 20px;
}

.nutrition-info h3, .ingredients h3 {
  margin-bottom: 15px;
  color: #333;
}

.nutrition-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
}

.nutrition-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 10px;
}

.icon {
  font-size: 24px;
  margin-bottom: 5px;
}

.label {
  font-size: 12px;
  color: #999;
}

.value {
  font-weight: bold;
  color: #333;
}

.ingredients p {
  color: #666;
  line-height: 1.6;
}

.action-section {
  display: flex;
  justify-content: center;
  gap: 20px;
  align-items: center;
  padding: 30px;
  background: white;
  border-radius: 15px;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 20px;
}

.quantity {
  font-size: 24px;
  font-weight: bold;
  min-width: 40px;
  text-align: center;
}

.add-btn {
  padding: 15px 40px;
  font-size: 18px;
}

.loading {
  text-align: center;
  padding: 100px;
}

.loading-icon {
  font-size: 60px;
  display: block;
  margin-bottom: 20px;
}
</style>