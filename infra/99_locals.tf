locals {
  product = "${var.prefix}-${var.env_short}"

  apim = {
    name                   = "${local.product}-apim"
    rg                     = "${local.product}-api-rg"
    bo_external_product_psp_id = "selfcare-bo-external-psp"
    bo_external_product_ec_id = "selfcare-bo-external-ec"
    bo_helpdesk_product_id = "selfcare-bo-helpdesk"
  }
}

