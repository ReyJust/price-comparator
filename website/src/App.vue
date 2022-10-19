<script setup>
import NavBar from "./components/NavBar.vue";
import Footer from "./components/Footer.vue";
</script>

<template>
  <div class="common-layout">
    <el-header>
      <NavBar />
    </el-header>
    <RouterView />
    <Footer />
  </div>
</template>

<style>
#app {
  text-align: center;
}

.main {
  margin: 0;
  padding: 0;
  min-height: 85vh;
}
</style>
