<template>
  <el-row justify="center">
    <el-col :span="3">Pub</el-col>
    <el-col :span="18">
      <el-row class="main" :gutter="30" justify="center">
        <el-col v-for="product in products" :key="product" :span="5">
          <el-card
            shadow="hover"
            style="margin-top: 10px; margin-bottom: 10px"
            :body-style="{ padding: '0px' }"
            @click="handleSelectProduct(encodeURIComponent(product.model))"
          >
            <img :src="product.image_url" class="image" />
            <div style="padding: 20px">
              <span>{{ product.title }}</span>
              <!-- <div class="bottom">
                <span>As From RS {{ product.price }}</span>
              </div> -->
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row justify="center">
        <el-button>Previous Page</el-button>
        <el-dropdown
          ><el-button size="small">
            Dropdown List<el-icon><arrow-down /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>10</el-dropdown-item>
              <el-dropdown-item>20</el-dropdown-item>
              <el-dropdown-item>50</el-dropdown-item>
            </el-dropdown-menu>
          </template></el-dropdown
        >
        <el-button @click="handleNextPage()">Next Page</el-button>
      </el-row>
    </el-col>
    <el-col :span="3">Pub</el-col>
  </el-row>
</template>

<script>
import axios from "axios";

export default {
  name: "Shop",
  data() {
    return {
      products: [],

      currentPaginationNo: 0,
      pageSize: 10,
    };
  },
  async created() {
    await this.getProductList();
  },
  // async () => {
  // },
  methods: {
    async getProductList() {
      this.currentPaginationNo += 1;
      let res = await axios.get(`http://localhost:3000/browse/product-list`, {
        params: {
          start: this.currentPaginationNo,
          end: this.currentPaginationNo + this.pageSize,
        },
      });
      this.products = res.data.message;

      this.currentPaginationNo += this.products.length;
    },
    handleSelectProduct(product_id) {
      this.$router.push("/product/" + product_id);
    },
    async handleNextPage() {
      await this.getProductList();
    },
  },
};
</script>

<style scoped>
.image {
  width: 100%;
  display: block;
}

.el-card {
  cursor: pointer;
}
</style>
