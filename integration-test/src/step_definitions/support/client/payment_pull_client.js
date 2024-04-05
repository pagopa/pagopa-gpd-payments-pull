const { get } = require("../utility/axios_common");
const fs = require("fs");

const GPD_PULL_HOST = process.env.GPD_PULL_HOST;
const API_TIMEOUT = process.env.API_TIMEOUT;

async function getNotices(taxCode, dueDate) {
    const data = await get(GPD_PULL_HOST + `/payment-notices/v1`, {
        timeout: API_TIMEOUT,
        params: {
            dueDate: dueDate,
            page: 0,
            size: 50
        },
        headers: {
            "x-tax-code": taxCode,
            "Ocp-Apim-Subscription-Key": process.env.GPD_PULL_API_SUBSCRIPTION_KEY,
            "Content-Type": "application/json"
        }
    });
    return data;
}

module.exports = {
	getNotices
}
