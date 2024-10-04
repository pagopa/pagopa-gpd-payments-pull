import { getToService } from "./modules/payment_pull_client.js";
import { SharedArray } from 'k6/data';
import { check } from 'k6';

const varsArray = new SharedArray('vars', function () {
    return JSON.parse(open(`./${__ENV.VARS}`)).environment;
});
export const ENV_VARS = varsArray[0];

const paymentsPullServiceURIBasePath = `${ENV_VARS.paymentsPullServiceURIBasePath}`;
const userTaxCode = `${ENV_VARS.userTaxCode}`;

export default function () {
        let response = getToService(`${paymentsPullServiceURIBasePath}/payment-notices/v1`, userTaxCode);
        console.info(`Payment Pull Service getPaymentNotices status ${response.status}`);

        let responseBody = JSON.parse(response.body);

        check(response, {
            'Payment Pull Service getPaymentNotices status is 200': () => response.status === 200,
            'Payment Pull Service getPaymentNotices body has list of payment notices': () =>
                Boolean(responseBody && responseBody.length)
        });
    }
}