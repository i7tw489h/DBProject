<template>
  <div class="cart-container">
    <header class="header">
      <div class="header-left">
        <span class="logo">🛒</span>
        <span class="title">购物车</span>
      </div>
      <div class="header-right">
        <el-button @click="goBack">返回首页</el-button>
      </div>
    </header>

    <main class="cart-content">
      <div class="cart-list" v-if="cartStore.items.length > 0">
        <div class="cart-header">
          <span>菜品</span>
          <span>单价</span>
          <span>数量</span>
          <span>小计</span>
          <span>操作</span>
        </div>
        
        <div class="cart-item" v-for="item in cartStore.items" :key="item.dishId">
          <div class="item-info">
            <img :src="item.imageUrl || '/images/dishes/default.jpg'" alt="菜品图片" class="item-image" />
            <div class="item-detail">
              <h4>{{ item.name }}</h4>
              <p>热量: {{ item.calories }}kcal</p>
            </div>
          </div>
          <div class="item-price">¥{{ item.price }}</div>
          <div class="item-quantity">
            <el-button size="small" @click="decreaseQty(item)">-</el-button>
            <span>{{ item.quantity }}</span>
            <el-button size="small" @click="increaseQty(item)">+</el-button>
          </div>
          <div class="item-subtotal">¥{{ (item.price * item.quantity).toFixed(2) }}</div>
          <div class="item-action">
            <el-button type="danger" size="small" @click="removeItem(item)">删除</el-button>
          </div>
        </div>
        
        <div class="cart-footer">
          <el-button type="danger" @click="clearCart">清空购物车</el-button>
          <div class="total-info">
            <span>共 {{ cartStore.totalCount }} 件商品</span>
            <span class="total-price">合计: ¥{{ cartStore.totalPrice }}</span>
          </div>
          <el-button type="primary" class="checkout-btn" @click="checkout">去结算</el-button>
        </div>
      </div>

      <div class="empty-cart" v-else>
        <span class="empty-icon">🛒</span>
        <p>购物车是空的</p>
        <el-button type="primary" @click="goBack">去选购菜品</el-button>
      </div>
    </main>

    <el-dialog title="确认订单" v-model="showOrderModal">
      <el-form :model="orderForm" class="order-form">
        <el-form-item label="取餐时间段">
          <el-select v-model="orderForm.pickupTime" placeholder="请选择取餐时间">
            <el-option label="11:30-12:00" value="11:30-12:00"></el-option>
            <el-option label="12:00-12:30" value="12:00-12:30"></el-option>
            <el-option label="12:30-13:00" value="12:30-13:00"></el-option>
            <el-option label="17:30-18:00" value="17:30-18:00"></el-option>
            <el-option label="18:00-18:30" value="18:00-18:30"></el-option>
            <el-option label="18:30-19:00" value="18:30-19:00"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="订单金额">
          <span class="order-total">¥{{ cartStore.totalPrice }}</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="orderForm.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入特殊要求（如：不要辣、少盐等）"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showOrderModal = false">取消</el-button>
        <el-button type="primary" @click="submitOrder">确认下单</el-button>
      </template>
    </el-dialog>

    <el-dialog title="支付成功" v-model="showSuccessModal">
      <div class="success-content">
        <span class="success-icon">🎉</span>
        <p>订单提交成功！</p>
        <p class="pickup-code">取餐码: <span>{{ pickupCode }}</span></p>
      </div>
      <template #footer>
        <el-button type="primary" @click="goToOrders">查看订单</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore, useCartStore } from '@/stores'
import { orderApi } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()

const showOrderModal = ref(false)
const showSuccessModal = ref(false)
const pickupCode = ref('')

const orderForm = reactive({
  pickupTime: '',
  remark: ''
})

const increaseQty = (item) => {
  cartStore.updateQuantity(item.dishId, item.quantity + 1)
}

const decreaseQty = (item) => {
  cartStore.updateQuantity(item.dishId, item.quantity - 1)
}

const removeItem = (item) => {
  cartStore.removeItem(item.dishId)
}

const clearCart = () => {
  cartStore.clearCart()
}

const goBack = () => {
  router.push('/')
}

const checkout = () => {
  if (!userStore.user) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  showOrderModal.value = true
}

const submitOrder = async () => {
  if (!orderForm.pickupTime) {
    ElMessage.warning('请选择取餐时间')
    return
  }

  try {
    const orderItems = cartStore.items.map(item => ({
      dishId: item.dishId,
      quantity: item.quantity,
      price: item.price,
      name: item.name,
      imageUrl: item.imageUrl
    }))

    const result = await orderApi.submitOrder({
      userId: userStore.user.userId,
      items: orderItems,
      pickupTime: orderForm.pickupTime,
      totalAmount: parseFloat(cartStore.totalPrice),
      remark: orderForm.remark
    })

    pickupCode.value = result.pickupCode
    showOrderModal.value = false
    showSuccessModal.value = true
    cartStore.clearCart()
  } catch (error) {
    ElMessage.error(error.message || '下单失败')
  }
}

const goToOrders = () => {
  showSuccessModal.value = false
  router.push('/orders')
}
</script>

<style scoped>
.cart-container {
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

.cart-content {
  padding: 30px;
  max-width: 1000px;
  margin: 0 auto;
}

.cart-list {
  background: white;
  border-radius: 10px;
  overflow: hidden;
}

.cart-header {
  display: flex;
  padding: 15px 20px;
  background: #f8f9fa;
  font-weight: bold;
  color: #666;
}

.cart-header span {
  flex: 1;
  text-align: center;
}

.cart-header span:first-child {
  flex: 2;
  text-align: left;
}

.cart-item {
  display: flex;
  padding: 20px;
  border-bottom: 1px solid #eee;
  align-items: center;
}

.item-info {
  flex: 2;
  display: flex;
  gap: 15px;
}

.item-image {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
}

.item-detail h4 {
  margin-bottom: 5px;
  color: #333;
}

.item-detail p {
  font-size: 12px;
  color: #999;
}

.item-price, .item-quantity, .item-subtotal, .item-action {
  flex: 1;
  text-align: center;
}

.item-price {
  color: #e74c3c;
  font-weight: bold;
}

.item-quantity {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.item-subtotal {
  color: #e74c3c;
  font-weight: bold;
}

.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #f8f9fa;
}

.total-info {
  display: flex;
  gap: 20px;
}

.total-price {
  color: #e74c3c;
  font-weight: bold;
  font-size: 18px;
}

.checkout-btn {
  padding: 10px 30px;
}

.empty-cart {
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

.order-form {
  padding: 20px;
}

.order-total {
  color: #e74c3c;
  font-weight: bold;
  font-size: 18px;
}

.success-content {
  text-align: center;
  padding: 30px;
}

.success-icon {
  font-size: 60px;
  display: block;
  margin-bottom: 20px;
}

.pickup-code {
  font-size: 18px;
}

.pickup-code span {
  color: #e74c3c;
  font-weight: bold;
  font-size: 24px;
}
</style>