const { success, error } = require("../utils/wrapper");
const express = require("express"),
  router = express.Router();

var productListRoutes = require("./productList/index");
var productRoutes = require("./product/index");

/**
 * App Main Route.
 * @name get/
 * @function
 * @memberof router
 *
 * @return {string} health
 */
router
  .get("/", function (req, res) {
    try {
      res
        .status(200)
        .json(success("Backend Up and Running!", {}, res.statusCode));
    } catch (err) {
      res.status(500).json(error("Something went wrong.", res.statusCode));
    }
  })
  .use("/browse", productListRoutes)
  .use("/product", productRoutes);

module.exports = router;
