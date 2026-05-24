<template>
  <div class="home-container">
    <header class="header">
      <div class="header-left">
        <span class="logo">🍽️</span>
        <span class="title">校园AI食堂</span>
      </div>
      <div class="header-center">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索菜品..."
          class="search-input"
          @keyup.enter="handleSearch"
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
      </div>
      <div class="header-right">
        <el-button @click="goToCart" class="cart-btn">
          <img src="/images/dishes/购物车.svg" alt="购物车" class="cart-icon" />
          <span v-if="cartStore.totalCount > 0" class="cart-badge">{{ cartStore.totalCount }}</span>
        </el-button>
        <el-dropdown>
          <span class="user-info">
            <User />
            <span>{{ userStore.user?.name || '用户' }}</span>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="goToNutrition">营养中心</el-dropdown-item>
              <el-dropdown-item @click="goToOrders">我的订单</el-dropdown-item>
              <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>

    <div class="main-content">
      <aside class="sidebar">
        <div class="filter-status" v-if="activeCategory > 0 || activeWindow > 0 || searchKeyword">
          <h3>当前筛选</h3>
          <div class="filter-tags">
            <el-tag v-if="activeCategory > 0" closable @close="selectCategory(0)" type="primary">
              {{ categories.find(c => c.categoryId === activeCategory)?.name }}
            </el-tag>
            <el-tag v-if="activeWindow > 0" closable @close="selectWindow(0)" type="success">
              {{ windows.find(w => w.windowId === activeWindow)?.name }}
            </el-tag>
            <el-tag v-if="searchKeyword" closable @close="searchKeyword = ''; loadDishes()" type="warning">
              搜索: {{ searchKeyword }}
            </el-tag>
          </div>
          <el-button type="text" @click="resetFilters" style="margin-top: 10px;">重置全部</el-button>
        </div>

        <div class="category-section">
          <h3>菜品分类</h3>
          <el-menu :default-active="activeCategory" class="category-menu">
            <el-menu-item index="0" @click="selectCategory(0)">全部菜品</el-menu-item>
            <el-menu-item v-for="cat in categories" :key="cat.categoryId" :index="cat.categoryId.toString()" @click="selectCategory(cat.categoryId)">
              {{ cat.name }}
            </el-menu-item>
          </el-menu>
        </div>
        
        <div class="window-section">
          <h3>选择窗口</h3>
          <el-menu :default-active="activeWindow" class="window-menu">
            <el-menu-item index="0" @click="selectWindow(0)">全部窗口</el-menu-item>
            <el-menu-item v-for="win in windows" :key="win.windowId" :index="win.windowId.toString()" @click="selectWindow(win.windowId)">
              {{ win.name }}
            </el-menu-item>
          </el-menu>
        </div>
      </aside>

      <main class="content">
        <div class="ai-recommend" v-if="recommendations.length > 0">
          <div class="section-header">
            <h2>🤖 AI为您推荐</h2>
            <el-button type="text" @click="refreshRecommend">换一批</el-button>
          </div>
          <div class="dish-grid recommend-grid">
            <div class="dish-card" v-for="dish in recommendations" :key="dish.dishId" @click="goToDetail(dish.dishId)">
              <img v-if="dish.imageUrl" :src="dish.imageUrl" alt="菜品图片" class="dish-image" />
              <div v-else class="dish-emoji"><span>{{ getDishEmoji(dish.name) }}</span></div>
              <div class="dish-info">
                <h4>{{ dish.name }}</h4>
                <p class="price">¥{{ dish.price }}</p>
                <p class="nutrition">热量: {{ dish.calories }}kcal</p>
              </div>
              <el-button type="primary" size="small" @click.stop="addToCart(dish)">加入购物车</el-button>
            </div>
          </div>
        </div>

        <div class="section-header">
          <h2>🍳 全部菜品</h2>
        </div>
        <div class="dish-grid">
          <div class="dish-card" v-for="dish in dishes" :key="dish.dishId" @click="goToDetail(dish.dishId)">
            <img v-if="dish.imageUrl" :src="dish.imageUrl" alt="菜品图片" class="dish-image" />
            <div v-else class="dish-emoji"><span>{{ getDishEmoji(dish.name) }}</span></div>
            <div class="dish-info">
              <h4>{{ dish.name }}</h4>
              <p class="price">¥{{ dish.price }}</p>
              <p class="nutrition">热量: {{ dish.calories }}kcal | 蛋白: {{ dish.protein }}g</p>
            </div>
            <el-button type="primary" size="small" @click.stop="addToCart(dish)">加入购物车</el-button>
          </div>
        </div>

        <div v-if="dishes.length === 0" class="empty-state">
          <span class="empty-icon">🍽️</span>
          <p>暂无菜品</p>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ShoppingCart, User } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useUserStore, useCartStore } from '@/stores'
import { dishApi, aiApi } from '@/api'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()

const searchKeyword = ref('')
const activeCategory = ref(0)
const activeWindow = ref(0)
const categories = ref([])
const windows = ref([])
const dishes = ref([])
const recommendations = ref([])

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

const loadDishes = async () => {
  try {
    const params = {
      categoryId: activeCategory.value > 0 ? activeCategory.value : undefined,
      windowId: activeWindow.value > 0 ? activeWindow.value : undefined,
      keyword: searchKeyword.value || undefined
    }
    const result = await dishApi.getDishes(params)
    if (result.records) {
      dishes.value = result.records
    } else if (Array.isArray(result)) {
      dishes.value = result
    } else {
      dishes.value = []
    }
  } catch (error) {
    console.error('加载菜品失败:', error)
  }
}

const loadRecommendations = async () => {
  if (!userStore.user) return
  try {
    recommendations.value = await aiApi.recommendDishes(userStore.user.userId)
  } catch (error) {
    console.error('加载推荐失败:', error)
  }
}

const selectCategory = (id) => {
  activeCategory.value = id
  loadDishes()
}

const selectWindow = (id) => {
  activeWindow.value = id
  loadDishes()
}

const resetFilters = () => {
  activeCategory.value = 0
  activeWindow.value = 0
  searchKeyword.value = ''
  loadDishes()
}

const handleSearch = () => {
  loadDishes()
}

const refreshRecommend = () => {
  loadRecommendations()
}

const getDishEmoji = (name) => {
  const emojiMap = {
    '宫保鸡丁': '🍗', '鱼香肉丝': '🥩', '红烧肉': '🥓', '糖醋里脊': '🍖',
    '清炒西兰花': '🥦', '蒜蓉菠菜': '🥬', '麻婆豆腐': '🧈', '番茄炒蛋': '🍳',
    '酸辣汤': '🍲', '排骨汤': '🦴', '米饭': '🍚', '馒头': '🥟',
    '油条': '🥖', '包子': '🥮', '饺子': '🥟', '面条': '🍝',
    '粥': '🥣', '豆浆': '🥛', '鸡蛋': '🥚', '水果': '🍎'
  }
  return emojiMap[name] || '🍽️'
}

const goToDetail = (id) => {
  router.push(`/dish-detail/${id}`)
}

const goToCart = () => {
  router.push('/cart')
}

const goToNutrition = () => {
  router.push('/nutrition')
}

const goToOrders = () => {
  router.push('/orders')
}

const addToCart = (dish) => {
  cartStore.addItem(dish)
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

onMounted(() => {
  loadCategories()
  loadWindows()
  loadDishes()
  loadRecommendations()
})
</script>

<style scoped>
.home-container {
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

.header-center {
  display: flex;
  gap: 10px;
}

.search-input {
  width: 300px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.cart-btn {
  position: relative;
  padding: 8px;
}

.cart-icon {
  width: 24px;
  height: 24px;
}

.cart-badge {
  position: absolute;
  top: -10px;
  right: -10px;
  background: red;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  font-size: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
}

.main-content {
  display: flex;
  padding: 20px;
}

.sidebar {
  width: 200px;
  background: white;
  border-radius: 10px;
  padding: 20px;
  margin-right: 20px;
}

.category-section, .window-section {
  margin-bottom: 20px;
}

.category-section h3, .window-section h3 {
  margin-bottom: 10px;
  color: #333;
}

.category-menu, .window-menu {
  border: none;
}

.content {
  flex: 1;
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

.dish-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.recommend-grid {
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
}

.dish-card {
  background: white;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: transform 0.3s;
}

.dish-card:hover {
  transform: translateY(-5px);
}

.dish-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.dish-emoji {
  width: 100%;
  height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-size: 64px;
}

.dish-info {
  padding: 15px;
}

.dish-info h4 {
  margin-bottom: 5px;
  color: #333;
}

.price {
  color: #e74c3c;
  font-weight: bold;
  margin-bottom: 5px;
}

.nutrition {
  font-size: 12px;
  color: #999;
}

.dish-card el-button {
  width: calc(100% - 30px);
  margin: 0 15px 15px;
}

.empty-state {
  text-align: center;
  padding: 50px;
}

.empty-icon {
  font-size: 60px;
  display: block;
  margin-bottom: 20px;
}
</style>