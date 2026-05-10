<template>
  <!-- 文本输入 -->
  <input v-model="text" placeholder="输入文本">
  <p>你输入了：{{ text }}</p>
  
  <!-- 多行文本 -->
  <textarea v-model="message"></textarea>
  
  <!-- 复选框 -->
  <input type="checkbox" v-model="checked">
  
  <!-- 多个复选框 -->
  <input type="checkbox" value="选项1" v-model="checkedNames">
  <input type="checkbox" value="选项2" v-model="checkedNames">
  
  <!-- 单选按钮 -->
  <input type="radio" value="One" v-model="picked">
  <input type="radio" value="Two" v-model="picked">
  
  <!-- 选择框 -->
  <select v-model="selected">
    <option value="">请选择</option>
    <option>A</option>
    <option>B</option>
  </select>
</template>

<script>
import { ref } from 'vue'

export default {
  setup() {
    const text = ref('')
    const message = ref('')
    const checked = ref(false)
    const checkedNames = ref([])
    const picked = ref('')
    const selected = ref('')
    
    return {
      text,
      message,
      checked,
      checkedNames,
      picked,
      selected
    }
  }
}
</script>