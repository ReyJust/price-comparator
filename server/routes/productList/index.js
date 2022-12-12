const { success, error } = require("../../utils/wrapper");
const express = require("express"),
  router = express.Router();
const db = require("../../db/index");

/**
 * ProductList Main Route.
 * @name get/
 * @function
 * @memberof productList
 *
 * @return {string} health
 */
router.get("/", function (req, res) {
  try {
    res.status(200).json(success("ProductList main route", {}, res.statusCode));
  } catch (err) {
    res.status(500).json(error("Something went wrong.", res.statusCode));
  }
});

router.get("/product-list", (req, res, next) => {
  try {
    let query = req.query;

    if (query.searchString != "") {
      let pre_query =
        `SELECT *
      FROM
        (SELECT row_number() OVER (
                                  ORDER BY id) AS pagination_no,
                                  *
        FROM
          (SELECT DISTINCT ON (model) model,
                              title,
                              id,
                              image_url
            FROM product p
            WHERE LOWER(model) LIKE LOWER('%` +
        query.searchString +
        `%') ) AS sub) AS sbb
      WHERE pagination_no >= $1 AND pagination_no < $2;`;
      console.log(pre_query);
      db.query(pre_query, [query.start, query.end], (err, result) => {
        if (err) {
          return next(err);
        }
        res.status(200).json(success(result.rows, {}, res.statusCode));
      });
    } else {
      db.query(
        `
        SELECT *
        FROM
          (SELECT row_number() OVER (
                                    ORDER BY id) AS pagination_no,
                                    *
          FROM
            (SELECT DISTINCT ON (model) model,
                                title,
                                id,
                                image_url
              FROM product p
              WHERE model != '' ) AS sub) AS sbb
        WHERE pagination_no >= $1 AND pagination_no < $2;
        `,
        [query.start, query.end],
        (err, result) => {
          if (err) {
            return next(err);
          }
          res.status(200).json(success(result.rows, {}, res.statusCode));
        }
      );
    }
  } catch (err) {
    console.log(err);
    res.status(500).json(error("Something went wrong.", res.statusCode));
  }
});

router.get("/search", (req, res, next) => {
  try {
    let query = req.query;

    db.query(
      `
      SELECT *
      FROM
        (SELECT row_number() OVER (
                                  ORDER BY id) AS pagination_no,
                                  *
        FROM
          (SELECT DISTINCT ON (model) model,
                              title,
                              id,
                              image_url
            FROM product p
            WHERE model LIKE '%$1%' ) AS sub) AS sbb
      WHERE pagination_no >= 0 AND pagination_no < 100;
      `,
      [query.start, query.end, query.searchString],
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
