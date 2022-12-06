<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>Product Name name</span>
        <el-button class="button" text>Operation button</el-button>
      </div>
    </template>
    <div>
      <p>Image</p>
      <p>Description</p>
    </div>
  </el-card>
</template>

<script>
import axios from "axios";

export default {
  name: "Description",
  data() {
    return {
      productInfo: {},
    };
  },
  async created() {
    await this.getProductInfo();
  },

  methods: {
    async getProductInfo() {
      let res = await axios.get(`http://localhost:3000/product`, {
        params: {
          id: this.$route.params.id,
        },
      });
      console.log(res);
      this.products = res.data.message;
    },
  },
};
</script>

<style></style>
