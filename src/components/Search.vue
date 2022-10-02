<script setup>
import { Search } from "@element-plus/icons-vue";

import { ref } from "vue";
const input = ref("");
</script>

<template>
  <el-row align="middle">
    <el-col :span="22" :offset="2">
      <el-card class="box-card search-box">
        <div class="search-bar">
          <p style="text-align: left; color: gray; margin-bottom: 5px">
            Find the best price for your product
          </p>
          <el-input v-model="input" placeholder="Product Title" clearable>
            <template #append> <el-button :icon="Search" /> </template
          ></el-input>
        </div> </el-card
    ></el-col>
  </el-row>
</template>

<style scoped>
.search-box {
  /* min-height: 50px; */
}
.search-bar {
  padding-top: 25px;
  padding-bottom: 25px;
}
.el-row {
  height: 90vh;
}
</style>
