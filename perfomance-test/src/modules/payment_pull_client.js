import http from 'k6/http';
const subKey = `${__ENV.OCP_APIM_SUBSCRIPTION_KEY}`;

export function getToService(endpoint, userTaxCode) {
  let url = endpoint;
  let headers = {
    'Ocp-Apim-Subscription-Key': subKey,
    'x-tax-code': userTaxCode,
    "Content-Type": "application/json"
  };
  let params = {
    page: 0,
    limit: 10
};

  return http.get(url, { headers, responseType: "text", params });
}