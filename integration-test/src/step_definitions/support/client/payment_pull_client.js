const { post, del } = require("../utility/axios_common");
const fs = require("fs");

const GPD_PULL_HOST = process.env.GPD_PULL_HOST;
const API_TIMEOUT = process.env.API_TIMEOUT;

function getNotices(taxCode, dueDate) {
    return post(GPD_HOST + `payment-notices/v1`, body, {
        timeout: API_TIMEOUT,
        params: {
            dueDate: dueDate,
            page: 0,
            size: 50
        },
        headers: {
            "Ocp-Apim-Subscription-Key": process.env.GPD_PULL_API_SUBSCRIPTION_KEY,
            "Content-Type": "application/json"
        }
    })
}

module.exports = {
	getNotices
}
