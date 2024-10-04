const {addDays, buildStringFromDate, makeIdNumber, makeIdMix,} = require("./helpers");

function buildDebtPositionOneOption(iupd, iuv, organizationCode, taxCode) {
    return {
        "iupd": iupd,
        "organizationFiscalCode": organizationCode,
        "type": "F",
        "pull": true,
        "companyName": "EC Demo Pagamenti Pull Test",
        "fullName": "EC Demo Pagamenti Pull Test",
        "fiscalCode": taxCode,
        "officeName": null,
        "validityDate": null,
        "switchToExpired": false,
        "paymentOption": [
            {
                "iuv": iuv,
                "organizationFiscalCode": organizationCode,
                "amount": 120,
                "description": "Test Pull - unica opzione",
                "isPartialPayment": false,
                "dueDate": "2024-10-30T23:59:59",
                "retentionDate": "2024-11-30T23:59:59",
                "paymentDate": null,
                "reportingDate": null,
                "paymentMethod": "CP",
                "pspCompany": null,
                "transfer": [
                    {
                        "organizationFiscalCode": organizationCode,
                        "companyName": "test",
                        "idTransfer": "1",
                        "amount": 120,
                        "remittanceInformation": "Test Pull",
                        "category": "9/0101108TS/",
                        "iban": "IT60X0542811101000000123456"
                    }
                ]
            }
        ]
    };
}

function buildDebtPositionComplex(iupd, organizationCode, taxCode) {
    return {
        "iupd": iupd,
        "organizationFiscalCode": organizationCode,
        "type": "F",
        "pull": true,
        "companyName": "EC Demo Pagamenti Pull Test",
        "fullName": "EC Demo Pagamenti Pull Test",
        "fiscalCode": taxCode,
        "officeName": null,
        "validityDate": null,
        "switchToExpired": false,
        "paymentOption": [
            {
                "iuv": makeIdNumber(17),
                "organizationFiscalCode": organizationCode,
                "amount": 120,
                "description": "Test Pull - opzione totale e piano rateale",
                "isPartialPayment": false,
                "dueDate": "2024-10-30T23:59:59",
                "retentionDate": "2024-11-30T23:59:59",
                "paymentDate": null,
                "reportingDate": null,
                "paymentMethod": "CP",
                "pspCompany": null,
                "transfer": [
                    {
                        "organizationFiscalCode": organizationCode,
                        "companyName": "test",
                        "idTransfer": "1",
                        "amount": 120,
                        "remittanceInformation": "Test Pull",
                        "category": "9/0101108TS/",
                        "iban": "IT60X0542811101000000123456"
                    }
                ]
            },
            {
                "iuv": makeIdNumber(17),
                "organizationFiscalCode": organizationCode,
                "amount": 100,
                "description": "Test Pull - opzione totale e piano rateale",
                "isPartialPayment": true,
                "dueDate": "2024-10-30T23:59:59",
                "retentionDate": "2024-10-30T23:59:59",
                "paymentDate": null,
                "reportingDate": null,
                "paymentMethod": "CP",
                "pspCompany": null,
                "transfer": [
                    {
                        "organizationFiscalCode": organizationCode,
                        "companyName": "test",
                        "idTransfer": "1",
                        "amount": 100,
                        "remittanceInformation": "Test Pull",
                        "category": "9/0101108TS/",
                        "iban": "IT60X0542811101000000123456"
                    }
                ]
            },
            {
                "iuv": makeIdNumber(17),
                "organizationFiscalCode": organizationCode,
                "amount": 200,
                "description": "Test Pull - opzione totale e piano rateale",
                "isPartialPayment": true,
                "dueDate": "2024-11-30T23:59:59",
                "retentionDate": "2024-11-30T23:59:59",
                "paymentDate": null,
                "reportingDate": null,
                "paymentMethod": "CP",
                "pspCompany": null,
                "transfer": [
                    {
                        "organizationFiscalCode": organizationCode,
                        "companyName": "test",
                        "idTransfer": "1",
                        "amount": 200,
                        "remittanceInformation": "Test Pull",
                        "category": "9/0101108TS/",
                        "iban": "IT60X0542811101000000123456"
                    }
                ]
            },
            {
                "iuv": makeIdNumber(17),
                "organizationFiscalCode": organizationCode,
                "amount": 300,
                "description": "Test Pull - opzione totale e piano rateale",
                "isPartialPayment": true,
                "dueDate": "2024-12-30T23:59:59",
                "retentionDate": "2024-12-30T23:59:59",
                "paymentDate": null,
                "reportingDate": null,
                "paymentMethod": "CP",
                "pspCompany": null,
                "transfer": [
                    {
                        "organizationFiscalCode": organizationCode,
                        "companyName": "test",
                        "idTransfer": "1",
                        "amount": 300,
                        "remittanceInformation": "Test Pull",
                        "category": "9/0101108TS/",
                        "iban": "IT60X0542811101000000123456"
                    }
                ]
            }
        ]
    };
}

function buildDebtPositionWithInstallments(iupd, organizationCode, taxCode) {
    return {
        "iupd": iupd,
        "organizationFiscalCode": organizationCode,
        "type": "F",
        "pull": true,
        "companyName": "EC Demo Pagamenti Pull Test",
        "fullName": "EC Demo Pagamenti Pull Test",
        "fiscalCode": taxCode,
        "officeName": null,
        "validityDate": null,
        "switchToExpired": false,
        "paymentOption": [
            {
                "iuv": makeIdNumber(17),
                "organizationFiscalCode": organizationCode,
                "amount": 100,
                "description": "Test Pull - piano rateale",
                "isPartialPayment": true,
                "dueDate": "2024-10-30T23:59:59",
                "retentionDate": "2024-10-30T23:59:59",
                "paymentDate": null,
                "reportingDate": null,
                "paymentMethod": "CP",
                "pspCompany": null,
                "transfer": [
                    {
                        "organizationFiscalCode": organizationCode,
                        "companyName": "test",
                        "idTransfer": "1",
                        "amount": 100,
                        "remittanceInformation": "Test Pull",
                        "category": "9/0101108TS/",
                        "iban": "IT60X0542811101000000123456"
                    }
                ]
            },
            {
                "iuv": makeIdNumber(17),
                "organizationFiscalCode": organizationCode,
                "amount": 200,
                "description": "Test Pull - piano rateale",
                "isPartialPayment": true,
                "dueDate": "2024-11-30T23:59:59",
                "retentionDate": "2024-11-30T23:59:59",
                "paymentDate": null,
                "reportingDate": null,
                "paymentMethod": "CP",
                "pspCompany": null,
                "transfer": [
                    {
                        "organizationFiscalCode": organizationCode,
                        "companyName": "test",
                        "idTransfer": "1",
                        "amount": 200,
                        "remittanceInformation": "Test Pull",
                        "category": "9/0101108TS/",
                        "iban": "IT60X0542811101000000123456"
                    }
                ]
            },
            {
                "iuv": makeIdNumber(17),
                "organizationFiscalCode": organizationCode,
                "amount": 300,
                "description": "Test Pull - piano rateale",
                "isPartialPayment": true,
                "dueDate": "2024-12-30T23:59:59",
                "retentionDate": "2024-12-30T23:59:59",
                "paymentDate": null,
                "reportingDate": null,
                "paymentMethod": "CP",
                "pspCompany": null,
                "transfer": [
                    {
                        "organizationFiscalCode": organizationCode,
                        "companyName": "test",
                        "idTransfer": "1",
                        "amount": 300,
                        "remittanceInformation": "Test Pull",
                        "category": "9/0101108TS/",
                        "iban": "IT60X0542811101000000123456"
                    }
                ]
            },
            {
                "iuv": makeIdNumber(17),
                "organizationFiscalCode": organizationCode,
                "amount": 400,
                "description": "Test Pull - piano rateale",
                "isPartialPayment": true,
                "dueDate": "2025-01-30T23:59:59",
                "retentionDate": "2025-01-30T23:59:59",
                "paymentDate": null,
                "reportingDate": null,
                "paymentMethod": "CP",
                "pspCompany": null,
                "transfer": [
                    {
                        "organizationFiscalCode": organizationCode,
                        "companyName": "test",
                        "idTransfer": "1",
                        "amount": 400,
                        "remittanceInformation": "Test Pull",
                        "category": "9/0101108TS/",
                        "iban": "IT60X0542811101000000123456"
                    }
                ]
            }
        ]
    };
}

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

function buildUpdateDebtPositionRequest(debtPosition, payer, pullFlag) {
    return {
        iupd: debtPosition.iupd,
        type: "F",
        pull: pullFlag,
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
    buildUpdateDebtPositionRequest,
    buildDebtPositionOneOption,
    buildDebtPositionComplex,
    buildDebtPositionWithInstallments
}
