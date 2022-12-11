<template>
  <!-- Dynamic Product Page -->
  <el-row justify="center" class="main" :gutter="40">
    <!-- Product general Details -->
    <Description />
    <!-- Price Listing of each site component -->
    <Comparison />
  </el-row>
</template>

<script setup>
import Description from "../components/Product/Description.vue";
import Comparison from "../components/Product/Comparison.vue";
</script>

<style></style>
