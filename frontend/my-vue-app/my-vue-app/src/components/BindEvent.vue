<template>
  <!-- 完整语法 -->
  <button v-on:click="doSomething">点击</button>
  
  <!-- 缩写 -->
  <button @click="doSomething">点击</button>
  
  <!-- 内联语句 -->
  <button @click="count++">增加</button>
  
  <!-- 多个事件 -->
  <button @click="doThis" @mouseenter="doThat">点击</button>
  
  <!-- 传递参数 -->
  <button @click="say('hi')">打招呼</button>
  
  <!-- 事件对象 -->
  <button @click="handleClick($event)">点击</button>
</template>

<script>
import { ref } from 'vue'

export default {
  setup() {
    const count = ref(0)
    
    function doSomething() {
      console.log('被点击了')
    }
    
    function say(message) {
      alert(message)
    }
    
    function handleClick(event) {
      console.log('事件对象：', event)
    }
    function doThis(){
        console.log("鼠标点击了")
    }
    function doThat(){
        console.log("鼠标进入了")
    }
    return {
      count,
      doSomething,
      say,
      handleClick,
      doThis,
      doThat
    }
  }
}
</script>