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
    let query = req.query;

    db.query(
      `SELECT *
      FROM product
      INNER JOIN product_details pd ON product.id = pd.product_id
      AND model = $1
      LIMIT 1;
      `,
      [query.id],
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

router.get("/compare", function (req, res, next) {
  try {
    let query = req.query;

    db.query(
      `SELECT *
      FROM
        (SELECT DISTINCT ON (website_id) website_id,
                            w.title,
                            w.website_image_url,
                            price,
                            p.url
         FROM product AS p
         INNER JOIN website w ON website_id=w.id
         WHERE model = $1) AS sub
      ORDER BY price ASC;
      `,
      [query.model],
      (err, result) => {
        if (err) {
          return next(err);
        }
        res.status(200).json(success(result.rows, {}, res.statusCode));
      }
    );
  } catch (err) {
    console.log(err);
    res.status(500).json(error("Something went wrong.", res.statusCode));
  }
});

module.exports = router;
