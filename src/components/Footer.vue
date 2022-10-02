<template>
  <el-footer height="2vh" class="footer">
    <el-row align="middle" justify="center" :gutter="20">
      <el-col
        ><span style="margin: 0">
          Made by Justin Mayer, Advanced Web Development & Big Data
        </span></el-col
      ></el-row
    >
  </el-footer>
</template>

<script>
export default {
  name: "Footer",
};
</script>

<style scoped>
.footer {
  /* background-color: var(--el-color-primary);
  */
  background-color: #ffffff;
  margin: 10px;
}
</style>
