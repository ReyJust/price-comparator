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
  name: "SearchBar",
  props: { searchString: String },
  data() {
    return {};
  },
  methods: {
    handleSearch() {
      this.$emit("updateSearchString", this.searchString);
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
