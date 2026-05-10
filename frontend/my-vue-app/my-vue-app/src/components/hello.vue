<script>
import { ref } from "vue";
export default {
    setup() {
        const count = ref(0);
        const message =ref("hello vue3");
        console.log( count.value);
        count.value++;
        return{
            count,
            message
        }
    }

}
</script>
<template>
    <div>
        <p>{{ message }}</p>
        <p>计数：{{ count }}</p>
        <button @click="count++">增加</button>
    </div>
</template>