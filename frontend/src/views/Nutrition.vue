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
        <div class="nutrition-cards">
          <div class="nutrition-card">
            <span class="card-icon">🔥</span>
            <div class="card-content">
              <p class="card-label">热量</p>
              <p class="card-value">{{ todayNutrition.calories || 0 }} <span class="unit">kcal</span></p>
              <div class="progress-bar">
                <div :style="{ width: getCaloriesPercent() + '%' }" class="progress-fill"></div>
              </div>
              <p class="progress-text">{{ getCaloriesPercent().toFixed(1) }}% 目标</p>
            </div>
          </div>
          <div class="nutrition-card">
            <span class="card-icon">💪</span>
            <div class="card-content">
              <p class="card-label">蛋白质</p>
              <p class="card-value">{{ todayNutrition.protein || 0 }} <span class="unit">g</span></p>
              <div class="progress-bar">
                <div :style="{ width: getProteinPercent() + '%' }" class="progress-fill protein"></div>
              </div>
              <p class="progress-text">{{ getProteinPercent().toFixed(1) }}% 目标</p>
            </div>
          </div>
          <div class="nutrition-card">
            <span class="card-icon">🥑</span>
            <div class="card-content">
              <p class="card-label">脂肪</p>
              <p class="card-value">{{ todayNutrition.fat || 0 }} <span class="unit">g</span></p>
              <div class="progress-bar">
                <div :style="{ width: getFatPercent() + '%' }" class="progress-fill fat"></div>
              </div>
              <p class="progress-text">{{ getFatPercent().toFixed(1) }}% 目标</p>
            </div>
          </div>
          <div class="nutrition-card">
            <span class="card-icon">🍞</span>
            <div class="card-content">
              <p class="card-label">碳水</p>
              <p class="card-value">{{ todayNutrition.carbs || 0 }} <span class="unit">g</span></p>
              <div class="progress-bar">
                <div :style="{ width: getCarbsPercent() + '%' }" class="progress-fill carbs"></div>
              </div>
              <p class="progress-text">{{ getCarbsPercent().toFixed(1) }}% 目标</p>
            </div>
          </div>
        </div>
      </div>

      <div class="chart-section">
        <h2>📈 营养趋势</h2>
        <div class="chart-container">
          <div ref="chartRef" class="chart"></div>
        </div>
      </div>

      <div class="evaluation-section">
        <h2>👩⚕️ AI健康评价</h2>
        <div class="evaluation-card" v-if="evaluation">
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
        <div class="empty-evaluation" v-else>
          <p>暂无评价数据</p>
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
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores'
import { nutritionApi } from '@/api'
import * as echarts from 'echarts'

const router = useRouter()
const userStore = useUserStore()

const todayNutrition = ref({
  calories: 0,
  protein: 0,
  fat: 0,
  carbs: 0
})

const evaluation = ref(null)
const historyRecords = ref([])
const chartRef = ref(null)

const targetCalories = computed(() => {
  const goal = userStore.user?.dietGoal || 0
  switch (goal) {
    case 1: return 1500  // 减脂
    case 2: return 2500  // 增肌
    case 3: return 1800  // 养胃
    default: return 2000 // 正常
  }
})

const targetProtein = computed(() => 80)
const targetFat = computed(() => 60)
const targetCarbs = computed(() => 250)

const getCaloriesPercent = () => {
  return Math.min((todayNutrition.value.calories / targetCalories.value) * 100, 100)
}

const getProteinPercent = () => {
  return Math.min((todayNutrition.value.protein / targetProtein.value) * 100, 100)
}

const getFatPercent = () => {
  return Math.min((todayNutrition.value.fat / targetFat.value) * 100, 100)
}

const getCarbsPercent = () => {
  return Math.min((todayNutrition.value.carbs / targetCarbs.value) * 100, 100)
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

const loadTodayNutrition = async () => {
  if (!userStore.user) return
  try {
    todayNutrition.value = await nutritionApi.getTodayNutrition(userStore.user.userId)
  } catch (error) {
    console.error('加载今日营养失败:', error)
  }
}

const loadEvaluation = async () => {
  if (!userStore.user) return
  try {
    evaluation.value = await nutritionApi.getHealthEvaluation(userStore.user.userId)
  } catch (error) {
    console.error('加载健康评价失败:', error)
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
  const calories = historyRecords.value.map(r => r.calories)
  const protein = historyRecords.value.map(r => r.protein)
  
  const option = {
    tooltip: { trigger: 'axis' },
    legend: { data: ['热量', '蛋白质'] },
    xAxis: { type: 'category', data: dates },
    yAxis: [{ type: 'value', name: 'kcal' }, { type: 'value', name: 'g' }],
    series: [
      { name: '热量', type: 'line', data: calories },
      { name: '蛋白质', type: 'line', yAxisIndex: 1, data: protein }
    ]
  }
  
  chart.setOption(option)
  
  window.addEventListener('resize', () => chart.resize())
}

const goBack = () => {
  router.push('/')
}

onMounted(async () => {
  await loadTodayNutrition()
  await loadEvaluation()
  await loadHistory()
  setTimeout(initChart, 100)
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

.nutrition-content {
  padding: 30px;
  max-width: 1000px;
  margin: 0 auto;
}

.today-section, .chart-section, .evaluation-section, .history-section {
  background: white;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
}

.today-section h2, .chart-section h2, .evaluation-section h2, .history-section h2 {
  margin-bottom: 20px;
  color: #333;
}

.nutrition-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.nutrition-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 10px;
  padding: 20px;
  color: white;
}

.card-icon {
  font-size: 30px;
  display: block;
  margin-bottom: 10px;
}

.card-label {
  font-size: 14px;
  opacity: 0.8;
  margin-bottom: 5px;
}

.card-value {
  font-size: 28px;
  font-weight: bold;
}

.unit {
  font-size: 14px;
  font-weight: normal;
}

.progress-bar {
  height: 8px;
  background: rgba(255,255,255,0.3);
  border-radius: 4px;
  margin: 10px 0;
}

.progress-fill {
  height: 100%;
  background: white;
  border-radius: 4px;
  transition: width 0.5s;
}

.progress-fill.protein { background: #4ade80; }
.progress-fill.fat { background: #fbbf24; }
.progress-fill.carbs { background: #f87171; }

.progress-text {
  font-size: 12px;
  opacity: 0.8;
}

.chart-container {
  height: 300px;
}

.chart {
  width: 100%;
  height: 100%;
}

.evaluation-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30px;
  background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
  border-radius: 10px;
}

.evaluation-content {
  display: flex;
  gap: 20px;
}

.evaluation-icon {
  font-size: 50px;
}

.evaluation-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.evaluation-desc {
  color: #666;
}

.evaluation-score span {
  font-size: 48px;
  font-weight: bold;
  color: #333;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.history-item {
  display: flex;
  justify-content: space-between;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
}

.history-date {
  font-weight: bold;
  color: #333;
}

.history-nutrition {
  display: flex;
  gap: 20px;
}

.empty-evaluation, .empty-history {
  text-align: center;
  padding: 40px;
  color: #999;
}
</style>