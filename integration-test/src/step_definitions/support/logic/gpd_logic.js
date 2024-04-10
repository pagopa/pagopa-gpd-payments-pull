const {
    createAndPublishDebtPosition,
    deleteDebtPosition,
} = require("../client/gpd_client");

const {
    buildDebtPositionDynamicData,
    buildUpdateDebtPositionRequest,
    buildCreateDebtPositionRequest
} = require("../utility/request_builder");


async function executeDebtPositionCreationAndPublication(bundle, idOrg, iupd, fiscalCode, dueDate, pullFlag) {
    bundle.organizationCode = idOrg;
    bundle.debtPosition = buildDebtPositionDynamicData(bundle, iupd);
    bundle.payer.fiscalCode = fiscalCode;
    bundle.debtPosition.dueDate = new Date(dueDate);
    bundle.debtPosition.dueDate.setFullYear(bundle.debtPosition.dueDate.getFullYear() + 1);
    bundle.debtPosition.dueDate.setDate(bundle.debtPosition.dueDate.getDate() + 1);
    bundle.debtPosition.retentionDate = bundle.debtPosition.dueDate;
    //TODO: Add pullFlag property when defined on GPD
    return await createAndPublishDebtPosition(bundle.organizationCode, buildUpdateDebtPositionRequest(bundle.debtPosition, bundle.payer));
}

async function executeDebtPositionDeletion(idOrg, iupd) {
    return await deleteDebtPosition(idOrg, iupd);
}

module.exports = {
    executeDebtPositionCreationAndPublication,
    executeDebtPositionDeletion
}
