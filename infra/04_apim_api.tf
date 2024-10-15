locals {
  apim_gpd_payments_pull = {
    // GPD Payments Pull
    display_name          = "GPD Payments Pull"
    description           = "API for GPD Payments Pull"
    path                  = "pagopa-gpd-payments-pull"
    subscription_required = true
    service_url           = null
  }
  host              = "api.${var.apim_dns_zone_prefix}.${var.external_domain}"
  hostname          = var.hostname
}

##############
## Api Vers ##
##############

resource "azurerm_api_management_api_version_set" "api_gpd_payments_pull_api" {

  name                = "pagopa-${var.env_short}-gpd-payments-pull-api"
  resource_group_name = local.apim.rg
  api_management_name = local.apim.name
  display_name        = local.apim_gpd_payments_pull.display_name
  versioning_scheme   = "Segment"
}

##############
## OpenApi  ##
##############

module "apim_gpd_payments_pull_api_v1" {
  source = "git::https://github.com/pagopa/terraform-azurerm-v3.git//api_management_api?ref=v6.4.1"

  name                  = "pagopa-${var.env_short}-gpd-payments-pull-api"
  api_management_name   = local.apim.name
  resource_group_name   = local.apim.rg
  product_ids = concat([local.apim.gpd_payments_pull_product_id], [local.apim.gpd_payments_pull_product_test_id])
  subscription_required = local.apim_gpd_payments_pull.subscription_required
  version_set_id        = azurerm_api_management_api_version_set.api_gpd_payments_pull_api.id
  api_version           = "v1"

  description  = local.apim_gpd_payments_pull.description
  display_name = local.apim_gpd_payments_pull.display_name
  path         = local.apim_gpd_payments_pull.path
  protocols = ["https"]
  service_url  = local.apim_gpd_payments_pull.service_url

  content_format = "openapi"
  content_value = file("../openapi/openapi.json")

  xml_content = templatefile("./policy/_base_policy.xml", {
    hostname = local.hostname
  })

}