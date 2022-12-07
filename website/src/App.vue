<script setup>
import NavBar from "./components/NavBar.vue";
import Footer from "./components/Footer.vue";
</script>

<template>
  <div class="common-layout">
    <el-header>
      <NavBar />
    </el-header>
    <el-main>
      <RouterView />
    </el-main>
    <Footer />
  </div>
</template>

<style>
#app {
  text-align: center;
}

.main {
  margin: 2vh !important;
  padding: 0;
  min-height: 85vh;
}
</style>
