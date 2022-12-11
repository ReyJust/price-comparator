<template>
  <!-- Browsing Page -->
  <el-row
    ><el-col
      ><h1>
        All your products from the web... <br />With the best prices
      </h1></el-col
    >
    <!-- Dynamic Product Page with Paginations controls -->
    <ProductList
  /></el-row>
</template>

<script setup>
import ProductList from "../components/ProductList.vue";
</script>

<style scoped>
.main {
  padding: 50px;
}
</style>
