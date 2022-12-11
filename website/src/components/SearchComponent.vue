<template>
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
</template>

<script setup>
import { Search } from "@element-plus/icons-vue";
</script>

<script>
export default {
  name: "SearchComponent",
  props: { searchString: String },
  data() {
    return {};
  },
  methods: {
    handleSearch() {
      console.log(this.searchString);

      this.$emit("updateSearchString", this.searchString);
      // this.activeIndex = "/browse";

      // this.$router.push({ name: "Browse", query: { s: this.searchString } });
    },
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
