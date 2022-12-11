<script setup>
import Search from "../components/Search.vue";
</script>

<template>
  <el-main class="main"></el-main>
    <main class="home">
      <div>
        <el-row>
          <el-col :span="12"
            ><div class="grid-content" />
            <Search />
          </el-col>
        </el-row>
      </div>
      <!-- <el-image src="public/pexels-karolina-grabowska-5632371.jpg"> </el-image> -->
    </main></el-main
  >
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
