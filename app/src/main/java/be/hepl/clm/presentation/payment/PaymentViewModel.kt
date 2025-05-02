package be.hepl.clm.presentation.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.hepl.clm.data.purchase.PurchaseRepository
import be.hepl.clm.domain.BillingInfo
import be.hepl.clm.domain.CartManager
import be.hepl.clm.domain.PaymentInfo
import be.hepl.clm.domain.ProductItem
import be.hepl.clm.domain.PurchaseRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaymentUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    // Adresse de livraison
    val street: String = "",
    val number: String = "",
    val postalCode: String = "",
    val city: String = "",
    // Informations bancaires
    val cardNumber: String = "",
    val customerBank: String = "",
    val communication: String = ""
)

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val purchaseRepository: PurchaseRepository
) : ViewModel() {

    private val cartManager = CartManager.getInstance()

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    // Adresse de livraison
    fun updateStreet(street: String) {
        _uiState.value = _uiState.value.copy(street = street)
    }

    fun updateNumber(number: String) {
        _uiState.value = _uiState.value.copy(number = number)
    }

    fun updatePostalCode(postalCode: String) {
        _uiState.value = _uiState.value.copy(postalCode = postalCode)
    }

    fun updateCity(city: String) {
        _uiState.value = _uiState.value.copy(city = city)
    }

    // Informations bancaires
    fun updateCardNumber(cardNumber: String) {
        _uiState.value = _uiState.value.copy(cardNumber = cardNumber)
    }

    fun updateCustomerBank(customerBank: String) {
        _uiState.value = _uiState.value.copy(customerBank = customerBank)
    }

    fun updateCommunication(communication: String) {
        _uiState.value = _uiState.value.copy(communication = communication)
    }

    fun validateCart() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val currentState = _uiState.value

                // Vérifier que les champs obligatoires sont remplis
                if (currentState.cardNumber.isBlank() ||
                    currentState.customerBank.isBlank() ||
                    currentState.street.isBlank() ||
                    currentState.number.isBlank() ||
                    currentState.postalCode.isBlank() ||
                    currentState.city.isBlank()
                ) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Veuillez remplir tous les champs obligatoires"
                    )
                    return@launch
                }

                // Create billing info
                val billingInfo = BillingInfo(
                    billingAddressStreet = currentState.street,
                    billingAddressNum = currentState.number,
                    billingAddressPc = currentState.postalCode,
                    billingAddressCity = currentState.city,
                )

                // Create payment info
                val paymentInfo = PaymentInfo(
                    cardNumber = currentState.cardNumber,
                    customerBank = currentState.customerBank,
                    communication = currentState.communication
                )

                // Map cart items to product items
                val productItems = cartManager.items.value.map {
                    ProductItem(id = it.article.id, quantity = it.quantity)
                }

                // Create purchase request (token will be added by the repository)
                val purchaseRequest = PurchaseRequest(
                    products = productItems,
                    billingInfo = billingInfo,
                    paymentInfo = paymentInfo,
                    token = ""
                )

                val success = purchaseRepository.validateCart(purchaseRequest)

                if (success) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                    // Optionally clear cart after successful purchase
                    cartManager.clearCart()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "La validation du panier a échoué"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Une erreur est survenue"
                )
            }
        }
    }
}