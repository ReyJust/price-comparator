<template>
  <el-menu
    :default-active="activeIndex"
    class="el-menu-demo"
    mode="horizontal"
    :ellipsis="false"
    @select="handleSelect"
    :router="true"
  >
    <el-image
      style="cursor: pointer; width: 100px"
      @click="goHome"
      class="logo"
      src="/Frame 1.png"
      fit="contain"
    />
    <!-- </el-menu-item> -->
    <template v-for="route in this.navItems" :key="route.name">
      <el-menu-item :index="route.path">
        {{ route.name }}
      </el-menu-item>
    </template>
    <div class="flex-grow" />
    <div>
      <el-input
        style="width: 75vh; margin-top: 1vh"
        v-model="searchString"
        placeholder="Product Title"
        :prefix-icon="Search"
        clearable
        size="large"
        @keyup.enter="handleSearch"
      >
      </el-input>
    </div>
  </el-menu>
</template>

<script setup>
import { ref } from "vue";

import { Search } from "@element-plus/icons-vue";
const activeIndex = ref("/");
const handleSelect = (key, keyPath) => {
  // console.log(key, keyPath);
};
</script>

<script>
export default {
  name: "NavBar",
  data() {
    return {
      searchString: "",
    };
  },
  methods: {
    goHome() {
      this.activeIndex = "/";
      console.log(this.activeIndex);
      this.$router.push({ name: "home" });
    },
    handleSearch() {
      this.activeIndex = "/browse";
      this.$router.push({ name: "Browse", query: { s: this.searchString } });
    },
  },
  beforeCreate() {
    this.navItems = this.$router.options.routes.filter(
      (route) => route.name != "Product"
    );
  },
};
</script>

<style scoped>
.flex-grow {
  flex-grow: 1;
}
.logo {
  padding: 1em;
}
</style>
