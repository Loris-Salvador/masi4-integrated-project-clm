package be.hepl.clm.domain

data class PaymentInfo(
    val cardNumber: String,
    val customerBank: String,
    val communication: String = ""
)

data class BankTokenResponse(
    val bankToken: String
)

data class PurchaseRequest(
    val products: List<ProductItem>,
    val billingInfo: BillingInfo,
    val paymentInfo: PaymentInfo,
    var token: String = ""
)

data class BankTokenRequest(
    val bankToken: String,
    val customerBank: String,
    val numCardCustomer: String,
    val communication: String,
    val userToken: String
)

data class PaymentResponse(
    val serverResponse: String
)