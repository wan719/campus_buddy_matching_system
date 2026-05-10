<template>
  <div class="todo-app">
    <h1>📋 待办事项</h1>

    <div class="input-group">
      <input
        v-model="newTodoText"
        @keyup.enter="addTodo"
        placeholder="输入待办事项，按回车添加"
      >
      <button @click="addTodo" class="btn-add">添加</button>
    </div>

    <div class="filter-bar" v-show="todos.length > 0">
      <button
        v-for="f in filters"
        :key="f.value"
        :class="['btn-filter', { active: currentFilter === f.value }]"
        @click="currentFilter = f.value"
      >
        {{ f.label }}
      </button>
    </div>

    <ul class="todo-list" v-if="filteredTodos.length > 0">
      <li v-for="todo in filteredTodos" :key="todo.id" :class="{ completed: todo.completed }">
        <input type="checkbox" v-model="todo.completed" class="todo-checkbox">
        <span class="todo-text">{{ todo.text }}</span>
        <button @click="removeTodo(todo.id)" class="btn-delete">删除</button>
      </li>
    </ul>

    <div class="empty-state" v-else>
      <p v-if="todos.length === 0">还没有待办事项，快来添加吧 🎉</p>
      <p v-else>当前筛选条件下暂无数据</p>
    </div>

    <div class="footer-bar" v-show="todos.length > 0">
      <span class="stats">剩余 <strong>{{ incompleteCount }}</strong> 项未完成</span>
      <button
        v-if="completedCount > 0"
        @click="clearCompleted"
        class="btn-clear"
      >
        清除已完成
      </button>
    </div>
  </div>
</template>

<script>
import { ref, computed, watch, onMounted } from 'vue'

const STORAGE_KEY = 'vue-todo-list'

function loadTodos() {
  try {
    const data = localStorage.getItem(STORAGE_KEY)
    return data ? JSON.parse(data) : null
  } catch {
    return null
  }
}

function saveTodos(todos) {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(todos))
  } catch {
    // 忽略存储失败
  }
}

export default {
  setup() {
    const saved = loadTodos()
    const todos = ref(saved || [
      { id: 1, text: '学习Vue 3', completed: false },
      { id: 2, text: '完成作业', completed: true }
    ])

    const newTodoText = ref('')
    const currentFilter = ref('all')

    const filters = [
      { label: '全部', value: 'all' },
      { label: '进行中', value: 'active' },
      { label: '已完成', value: 'completed' }
    ]

    const incompleteCount = computed(() => {
      return todos.value.filter(t => !t.completed).length
    })

    const completedCount = computed(() => {
      return todos.value.filter(t => t.completed).length
    })

    const filteredTodos = computed(() => {
      switch (currentFilter.value) {
        case 'active':
          return todos.value.filter(t => !t.completed)
        case 'completed':
          return todos.value.filter(t => t.completed)
        default:
          return todos.value
      }
    })

    function addTodo() {
      const text = newTodoText.value.trim()
      if (text) {
        todos.value.push({
          id: Date.now(),
          text,
          completed: false
        })
        newTodoText.value = ''
      }
    }

    function removeTodo(id) {
      todos.value = todos.value.filter(t => t.id !== id)
    }

    function clearCompleted() {
      todos.value = todos.value.filter(t => !t.completed)
    }

    watch(todos, (val) => {
      saveTodos(val)
    }, { deep: true })

    return {
      todos,
      newTodoText,
      currentFilter,
      filters,
      incompleteCount,
      completedCount,
      filteredTodos,
      addTodo,
      removeTodo,
      clearCompleted
    }
  }
}
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.todo-app {
  max-width: 520px;
  margin: 60px auto;
  padding: 30px 28px;
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
}

h1 {
  text-align: center;
  color: #2c3e50;
  font-size: 28px;
  margin: 0 0 24px 0;
  font-weight: 700;
}

.input-group {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.input-group input {
  flex: 1;
  padding: 12px 16px;
  border: 2px solid #e8ecf1;
  border-radius: 10px;
  font-size: 15px;
  outline: none;
  transition: border-color 0.25s;
}

.input-group input:focus {
  border-color: #42b983;
}

.btn-add {
  padding: 12px 22px;
  background: #42b983;
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.25s;
}

.btn-add:hover {
  background: #38a573;
}

.btn-add:active {
  background: #2e8b5f;
}

.filter-bar {
  display: flex;
  justify-content: center;
  gap: 6px;
  margin-bottom: 16px;
}

.btn-filter {
  padding: 7px 18px;
  border: 2px solid #e8ecf1;
  border-radius: 20px;
  background: transparent;
  color: #666;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s;
}

.btn-filter:hover {
  border-color: #42b983;
  color: #42b983;
}

.btn-filter.active {
  background: #42b983;
  border-color: #42b983;
  color: white;
}

.todo-list {
  list-style: none;
  padding: 0;
  margin: 0 0 8px 0;
}

.todo-list li {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 14px;
  border-radius: 10px;
  margin-bottom: 6px;
  background: #f8fafb;
  transition: background 0.2s;
}

.todo-list li:hover {
  background: #eef2f5;
}

.todo-checkbox {
  width: 20px;
  height: 20px;
  accent-color: #42b983;
  cursor: pointer;
  flex-shrink: 0;
}

.todo-text {
  flex: 1;
  font-size: 15px;
  color: #2c3e50;
  word-break: break-all;
}

.todo-list li.completed .todo-text {
  text-decoration: line-through;
  color: #b0b8c1;
}

.btn-delete {
  padding: 6px 14px;
  background: #ff6b6b;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.25s;
  flex-shrink: 0;
}

.btn-delete:hover {
  background: #e55a5a;
}

.btn-delete:active {
  background: #cc4c4c;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #b0b8c1;
  font-size: 15px;
}

.footer-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f0f2f4;
}

.stats {
  color: #8899a6;
  font-size: 14px;
}

.stats strong {
  color: #42b983;
}

.btn-clear {
  padding: 8px 16px;
  background: transparent;
  color: #ff6b6b;
  border: 1px solid #ffcccc;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s;
}

.btn-clear:hover {
  background: #ff6b6b;
  color: white;
  border-color: #ff6b6b;
}
</style>
