//CommonJS impotrs
const express = require("express");
const cors = require("cors");
const routes = require("./routes/routes");
const bodyParser = require("body-parser");
const app = express();

//Allow CORS when requesting form same origin
app.use(
  cors({
    origin: "http://localhost:5173",
    methods: ["POST", "PUT", "GET", "OPTIONS", "HEAD"],
  })
);
app.use(bodyParser.json());

//Routes
app.use("/", routes);

port = 3000;

//Listening
app.listen(port, () => console.log(`Listening on port ${port}`));
