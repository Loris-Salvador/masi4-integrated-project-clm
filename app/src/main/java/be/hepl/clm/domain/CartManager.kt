package be.hepl.clm.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CartItem(
    val article: Article,
    val quantity: Int
)

class CartManager private constructor() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    private val _totalItems = MutableStateFlow(0)
    val totalItems: StateFlow<Int> = _totalItems.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    fun addItem(article: Article, quantity: Int = 1) {
        val currentItems = _items.value.toMutableList()
        val existingItem = currentItems.find { it.article.id == article.id }

        if (existingItem != null) {
            // Mettre à jour la quantité si l'article existe déjà
            val index = currentItems.indexOf(existingItem)
            val newQuantity = existingItem.quantity + quantity
            if (newQuantity <= article.stock.quantity) { // Vérification du stock
                currentItems[index] = existingItem.copy(quantity = newQuantity)
            }
        } else {
            // Ajouter un nouvel article
            if (quantity <= article.stock.quantity) { // Vérification du stock
                currentItems.add(CartItem(article, quantity))
            }
        }

        _items.value = currentItems
        updateTotals()
    }

    fun removeItem(articleId: Int) {
        _items.update { items ->
            items.filter { it.article.id != articleId }
        }
        updateTotals()
    }

    fun updateItemQuantity(articleId: Int, quantity: Int) {
        val currentItems = _items.value.toMutableList()
        val itemIndex = currentItems.indexOfFirst { it.article.id == articleId }

        if (itemIndex != -1) {
            val item = currentItems[itemIndex]
            if (quantity <= item.article.stock.quantity && quantity > 0) {
                currentItems[itemIndex] = item.copy(quantity = quantity)
                _items.value = currentItems
                updateTotals()
            }
        }
    }

    fun clearCart() {
        _items.value = emptyList()
        updateTotals()
    }

    private fun updateTotals() {
        val items = _items.value
        _totalItems.value = items.sumOf { it.quantity }
        _totalPrice.value = items.sumOf {
            val discountedPrice = it.article.price * (1 - it.article.promotion / 100.0)
            discountedPrice * it.quantity
        }
    }

    companion object {
        private var instance: CartManager? = null

        fun getInstance(): CartManager {
            return instance ?: synchronized(this) {
                instance ?: CartManager().also { instance = it }
            }
        }
    }
}