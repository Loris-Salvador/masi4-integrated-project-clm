package be.hepl.clm.domain

// Ajout d'un modèle pour les informations de paiement
data class PaymentInfo(
    val cardNumber: String,
    val customerBank: String,
    val communication: String = ""
)

// Classe de réponse du serveur bancaire
data class BankTokenResponse(
    val bankToken: String
)

// Mise à jour du modèle PurchaseRequest pour inclure les informations de paiement
data class PurchaseRequest(
    val products: List<ProductItem>,
    val billingInfo: BillingInfo,
    val paymentInfo: PaymentInfo,
    var token: String = ""
)

// Classe pour la requête d'envoi du token bancaire
data class BankTokenRequest(
    val bankToken: String,
    val customerBank: String,
    val numCardCustomer: String,
    val communication: String,
    val userToken: String
)

// Classe pour la réponse de paiement
data class PaymentResponse(
    val serverResponse: String
)