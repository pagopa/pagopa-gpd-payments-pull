import http from 'k6/http';
const subKey = `${__ENV.OCP_APIM_SUBSCRIPTION_KEY}`;

export function postToGPD(endpoint, body) {
  let headers = {
    'Ocp-Apim-Subscription-Key': subKey,
    "Content-Type": "application/json"
  };

  return http.post(endpoint, JSON.stringify(body), { headers, responseType: "text" });
}

export function deleteToGPD(endpoint) {
  let headers = {
    'Ocp-Apim-Subscription-Key': subKey,
    "Content-Type": "application/json"
  };

  return http.del(endpoint, null, { headers, responseType: "text" });
}