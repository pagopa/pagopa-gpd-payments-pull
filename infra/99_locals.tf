locals {
  product = "${var.prefix}-${var.env_short}"

  apim = {
    name                   = "${local.product}-apim"
    rg                     = "${local.product}-api-rg"
    gpd_payments_pull_product_id = "gpd-payments-pull"
  }
}

