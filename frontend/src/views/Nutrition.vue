<template>
  <div class="nutrition-container">
    <header class="header">
      <div class="header-left">
        <span class="logo">🥗</span>
        <span class="title">营养中心</span>
      </div>
      <div class="header-right">
        <el-button @click="goBack">返回首页</el-button>
      </div>
    </header>

    <main class="nutrition-content">
      <div class="today-section">
        <h2>📊 今日营养摄入</h2>
        <p class="section-hint">（包含已下单和购物车中的菜品）</p>
        <div class="nutrition-cards">
          <div class="nutrition-card" :class="getNutritionStatus('calories')">
            <span class="card-icon">🔥</span>
            <div class="card-content">
              <p class="card-label">热量</p>
              <p class="card-value">{{ totalNutrition.calories || 0 }} <span class="unit">kcal</span></p>
              <div class="progress-bar">
                <div :style="{ width: getCaloriesPercent() + '%' }" :class="getProgressClass('calories')" class="progress-fill"></div>
              </div>
              <p class="progress-text">{{ getCaloriesPercent().toFixed(1) }}% 目标</p>
              <p class="status-text" v-if="getNutritionStatus('calories') !== 'normal'">{{ getStatusText('calories') }}</p>
            </div>
          </div>
          <div class="nutrition-card" :class="getNutritionStatus('protein')">
            <span class="card-icon">💪</span>
            <div class="card-content">
              <p class="card-label">蛋白质</p>
              <p class="card-value">{{ totalNutrition.protein || 0 }} <span class="unit">g</span></p>
              <div class="progress-bar">
                <div :style="{ width: getProteinPercent() + '%' }" :class="getProgressClass('protein')" class="progress-fill"></div>
              </div>
              <p class="progress-text">{{ getProteinPercent().toFixed(1) }}% 目标</p>
              <p class="status-text" v-if="getNutritionStatus('protein') !== 'normal'">{{ getStatusText('protein') }}</p>
            </div>
          </div>
          <div class="nutrition-card" :class="getNutritionStatus('fat')">
            <span class="card-icon">🥑</span>
            <div class="card-content">
              <p class="card-label">脂肪</p>
              <p class="card-value">{{ totalNutrition.fat || 0 }} <span class="unit">g</span></p>
              <div class="progress-bar">
                <div :style="{ width: getFatPercent() + '%' }" :class="getProgressClass('fat')" class="progress-fill"></div>
              </div>
              <p class="progress-text">{{ getFatPercent().toFixed(1) }}% 目标</p>
              <p class="status-text" v-if="getNutritionStatus('fat') !== 'normal'">{{ getStatusText('fat') }}</p>
            </div>
          </div>
          <div class="nutrition-card" :class="getNutritionStatus('carbs')">
            <span class="card-icon">🍞</span>
            <div class="card-content">
              <p class="card-label">碳水</p>
              <p class="card-value">{{ totalNutrition.carbs || 0 }} <span class="unit">g</span></p>
              <div class="progress-bar">
                <div :style="{ width: getCarbsPercent() + '%' }" :class="getProgressClass('carbs')" class="progress-fill"></div>
              </div>
              <p class="progress-text">{{ getCarbsPercent().toFixed(1) }}% 目标</p>
              <p class="status-text" v-if="getNutritionStatus('carbs') !== 'normal'">{{ getStatusText('carbs') }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="meal-section">
        <h2>🎯 AI智能配餐</h2>
        <div class="meal-types">
          <button 
            v-for="meal in mealTypes" 
            :key="meal.type" 
            @click="generateMeal(meal.type)"
            :class="['meal-btn', { active: selectedMealType === meal.type }]"
          >
            <span class="meal-icon">{{ meal.icon }}</span>
            <span class="meal-name">{{ meal.name }}</span>
          </button>
        </div>
        <div v-if="generatedMeals.length > 0" class="meal-result">
          <h3>✨ 为您推荐的{{ getMealTypeName(selectedMealType) }}</h3>
          <div class="meal-dishes">
            <div v-for="dish in generatedMeals" :key="dish.dishId" class="meal-dish-card">
              <img :src="dish.imageUrl || '/images/dishes/default.jpg'" :alt="dish.name" class="dish-image" />
              <div class="dish-info">
                <p class="dish-name">{{ dish.name }}</p>
                <p class="dish-price">¥{{ dish.price }}</p>
                <p class="dish-category">{{ dish.categoryName }}</p>
              </div>
              <el-button size="small" @click="addToCart(dish)">加入购物车</el-button>
            </div>
          </div>
        </div>
      </div>

      <div class="recommend-section">
        <div class="section-header">
          <h2>🤖 AI为您推荐</h2>
          <el-button v-if="recommendations.length > 0" size="small" type="primary" @click="refreshRecommendations" class="refresh-btn">
            🔄 换一换
          </el-button>
        </div>
        <h2>🤖 AI为您推荐</h2>
        
        <!-- 符合忌口 -->
        <div v-if="restrictionDishes.length > 0" class="recommend-group">
          <h3>符合忌口</h3>
          <div class="recommend-dishes">
            <div v-for="dish in restrictionDishes.slice(0, 4)" :key="dish.dishId" class="recommend-dish-card">
              <img :src="dish.imageUrl || '/images/dishes/default.jpg'" :alt="dish.name" class="dish-image" />
              <div class="dish-info">
                <p class="dish-name">{{ dish.name }}</p>
                <p class="dish-price">¥{{ dish.price }}</p>
                <div class="dish-tags">
                  <span v-if="dish.recommendScore" class="tag score">推荐度: {{ dish.recommendScore.toFixed(1) }}</span>
                  <span v-if="dish.nutritionLevel" :class="['tag', dish.nutritionLevel]">{{ dish.nutritionLevel }}</span>
                </div>
              </div>
              <el-button size="small" @click="addToCart(dish)">加入购物车</el-button>
            </div>
          </div>
        </div>
        
        <div v-if="recommendations.length > 0" class="recommend-list">
          <div v-for="(group, index) in recommendations" :key="index" class="recommend-group">
            <h3>{{ group.title }}</h3>
            <div v-if="group.type !== 'restriction'" class="recommend-dishes">
              <div v-for="dish in group.dishes" :key="dish.dishId" class="recommend-dish-card">
                <img :src="dish.imageUrl || '/images/dishes/default.jpg'" :alt="dish.name" class="dish-image" />
                <div class="dish-info">
                  <p class="dish-name">{{ dish.name }}</p>
                  <p class="dish-price">¥{{ dish.price }}</p>
                  <div class="dish-tags">
                    <span v-if="dish.recommendScore" class="tag score">推荐度: {{ dish.recommendScore.toFixed(1) }}</span>
                    <span v-if="dish.nutritionLevel" :class="['tag', dish.nutritionLevel]">{{ dish.nutritionLevel }}</span>
                  </div>
                </div>
                <el-button size="small" @click="addToCart(dish)">加入购物车</el-button>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="empty-recommend">
          <span class="empty-icon">🤔</span>
          <p>暂无推荐</p>
          <p class="empty-hint">多下单几次，AI会了解您的口味</p>
        </div>
      </div>

      <div class="pie-section">
        <h2>🥧 营养占比</h2>
        <div class="pie-chart-container">
          <div ref="pieChartRef" class="pie-chart"></div>
          <div v-if="pieChartEmpty" class="empty-pie">
            <span class="empty-icon">📊</span>
            <p>暂无营养数据</p>
            <p class="empty-hint">请先选购菜品</p>
          </div>
        </div>
      </div>

      <div class="chart-section">
        <h2>📈 营养趋势</h2>
        <div class="chart-container">
          <div ref="chartRef" class="chart"></div>
          <div v-if="historyRecords.length === 0" class="empty-chart">
            <span class="empty-icon">📈</span>
            <p>暂无历史数据</p>
          </div>
        </div>
      </div>

      <div class="evaluation-section">
        <h2>👩⚕️ AI健康评价</h2>
        <div class="evaluation-card" v-if="evaluation && totalNutrition.calories > 0">
          <div class="evaluation-content">
            <span class="evaluation-icon">{{ getEvaluationIcon(evaluation.score) }}</span>
            <div class="evaluation-text">
              <p class="evaluation-title">{{ getEvaluationTitle(evaluation.score) }}</p>
              <p class="evaluation-desc">{{ evaluation.advice }}</p>
            </div>
          </div>
          <div class="evaluation-score">
            <span>{{ evaluation.score }}分</span>
          </div>
        </div>
        <div class="evaluation-details" v-if="evaluation && totalNutrition.calories > 0">
          <div class="evaluation-tags">
            <div class="tag-group positives" v-if="evaluation.positives && evaluation.positives.length > 0">
              <h4>✨ 做得好</h4>
              <div class="tags">
                <span v-for="(item, index) in evaluation.positives" :key="index" class="tag positive">{{ item }}</span>
              </div>
            </div>
            <div class="tag-group issues" v-if="evaluation.issues && evaluation.issues.length > 0">
              <h4>⚠️ 需要注意</h4>
              <div class="tags">
                <span v-for="(item, index) in evaluation.issues" :key="index" class="tag issue">{{ item }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="empty-evaluation" v-else>
          <span class="empty-icon">💡</span>
          <p>暂无评价数据</p>
          <p class="empty-hint">选购菜品后即可获得AI健康评价</p>
        </div>
      </div>

      <div class="history-section">
        <h2>📜 历史饮食记录</h2>
        <div class="history-list" v-if="historyRecords.length > 0">
          <div class="history-item" v-for="record in historyRecords" :key="record.date">
            <div class="history-date">{{ record.date }}</div>
            <div class="history-nutrition">
              <span>🔥 {{ record.calories }}kcal</span>
              <span>💪 {{ record.protein }}g</span>
              <span>🥑 {{ record.fat }}g</span>
              <span>🍞 {{ record.carbs }}g</span>
            </div>
          </div>
        </div>
        <div class="empty-history" v-else>
          <p>暂无历史记录</p>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore, useCartStore } from '@/stores'
import { nutritionApi, aiApi, dishApi } from '@/api'
import { restrictionApi } from '@/api'
import * as echarts from 'echarts'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()

const todayNutrition = ref({
  calories: 0,
  protein: 0,
  fat: 0,
  carbs: 0
})

const evaluation = ref(null)
const historyRecords = ref([])
const chartRef = ref(null)
const pieChartRef = ref(null)

const recommendations = ref([])
const generatedMeals = ref([])
const selectedMealType = ref('')
const restrictionDishes = ref([])
const mealTypes = [
  { type: 'low-calorie', name: '减脂餐', icon: '🥗' },
  { type: 'high-protein', name: '增肌餐', icon: '💪' },
  { type: 'low-carb', name: '低糖餐', icon: '🍃' },
  { type: 'gentle', name: '养胃餐', icon: '🥣' },
  { type: 'light', name: '清淡餐', icon: '🌿' }
]

const getMealTypeName = (type) => {
  const meal = mealTypes.find(m => m.type === type)
  return meal ? meal.name : type
}

const pieChartEmpty = computed(() => {
  return totalNutrition.value.calories === 0 && 
         totalNutrition.value.protein === 0 && 
         totalNutrition.value.fat === 0 && 
         totalNutrition.value.carbs === 0
})

const cartNutrition = computed(() => {
  return cartStore.items.reduce((sum, item) => ({
    calories: sum.calories + (item.calories || 0) * item.quantity,
    protein: sum.protein + (item.protein || 0) * item.quantity,
    fat: sum.fat + (item.fat || 0) * item.quantity,
    carbs: sum.carbs + (item.carbs || 0) * item.quantity
  }), { calories: 0, protein: 0, fat: 0, carbs: 0 })
})

const totalNutrition = computed(() => ({
  calories: Math.round(todayNutrition.value.calories + cartNutrition.value.calories),
  protein: Math.round(todayNutrition.value.protein + cartNutrition.value.protein),
  fat: Math.round(todayNutrition.value.fat + cartNutrition.value.fat),
  carbs: Math.round(todayNutrition.value.carbs + cartNutrition.value.carbs)
}))

const nutritionTargets = ref({
  calories: 2000,
  protein: 80,
  fat: 60,
  carbs: 250
})

const targetCalories = computed(() => nutritionTargets.value.calories)
const targetProtein = computed(() => nutritionTargets.value.protein)
const targetFat = computed(() => nutritionTargets.value.fat)
const targetCarbs = computed(() => nutritionTargets.value.carbs)

const getCaloriesPercent = () => {
  if (totalNutrition.value.calories === 0) return 0
  return Math.min((totalNutrition.value.calories / targetCalories.value) * 100, 100)
}

const getProteinPercent = () => {
  if (totalNutrition.value.protein === 0) return 0
  return Math.min((totalNutrition.value.protein / targetProtein.value) * 100, 100)
}

const getFatPercent = () => {
  if (totalNutrition.value.fat === 0) return 0
  return Math.min((totalNutrition.value.fat / targetFat.value) * 100, 100)
}

const getCarbsPercent = () => {
  if (totalNutrition.value.carbs === 0) return 0
  return Math.min((totalNutrition.value.carbs / targetCarbs.value) * 100, 100)
}

const getNutritionStatus = (type) => {
  const percent = type === 'calories' ? getCaloriesPercent() :
                  type === 'protein' ? getProteinPercent() :
                  type === 'fat' ? getFatPercent() : getCarbsPercent()
  if (percent === 0) return 'empty'
  if (percent >= 120) return 'danger'
  if (percent >= 80) return 'normal'
  return 'warning'
}

const getProgressClass = (type) => {
  const status = getNutritionStatus(type)
  if (status === 'danger') return 'danger'
  if (status === 'warning') return 'warning'
  if (status === 'empty') return 'empty'
  return type
}

const getStatusText = (type) => {
  const percent = type === 'calories' ? getCaloriesPercent() :
                  type === 'protein' ? getProteinPercent() :
                  type === 'fat' ? getFatPercent() : getCarbsPercent()
  if (percent === 0) return '暂无数据'
  if (percent >= 120) return '摄入超标'
  if (percent < 80) return '摄入不足'
  return ''
}

const getEvaluationIcon = (score) => {
  if (score >= 90) return '🏆'
  if (score >= 70) return '👍'
  if (score >= 50) return '💪'
  return '⚠️'
}

const getEvaluationTitle = (score) => {
  if (score >= 90) return '优秀！继续保持'
  if (score >= 70) return '良好！再接再厉'
  if (score >= 50) return '一般，还需努力'
  return '较差，请调整饮食'
}

const loadNutritionTargets = async () => {
  if (!userStore.user) return
  try {
    const data = await nutritionApi.getNutritionTargets(userStore.user.userId)
    nutritionTargets.value = {
      calories: Number(data.calories) || 2000,
      protein: Number(data.protein) || 80,
      fat: Number(data.fat) || 60,
      carbs: Number(data.carbs) || 250
    }
  } catch (error) {
    console.error('加载营养目标失败:', error)
  }
}

const loadTodayNutrition = async () => {
  if (!userStore.user) return
  try {
    const data = await nutritionApi.getTodayNutrition(userStore.user.userId)
    todayNutrition.value = {
      calories: Number(data.calories) || 0,
      protein: Number(data.protein) || 0,
      fat: Number(data.fat) || 0,
      carbs: Number(data.carbs) || 0
    }
  } catch (error) {
    console.error('加载今日营养失败:', error)
  }
}

const loadEvaluation = async () => {
  if (!userStore.user || totalNutrition.value.calories === 0) {
    evaluation.value = null
    return
  }
  try {
    evaluation.value = await nutritionApi.getHealthEvaluation(userStore.user.userId)
  } catch (error) {
    console.error('加载健康评价失败:', error)
    evaluation.value = null
  }
}

const loadRecommendations = async () => {
  if (!userStore.user) return
  try {
    const data = await aiApi.recommendDishes(userStore.user.userId)
    console.log('AI推荐数据:', data)
    // 检查每个菜品的 imageUrl
    data.forEach((group, index) => {
      console.log(`推荐组 ${index + 1}: ${group.title}`)
      if (group.dishes && group.dishes.length > 0) {
        group.dishes.forEach((dish, idx) => {
          console.log(`  菜品 ${idx + 1}: name=${dish.name}, dishId=${dish.dishId}, imageUrl=${dish.imageUrl}`)
        })
      }
    })
    recommendations.value = data
  } catch (error) {
    console.error('加载AI推荐失败:', error)
    recommendations.value = []
  }
}

const refreshRecommendations = async () => {
  await loadRecommendations()
}

const loadRestrictionDishes = async () => {
  if (!userStore.user) return
  try {
    const res = await restrictionApi.getRecommendedDishes(userStore.user.userId)
    restrictionDishes.value = res || []
  } catch (error) {
    console.error('加载符合忌口菜品失败:', error)
    restrictionDishes.value = []
  }
}

const generateMeal = async (type) => {
  if (!userStore.user) {
    console.log('用户未登录，无法生成智能配餐')
    return
  }
  console.log(`生成智能配餐: ${type}, 用户ID: ${userStore.user.userId}`)
  selectedMealType.value = type
  try {
    generatedMeals.value = await aiApi.intelligentMeal(userStore.user.userId, type)
    console.log('智能配餐结果:', generatedMeals.value)
  } catch (error) {
    console.error('生成智能配餐失败:', error)
    generatedMeals.value = []
  }
}

const enrichDishNutrition = async (dish) => {
  if (dish.calories != null && Number(dish.calories) > 0) {
    return dish
  }
  // 检查 dishId 是否存在
  if (!dish.dishId) {
    console.warn('菜品ID为空，跳过获取营养信息:', dish)
    return dish
  }
  try {
    const detail = await dishApi.getDishById(dish.dishId)
    const nutrition = detail.nutrition || {}
    return {
      ...dish,
      calories: Number(nutrition.calories) || 0,
      protein: Number(nutrition.protein) || 0,
      fat: Number(nutrition.fat) || 0,
      carbs: Number(nutrition.carbs) || 0
    }
  } catch (error) {
    console.error('加载菜品营养失败:', error)
    return dish
  }
}

const addToCart = async (dish) => {
  if (!userStore.user) {
    router.push('/login')
    return
  }
  try {
    const dishWithNutrition = await enrichDishNutrition(dish)
    cartStore.addItem(dishWithNutrition)
    cartStore.persistCart(userStore.user.userId)
    refreshData()
    alert('已加入购物车')
  } catch (error) {
    console.error('加入购物车失败:', error)
  }
}

const loadHistory = async () => {
  if (!userStore.user) return
  try {
    historyRecords.value = await nutritionApi.getHistoryNutrition(userStore.user.userId, 7)
  } catch (error) {
    console.error('加载历史记录失败:', error)
  }
}

const initChart = () => {
  if (!chartRef.value || historyRecords.value.length === 0) return
  
  const chart = echarts.init(chartRef.value)
  const dates = historyRecords.value.map(r => r.date)
  const calories = historyRecords.value.map(r => Number(r.calories) || 0)
  const protein = historyRecords.value.map(r => Number(r.protein) || 0)
  const fat = historyRecords.value.map(r => Number(r.fat) || 0)
  const carbs = historyRecords.value.map(r => Number(r.carbs) || 0)
  
  const option = {
    tooltip: { trigger: 'axis' },
    legend: { data: ['热量', '蛋白质', '脂肪', '碳水'] },
    xAxis: { type: 'category', data: dates },
    yAxis: [{ type: 'value', name: 'kcal/g' }],
    series: [
      { name: '热量', type: 'line', data: calories, smooth: true },
      { name: '蛋白质', type: 'line', data: protein, smooth: true },
      { name: '脂肪', type: 'line', data: fat, smooth: true },
      { name: '碳水', type: 'line', data: carbs, smooth: true }
    ]
  }
  
  chart.setOption(option)
  
  window.addEventListener('resize', () => chart.resize())
}

const initPieChart = () => {
  if (!pieChartRef.value) return
  
  const calories = Number(totalNutrition.value.calories) || 0
  const protein = Number(totalNutrition.value.protein) || 0
  const fat = Number(totalNutrition.value.fat) || 0
  const carbs = Number(totalNutrition.value.carbs) || 0

  const proteinCal = protein * 4
  const fatCal = fat * 9
  const carbsCal = carbs * 4
  const total = proteinCal + fatCal + carbsCal

  if (total === 0) return

  const chart = echarts.init(pieChartRef.value)

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c}kcal ({d}%)'
    },
    legend: {
      orient: 'horizontal',
      bottom: '5%'
    },
    series: [
      {
        name: '营养占比',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}\n{d}%'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        data: [
          { value: proteinCal, name: '蛋白质', itemStyle: { color: '#4ade80' } },
          { value: fatCal, name: '脂肪', itemStyle: { color: '#fbbf24' } },
          { value: carbsCal, name: '碳水', itemStyle: { color: '#60a5fa' } }
        ]
      }
    ]
  }
  
  chart.setOption(option)
  
  window.addEventListener('resize', () => chart.resize())
}

const goBack = () => {
  router.push('/')
}

const refreshData = () => {
  loadTodayNutrition()
  loadEvaluation()
  setTimeout(() => {
    initPieChart()
  }, 100)
}

watch(() => cartStore.items, () => {
  refreshData()
}, { deep: true })

onMounted(async () => {
  if (userStore.user) {
    cartStore.loadCart(userStore.user.userId)
  }
  await loadNutritionTargets()
  await loadTodayNutrition()
  await loadEvaluation()
  await loadHistory()
  await loadRecommendations()
  await loadRestrictionDishes()
  setTimeout(() => {
    initChart()
    initPieChart()
  }, 100)
})
</script>

<style scoped>
.nutrition-container {
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

.header-right {
  display: flex;
  gap: 10px;
}

.nutrition-content {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.section-hint {
  font-size: 14px;
  color: #666;
  margin-top: -10px;
  margin-bottom: 15px;
}

.today-section, .meal-section, .recommend-section, .pie-section, .chart-section, .evaluation-section, .history-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.today-section h2, .meal-section h2, .recommend-section h2, .pie-section h2, .chart-section h2, .evaluation-section h2, .history-section h2 {
  margin: 0 0 15px 0;
  font-size: 18px;
  color: #333;
}

.meal-types {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.meal-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
  padding: 15px 20px;
  background: #f8f9fa;
  border: 2px solid transparent;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 100px;
}

.meal-btn:hover {
  background: #e9ecef;
  border-color: #667eea;
}

.meal-btn.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: #667eea;
  color: white;
}

.meal-icon {
  font-size: 28px;
}

.meal-name {
  font-size: 14px;
  font-weight: 500;
}

.meal-result h3 {
  font-size: 16px;
  color: #333;
  margin: 0 0 15px 0;
}

.meal-dishes, .recommend-dishes {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 15px;
}

.meal-dish-card, .recommend-dish-card {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 12px;
  background: #f9fafb;
  border-radius: 10px;
  transition: box-shadow 0.3s ease;
}

.meal-dish-card:hover, .recommend-dish-card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.dish-image {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  object-fit: cover;
}

.dish-info {
  flex: 1;
}

.dish-info .dish-name {
  font-weight: 600;
  color: #333;
  margin: 0 0 5px 0;
}

.dish-info .dish-price {
  color: #667eea;
  font-weight: 600;
  margin: 0 0 5px 0;
}

.dish-info .dish-category {
  font-size: 12px;
  color: #999;
  margin: 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.section-header h2 {
  margin: 0;
}

.refresh-btn {
  font-size: 14px;
  padding: 6px 16px;
}

.recommend-group h3 {
  font-size: 15px;
  color: #666;
  margin: 0 0 12px 0;
  padding-left: 5px;
  border-left: 3px solid #667eea;
}

.empty-recommend {
  text-align: center;
  padding: 40px;
  color: #999;
}

.nutrition-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
}

.nutrition-card {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 12px;
  padding: 15px;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.nutrition-card.empty {
  background: #fafafa;
}

.nutrition-card.warning {
  background: linear-gradient(135deg, #fff7ed 0%, #ffedd5 100%);
  border: 1px solid #fed7aa;
}

.nutrition-card.danger {
  background: linear-gradient(135deg, #fef2f2 0%, #fee2e2 100%);
  border: 1px solid #fecaca;
}

.nutrition-card.normal {
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%);
  border: 1px solid #bbf7d0;
}

.card-icon {
  font-size: 28px;
}

.card-content {
  margin-top: 10px;
}

.card-label {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 5px 0;
}

.card-value .unit {
  font-size: 14px;
  font-weight: normal;
  color: #999;
}

.progress-bar {
  height: 6px;
  background: #e5e7eb;
  border-radius: 3px;
  margin: 10px 0;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 3px;
  transition: width 0.5s ease;
}

.progress-fill.calories {
  background: linear-gradient(90deg, #f87171, #ef4444);
}

.progress-fill.protein {
  background: linear-gradient(90deg, #4ade80, #22c55e);
}

.progress-fill.fat {
  background: linear-gradient(90deg, #fbbf24, #f59e0b);
}

.progress-fill.carbs {
  background: linear-gradient(90deg, #60a5fa, #3b82f6);
}

.progress-fill.warning {
  background: linear-gradient(90deg, #fbbf24, #f59e0b);
}

.progress-fill.danger {
  background: linear-gradient(90deg, #f87171, #ef4444);
}

.progress-fill.empty {
  background: #e5e7eb;
}

.progress-text {
  font-size: 12px;
  color: #999;
  margin: 0;
}

.status-text {
  font-size: 12px;
  margin: 5px 0 0 0;
  font-weight: bold;
}

.nutrition-card.warning .status-text {
  color: #d97706;
}

.nutrition-card.danger .status-text {
  color: #dc2626;
}

.nutrition-card.empty .status-text {
  color: #9ca3af;
}

.pie-chart-container, .chart-container {
  position: relative;
  height: 300px;
}

.pie-chart, .chart {
  width: 100%;
  height: 100%;
}

.empty-pie, .empty-chart, .empty-evaluation, .empty-history {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  color: #999;
}

.empty-icon {
  font-size: 48px;
  display: block;
  margin-bottom: 10px;
}

.empty-hint {
  font-size: 14px;
  color: #bbb;
  margin-top: 5px;
}

.evaluation-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: white;
}

.evaluation-content {
  display: flex;
  align-items: center;
  gap: 15px;
}

.evaluation-icon {
  font-size: 40px;
}

.evaluation-text {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.evaluation-title {
  font-size: 18px;
  font-weight: bold;
  margin: 0;
}

.evaluation-desc {
  font-size: 14px;
  margin: 0;
  opacity: 0.9;
}

.evaluation-score {
  font-size: 36px;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
}

.evaluation-details {
  margin-top: 15px;
}

.evaluation-tags {
  display: flex;
  gap: 20px;
}

.tag-group {
  flex: 1;
}

.tag-group h4 {
  font-size: 14px;
  color: #333;
  margin: 0 0 10px 0;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  padding: 5px 12px;
  border-radius: 20px;
  font-size: 13px;
}

.tag.positive {
  background: #dcfce7;
  color: #166534;
}

.tag.issue {
  background: #fee2e2;
  color: #991b1b;
}

.tag.score {
  background: #eaf5fb;
  color: #1e40af;
}

.tag.优秀 {
  background: #dcfce7;
  color: #166534;
}

.tag.良好 {
  background: #fef9c3;
  color: #854d0e;
}

.tag.一般 {
  background: #f3f4f6;
  color: #6b7280;
}

.tag.不合适 {
  background: #fee2e2;
  color: #991b1b;
}

.dish-meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #666;
  margin-top: 5px;
}

.meta-item {
  background: #f5f7fa;
  padding: 2px 8px;
  border-radius: 4px;
}

.dish-tags {
  display: flex;
  gap: 8px;
  margin-top: 8px;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 15px;
  background: #f9fafb;
  border-radius: 8px;
}

.history-date {
  font-weight: bold;
  color: #333;
}

.history-nutrition {
  display: flex;
  gap: 20px;
  font-size: 14px;
}

.history-nutrition span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.empty-evaluation, .empty-history {
  padding: 40px;
}

@media (max-width: 768px) {
  .nutrition-cards {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .evaluation-card {
    flex-direction: column;
    text-align: center;
    gap: 15px;
  }
  
  .evaluation-tags {
    flex-direction: column;
  }
}
</style>
