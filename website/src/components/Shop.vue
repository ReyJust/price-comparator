<template>
  <el-row :span="24" justify="center" :gutter="25">
    <el-col v-for="product in products" :key="product" :span="10">
      <el-card
        shadow="hover"
        style="margin-top: 10px; margin-bottom: 10px; min-height: 150px"
        :body-style="{ padding: '0px' }"
        class="box-card"
        @click="handleSelectProduct(encodeURIComponent(product.model))"
      >
        <div style="display: flex; margin: 2%">
          <div>
            <img
              :src="product.image_url"
              style="width: 125px; margin-top: auto; margin-bottom: auto"
              class="image"
            />
          </div>
          <div style="margin-top: auto; margin-bottom: auto">
            <h3
              style="
                text-transform: uppercase;
                text-align: left;
                margin-left: 10px;
              "
            >
              {{ product.title }}
            </h3>
            <!-- <div class="bottom">
                <span>As From RS {{ product.price }}</span>
              </div> -->
          </div>
        </div>
      </el-card>
    </el-col>
    <el-col>
      <el-row justify="center">
        <el-button>&lt;&lt;</el-button>
        <el-dropdown
          ><el-button>
            {{ pageSize }}<el-icon><arrow-down /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>10</el-dropdown-item>
              <el-dropdown-item>20</el-dropdown-item>
              <el-dropdown-item>30</el-dropdown-item>
            </el-dropdown-menu>
          </template></el-dropdown
        >
        <el-button @click="handleNextPage()">&gt;&gt;</el-button>
      </el-row></el-col
    >
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
      // this.currentPaginationNo += 1;
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
/* .image {
  width: 100%;
  display: block;
} */

.el-card {
  cursor: pointer;
}
</style>
