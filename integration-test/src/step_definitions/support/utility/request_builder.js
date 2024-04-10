const {addDays, buildStringFromDate, makeIdNumber, makeIdMix,} = require("./helpers");

function buildDebtPositionDynamicData(gpdSessionBundle, iupdIn) {
    return {
        iupd: iupdIn,
        iuv1: makeIdNumber(17),
        iuv2: makeIdNumber(17),
        iuv3: makeIdNumber(17),
        iuvOK: process.env.iuv_ok,  // es. "11101751670642134"
        iuvKO: process.env.iuv_ko,  // es. "03163674189686371"
        iban: gpdSessionBundle.debtPosition.iban,
        dueDate: addDays(30),
        retentionDate: addDays(90),
        transferId1: '1',
        transferId2: '2',
        amount: 300.00,
        receiptId: makeIdMix(33),
        pspId: "60000000001",
        pspBrokerId: "60000000001",
        pspChannelId: "60000000001_01",
        pspName: "PSP Paolo",
        pspFiscalCode: "CF60000000006",
        idempotency: `60000000001_${makeIdNumber(6)}${makeIdMix(4)}`,
        applicationDate: buildStringFromDate(addDays(0)),
        transferDate: buildStringFromDate(addDays(1)),
        transferOtherCIFiscalCode: "01020304059"
    };
}

function buildUpdateDebtPositionRequest(debtPosition, payer) {
    return {
        iupd: debtPosition.iupd,
        type: "F",
        fiscalCode: payer.fiscalCode,
        fullName: payer.name,
        streetName: payer.streetName,
        civicNumber: payer.civicNumber,
        postalCode: payer.postalCode,
        city: payer.city,
        province: payer.province,
        region: payer.region,
        country: payer.country,
        email: payer.email,
        phone: payer.phone,
        companyName: payer.companyName + " - Edit",
        officeName: payer.officeName + " - Edit",
        switchToExpired: false,
        paymentOption: [
            {
                iuv: debtPosition.iuv1,
                amount: debtPosition.amount * 100,
                description: "Canone Unico Patrimoniale - SkyLab Inc. - Edit",
                isPartialPayment: false,
                dueDate: debtPosition.dueDate,
                retentionDate: debtPosition.retentionDate,
                fee: 0,
                transfer: [
                    {
                        idTransfer: debtPosition.transferId1,
                        amount: (debtPosition.amount * 100 / 3),
                        remittanceInformation: "Rata 1 Edit",
                        category: "9/0101108TS/",
                        iban: debtPosition.iban,
                    },
                    {
                        idTransfer: debtPosition.transferId2,
                        organizationFiscalCode: debtPosition.transferOtherCIFiscalCode,
                        amount: (debtPosition.amount * 100 / 3) * 2,
                        remittanceInformation: "Rata 2 Edit",
                        category: "9/0101108TS/",
                        iban: debtPosition.iban,
                    }
                ]
            }
        ]
    };
}

module.exports = {
    buildDebtPositionDynamicData,
    buildUpdateDebtPositionRequest
}
