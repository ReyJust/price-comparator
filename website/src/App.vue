<script setup>
import NavBar from "./components/NavBar.vue";
import Footer from "./components/Footer.vue";
</script>

<template>
  <!-- Main App Layout -->
  <div class="common-layout">
    <!-- Navbar -->
    <el-header>
      <NavBar />
    </el-header>
    <!-- Dynamic Body Content  with router-->
    <el-main>
      <RouterView />
    </el-main>
    <!-- Main Footer -->
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
