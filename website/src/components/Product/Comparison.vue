<template>
  <el-col :span="22">
    <el-card class="box-card">
      <el-table :data="prices" style="width: 100%" striped>
        <el-table-column label="" width="250" align="center">
          <template #default="scope">
            <el-image style="height: 50px" :src="scope.row.website_image_url">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column label="Merchant">
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <span
                ><a :href="scope.row.url" target="_blank">{{
                  scope.row.title
                }}</a></span
              >
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="Price" width="200" />
      </el-table>
    </el-card>
  </el-col>
</template>

<script>
import axios from "axios";
export default {
  name: "Comparison",
  data() {
    return {
      prices: [
        {
          title: "",
          price: 0,
          website_image_url: "",
        },
      ],
    };
  },
  async created() {
    await this.getProductComparison();
  },

  methods: {
    async getProductComparison() {
      let res = await axios.get(`http://localhost:3000/product/compare`, {
        params: {
          model: this.$route.params.id,
        },
      });
      console.log(res);
      this.prices = res.data.message;
    },
  },
};
</script>

<style></style>
