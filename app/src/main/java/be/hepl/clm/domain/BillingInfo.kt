package be.hepl.clm.domain

data class BillingInfo(
    val billingAddressStreet: String,
    val billingAddressNum: String,
    val billingAddressPc: String,
    val billingAddressCity: String,
)

data class ProductItem(
    val id: Int,
    val quantity: Int
)