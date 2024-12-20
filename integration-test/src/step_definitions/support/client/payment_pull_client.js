const {get} = require("../utility/axios_common");

const GPD_PULL_HOST = process.env.GPD_PULL_HOST;
const API_TIMEOUT = process.env.API_TIMEOUT;

async function getNotices(taxCode, dueDate) {
    let params = {
        page: 0,
        limit: 10
    };

    if (dueDate) {
        params.dueDate = dueDate;
    }
    const data = await get(GPD_PULL_HOST + `/payment-notices/v1`, {
        timeout: API_TIMEOUT,
        params,
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
