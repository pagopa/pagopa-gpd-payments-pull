export function buildDebtPositionComplex(iupd, organizationTaxCode, userTaxCode) {
    return {
        "iupd": iupd,
        "organizationFiscalCode": organizationTaxCode,
        "type": "F",
        "pull": true,
        "companyName": "EC Demo Pagamenti Pull Test",
        "fullName": "EC Demo Pagamenti Pull Test",
        "fiscalCode": userTaxCode,
        "officeName": null,
        "validityDate": null,
        "switchToExpired": false,
        "paymentOption": [
            {
                "iuv": makeIdNumber(17),
                "organizationFiscalCode": organizationTaxCode,
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
                        "organizationFiscalCode": organizationTaxCode,
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
                "organizationFiscalCode": organizationTaxCode,
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
                        "organizationFiscalCode": organizationTaxCode,
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
                "organizationFiscalCode": organizationTaxCode,
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
                        "organizationFiscalCode": organizationTaxCode,
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
                "organizationFiscalCode": organizationTaxCode,
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
                        "organizationFiscalCode": organizationTaxCode,
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
};

function makeIdNumber(length) {
    let result = '';
    const characters = '0123456789';
    const charactersLength = characters.length;
    for (let i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}