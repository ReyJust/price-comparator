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
            @click="handleSelectProduct(product.id)"
          >
            <img :src="product.img" class="image" />
            <div style="padding: 20px">
              <span>{{ product.title }}</span>
              <div class="bottom">
                <span>As From RS {{ product.price }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-col>
    <el-col :span="3">Pub</el-col>
  </el-row>
</template>

<script>
export default {
  name: "Shop",
  data() {
    return {
      products: [
        {
          id: "p_1",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
        {
          id: "p_2",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
        {
          id: "p_3",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
        {
          id: "p_4",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
        {
          id: "p_2",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
        {
          id: "p_2",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
        {
          id: "p_2",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
        {
          id: "p_2",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
        {
          id: "p_2",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
        {
          id: "p_2",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
        {
          id: "p_2",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
        {
          id: "p_2",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
        {
          id: "p_2",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
        {
          id: "p_2",
          img: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png",
          title: "Product Title",
          price: "000.00",
        },
      ],
    };
  },
  methods: {
    handleSelectProduct(product_id) {
      console.log(product_id);
      this.$router.push("/product/" + product_id);
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
