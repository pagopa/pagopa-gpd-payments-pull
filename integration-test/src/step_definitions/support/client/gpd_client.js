const { post, del } = require("../utility/axios_common");
const fs = require("fs");

const GPD_HOST = process.env.GPD_HOST;
const API_TIMEOUT = process.env.API_TIMEOUT;

function createAndPublishDebtPosition(orgId, body) {
    return post(GPD_HOST + `/organizations/${orgId}/debtpositions`, body, {
        timeout: API_TIMEOUT,
        params: {
            toPublish: "True",
        },
        headers: {
            "Ocp-Apim-Subscription-Key": process.env.GPD_API_SUBSCRIPTION_KEY,
            "Content-Type": "application/json"
        }
    })
}

function deleteDebtPosition(orgId, iupd, segCodes) {
	const params = {}
	if (segCodes) {params.segregationCodes = segCodes}
    return del(GPD_HOST + `/organizations/${orgId}/debtpositions/${iupd}`, {
        timeout: API_TIMEOUT,
        params,
        headers: {
            "Ocp-Apim-Subscription-Key": process.env.GPD_API_SUBSCRIPTION_KEY,
            "Content-Type": "application/json"
        }
    })
}

module.exports = {
    createAndPublishDebtPosition,
    deleteDebtPosition
}