# K6 tests for Payments Pull project

[k6](https://k6.io/) is a load testing tool. 👀 See [here](https://k6.io/docs/get-started/installation/) to install it.

This is a set of [k6](https://k6.io) tests related to the Payments Pull initiative.

To invoke k6 test passing parameter use -e (or --env) flag:

```
-e MY_VARIABLE=MY_VALUE
```

Test the Payments Pull service:

```
k6 run --env VARS=local.environment.json --env TEST_TYPE=./test-types/load.json --env OCP_APIM_SUBSCRIPTION_KEY=<your-secret> payment_pull_service_test.js
```

where the mean of the environment variables is:

```json
  "environment": [
    {
      "env": "local",
      "paymentsPullServiceURIBasePath": "http://localhost:8080",
      "gpdURIBasePath": "https://api.dev.platform.pagopa.it/gpd/api/v1",
      "userTaxCode": "****************",
      "organizationTaxCode": "***********"
    }
  ]
```

`paymentsPullServiceURIBasePath`: Payments pull service base path

`gpdURIBasePath`: GPD service base path

`userTaxCode`: user's tax code used to create the debt positions

`organizationTaxCode`: CI tax code used to create the debt positions