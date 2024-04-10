const assert = require('assert');
const {defineParameterType, Given, When, Then, After, AfterAll} = require('@cucumber/cucumber');
const {executeDebtPositionCreationAndPublication, executeDebtPositionDeletion} = require("./support/logic/gpd_logic");
const {gpdSessionBundle} = require('./support/utility/data');
const {getNotices} = require("./support/client/payment_pull_client");
const {formatWithValidYear} = require('./support/utility/helpers');

const idOrg = process.env.ORGANIZATIONAL_FISCAL_CODE;
const positions = [];

defineParameterType({
    name: "boolean",
    regexp: /true|false/,
    transformer: (s) => s === "true"
});


// After each Scenario
AfterAll(async function () {

    // remove positions
    if (this.positions != null && this.positions.length > 0) {
        for (let position of positions) {
            await executeDebtPositionDeletion(idOrg, position);
        }
    }
    this.positions = [];
    this.response = null;

});

Given('the payment notice {string} for the taxCode {string} with due date {string} and pull recovery on {boolean}',
    async function (debtCode, fiscalCode, dueDate, pullFlag) {
        if (pullFlag) {
            await executeDebtPositionDeletion(idOrg, debtCode);
            let response = await executeDebtPositionCreationAndPublication(gpdSessionBundle, idOrg, debtCode, fiscalCode, dueDate, pullFlag);
            assert.strictEqual(response.status, 201);
            if (this.positions === undefined) {
                this.positions = [];
            }
            this.positions.push(response.data);
        }
    });

When('an Http GET request is sent to recover notices for taxCode {string} with dueDate {string}',
    async function (taxCode, dueDate) {
        this.response = await getNotices(taxCode, formatWithValidYear(dueDate));
    });

Then('response contains notice {string}', function (expectedCode) {
    assert.ok(this.response?.data?.some(item => item.iupd === expectedCode));
});

Then('response has size {int}', function (expectedSize) {
    assert.strictEqual(this.response?.data?.length, expectedSize);
});

Then('response has a {int} Http status', function (expectedStatus) {
    assert.strictEqual(this.response.status, expectedStatus);
});
