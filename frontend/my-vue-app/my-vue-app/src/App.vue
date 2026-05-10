<script lang="ts">
import Hello from './components/hello.vue'
import BindEvent from './components/BindEvent.vue';
import TodoList from './components/TodoList.vue';
import VModel from './components/VModel.vue';
export default {
  components: {
    Hello,
    BindEvent,
    TodoList,
    VModel
  }
}
</script>

<template>
  <TodoList />
</template>

<style scoped></style>
