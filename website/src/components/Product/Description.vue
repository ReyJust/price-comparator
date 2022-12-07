<template>
  <el-col :span="22">
    <el-card class="box-card">
      <div style="display: flex">
        <div>
          <el-image
            style="width: 300px; margin: 12px"
            :src="productInfo.image_url"
            fit="fill"
          />
        </div>
        <div>
          <h3 id="description">{{ productInfo.title }}</h3>
          <el-descriptions
            title="Specifications"
            direction="horizontal"
            :column="1"
            size="default"
            border
          >
            <el-descriptions-item label="Model">{{
              productInfo.model
            }}</el-descriptions-item>
            <el-descriptions-item label="Brand">{{
              productInfo.brand
            }}</el-descriptions-item>
            <el-descriptions-item label="Display Resolution"
              >{{ productInfo.display_resolution_px }}
            </el-descriptions-item>
            <el-descriptions-item label="Refresh Rate">{{
              productInfo.refresh_rate_hz
            }}</el-descriptions-item>
            <el-descriptions-item label="Screen Size">
              {{ productInfo.screen_size_inch }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </el-card>
  </el-col>
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

      this.productInfo = res.data.message;
    },
  },
};
</script>

<style>
#description {
  text-transform: uppercase;
  text-align: left;
}
</style>
