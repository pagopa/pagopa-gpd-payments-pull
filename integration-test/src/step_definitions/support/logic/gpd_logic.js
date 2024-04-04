const {
    createDebtPosition,
    deleteDebtPosition,
} = require("../client/gpd_client");

const {
    buildDebtPositionDynamicData,
    buildCreateDebtPositionRequest
} = require("../utility/request_builders");

async function executeDebtPositionCreationAndPublication(bundle, idOrg, iupd, fiscalCode, dueDate, pullFlag) {
    bundle.organizationCode = idOrg;
    bundle.debtPosition = buildDebtPositionDynamicData(bundle, iupd);
    bundle.payer.fiscalCode = fiscalCode;
    bundle.debtPosition.dueDate = new Date(dueDate);
    //TODO: Add pullFlag property when defined on GPD
    return await createAndPublishDebtPosition(bundle.organizationCode,
        buildUpdateDebtPositionRequest(bundle.debtPosition, bundle.payer));
    bundle.responseToCheck = response;
}

async function executeDebtPositionDeletion(idOrg, iupd) {
    return await deleteDebtPosition(idOrg, iupd);
}

module.exports = {
    executeDebtPositionCreationAndPublication,
    executeDebtPositionDeletion
}