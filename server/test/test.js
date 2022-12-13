const axios = require("axios");
const assert = require("assert");
const url = "http://localhost:3000";

//TEST product list
describe("GET /browse/product-list", function () {
  describe("Get product list with pagination 1 - 11", function () {
    it("It should return 10 product as JSON with 4 key: value", async function () {
      let res = await axios.get(`${url}/browse/product-list`, {
        params: {
          start: 0,
          end: 11,
          searchString: "",
        },
      });

      assert.equal(res.data.message.length, 10);

      assert.equal(typeof res.data.message[0], "object");

      res.data.message.forEach((elem) => {
        assert(Object.keys(elem).includes("pagination_no") == true);
        assert(Object.keys(elem).includes("model") == true);
        assert(Object.keys(elem).includes("title") == true);
        assert(Object.keys(elem).includes("image_url") == true);
      });
    });
  });

  describe("Get product list with pagination 10 and 20", function () {
    it("It should product with pagination number between 10 and 20", async function () {
      let res = await axios.get(`${url}/browse/product-list`, {
        params: {
          start: 11,
          end: 21,
          searchString: "",
        },
      });

      assert.equal(res.data.message.length, 10);

      assert.equal(typeof res.data.message[0], "object");

      res.data.message.forEach((elem) => {
        assert(Object.keys(elem).includes("pagination_no") == true);
        assert(Object.keys(elem).includes("pagination_no") < 21);

        assert(Object.keys(elem).includes("model") == true);
        assert(Object.keys(elem).includes("title") == true);
        assert(Object.keys(elem).includes("image_url") == true);
      });
    });
  });

  describe("Get product list with pagination 1 - 11 searching model containing a A", function () {
    it("It should a maximum of 10 product with A in the model.", async function () {
      let res = await axios.get(`${url}/browse/product-list`, {
        params: {
          start: 0,
          end: 11,
          searchString: "A",
        },
      });

      assert(res.data.message.length <= 10);
      assert.equal(typeof res.data.message[0], "object");

      res.data.message.forEach((elem) => {
        assert(elem["model"].indexOf("a"));
      });
    });
  });
});

//TEST search product
describe("GET /browse/product-list", function () {});

//TEST product comparison
describe("GET /product/compare", function () {
  describe("Get list of website and url selling the product", function () {
    it("It should a give list of website selling the product", async function () {
      let res = await axios.get(`${url}/product/compare`, {
        params: {
          model: "XG32VC",
        },
      });
      // No more than 5 website
      assert(res.data.message.length <= 5);

      res.data.message.forEach((elem) => {
        assert(Object.keys(elem).includes("website_id") == true);
        assert(Object.keys(elem).includes("title") == true);
        assert(Object.keys(elem).includes("website_image_url") == true);
        assert(Object.keys(elem).includes("price") == true);
        assert(Object.keys(elem).includes("url") == true);
      });
    });
  });
});

//TEST product description
describe("GET /product", function () {
  describe("Get the details of a product", function () {
    it("It should a give list of the product specs", async function () {
      let res = await axios.get(`${url}/product`, {
        params: {
          id: "XG32VC",
        },
      });

      let needed_keys = [
        "id",
        "brand",
        "description",
        "image_url",
        "model",
        "price",
        "title",
        "url",
        "website_id",
        "display_resolution_px",
        "refresh_rate_hz",
        "screen_size_inch",
        "product_id",
      ];
      for (key in needed_keys) {
        assert(
          Object.keys(res.data.message).includes(needed_keys[key]) == true
        );
      }

      assert.notEqual(typeof res.data.message["price"], "String");
    });
  });
});
