const assert = require('assert');
const { Given, When, Then, After } = require('@cucumber/cucumber');
const { executeDebtPositionCreationAndPublication, deletePositions } =
 require("./support/logic/gdp_logic.js");
const { gpdSessionBundle } = require('./utility/data');
const { getNotices } = require("./support/client/payment_pull_client.js");

const idOrg = process.env.ORGANIZATIONAL_FISCAL_CODE;

// After each Scenario
After(async function () {

  // remove positions
  if (this.positions != null && this.positions.length > 0) {
     for (index : positions) {
        await executeDebtPositionDeletion(idOrg, positions[position]);
     }
  }
  this.positions = [];
  this.response = null;

});

Given('the payment notice {string} for the taxCode {string} with due date {string} and pull recovery on {boolean}',
 async function (debtCode, fiscalCode, dueDate, pullFlag) {
  if (pullFlag) {
      await executeDebtPositionDeletion(idOrg, debtCode);
      let response = await executeDebtPositionCreationAndPublication(
        gpdSessionBundle, idOrg, debtCode, fiscalCode, dueDate, pullFlag);
      assert.strictEqual(response.statusCode, 201);
      this.positions.push(response.data);
  }
});

When('an Http GET request is sent to recover notices for taxCode {string} with dueDate {string}',
 async function (taxCode, dueDate) {
  this.response = await getNotices(taxCode, dueDate);
});

Then('response contains notice {string}', function (expectedCode) {
  assert.strictEqual(this.response?.data?.attachments?.[0]?.iuv, expectedCode);
});

Then('response has size {int}', function (expectedSize) {
  assert.strictEqual(this.response?.data?.length, expectedSize);
});

Then('response has a {int} Http status', function (expectedStatus) {
  assert.strictEqual(this.response.status, expectedStatus);
});