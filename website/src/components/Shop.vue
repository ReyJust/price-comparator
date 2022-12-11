<script setup>
import SearchComponent from "./SearchComponent.vue";
</script>

<template>
  <el-row :span="24" justify="center" :gutter="25">
    <el-col>
      <SearchComponent
        :searchString="searchString"
        @updateSearchString="updateSearchString"
      />
    </el-col>
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
        <el-col :span="2">
          <el-button @click="handlePaging('prev')" :disabled="isFirstPage"
            ><el-icon><DArrowLeft /></el-icon
          ></el-button>
        </el-col>
        <el-col :span="2">
          <el-dropdown @command="changePageSize"
            ><el-button>
              {{ pageSize }}<el-icon><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="10">10</el-dropdown-item>
                <el-dropdown-item command="20">20</el-dropdown-item>
                <el-dropdown-item command="30">30</el-dropdown-item>
              </el-dropdown-menu>
            </template></el-dropdown
          ></el-col
        >
        <el-col :span="2">
          <el-button @click="handlePaging('next')"
            ><el-icon><DArrowRight /></el-icon
          ></el-button> </el-col></el-row
    ></el-col>
  </el-row>
</template>

<script>
import axios from "axios";
import { Search } from "@element-plus/icons-vue";

export default {
  name: "Shop",
  data() {
    return {
      products: [],
      searchString: "",

      currentPaginationNo: 1,
      pageSize: 10,
    };
  },
  async created() {
    await this.getProductList(
      this.currentPaginationNo,
      this.currentPaginationNo + this.pageSize
    );

    this.currentPaginationNo += this.products.length;
  },
  methods: {
    async getProductList() {
      let end = this.currentPaginationNo + this.pageSize;

      // if (this.$route.query.s) {
      //   this.searchString = this.$route.query.s;
      // }

      let res = await axios.get(`http://localhost:3000/browse/product-list`, {
        params: {
          start: this.currentPaginationNo,
          end: end,
          searchString: this.searchString,
        },
      });
      this.products = res.data.message;
    },
    handleSelectProduct(product_id) {
      this.$router.push("/product/" + product_id);
    },
    async handlePaging(way) {
      if (way == "next") {
        this.currentPaginationNo = this.currentPaginationNo + this.pageSize;
        await this.getProductList();
      } else if (way == "prev") {
        this.currentPaginationNo = this.currentPaginationNo - this.pageSize;

        if (this.currentPaginationNo < 0) {
          this.currentPaginationNo = 0;
        }
        await this.getProductList();
      }

      this.$router.push({
        path: "browse",
        query: { start: this.currentPaginationNo },
      });
    },

    async changePageSize(command) {
      this.pageSize = parseInt(command);

      await this.getProductList();
    },

    async updateSearchString(newValue) {
      this.searchString = newValue;

      await this.getProductList();
    },
  },
  computed: {
    isFirstPage() {
      return this.currentPaginationNo <= this.pageSize + 1;
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
