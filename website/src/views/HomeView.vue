<script setup>
import Home from "../components/Home.vue";
</script>

<template>
  <!-- Presentation Page -->
  <el-main class="main">
    <main class="home">
      <div>
        <el-row>
          <el-col :span="12"
            ><div class="grid-content" />
            <Home />
          </el-col>
        </el-row>
      </div></main
  ></el-main>
</template>

<style scoped>
.main {
  background-image: url("/pexels-karolina-grabowska-5632371.jpg");
  background-color: "white";
  background-repeat: no-repeat;
  background-position: bottom right;
  background-size: cover;
}
</style>
