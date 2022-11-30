const { Pool } = require("pg");
//Database connections
const pool = new Pool({
  user: "mdx_user",
  host: "localhost",
  database: "price_comparator",
  password: "password",
  port: 5432,
});

module.exports = {
  query: (text, params, callback) => {
    return pool.query(text, params, callback);
  },
};
