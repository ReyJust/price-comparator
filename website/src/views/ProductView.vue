<template>
  <div>Product View</div>
  <p>{{ $route.params.id }}</p>
  <el-row class="main">
    <el-col :span="18">
      <el-row :gutter="30" justify="left">
        <el-col :span="18"> <Description /></el-col>
      </el-row>
    </el-col>
  </el-row>
</template>

<script setup>
import Description from "../components/Product/Description.vue";
</script>

<style></style>
