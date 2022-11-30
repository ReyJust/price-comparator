//Wrapping server response for uniformization
module.exports = {
  success: (message, results, httpCode) => {
    return {
      message,
      error: false,
      statusCode: httpCode,
      results,
    };
  },

  error: (message, code) => {
    //Some of the most used error http status codes.
    const httpCodes = [200, 201, 400, 401, 404, 404, 403, 422, 500];

    let actualCode = httpCodes.find((httpCode) => httpCode === code);

    !actualCode ? (code = 500) : (code = actualCode);

    return {
      message,
      error: true,
      statusCode: code,
    };
  },
};
