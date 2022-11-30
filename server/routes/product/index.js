const { success, error } = require("../../utils/wrapper");
const express = require("express"),
  router = express.Router();
const db = require("../../db/index");

/**
 * Get a product information.
 * @name get/
 * @function
 * @memberof product
 *
 * @return {string} product info
 */
router.get("/", function (req, res) {
  try {
    let body = req.body;
    console.log("REQ->" + req);

    db.query(
      `SELECT *
      FROM product
      INNER JOIN product_details pd ON product.id = pd.product_id
      AND model = $1
      LIMIT 1;
      `,
      [body.id],
      (err, result) => {
        if (err) {
          return next(err);
        }
        res.status(200).json(success(result.rows[0], {}, res.statusCode));
      }
    );
  } catch (err) {
    console.log(err);
    res.status(500).json(error("Something went wrong.", res.statusCode));
  }
});

module.exports = router;
