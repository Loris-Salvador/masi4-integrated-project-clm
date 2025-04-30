package be.hepl.clm.domain

data class Article(
    val id: Int,
    val name: String,
    val description: String,
    val category: Category,
    val price: Double,
    val promotion: Int,
    val pictures: List<Picture>,
    val categoryTax: CategoryTax,
    val stock: Stock,
)

data class Category(
    val id_category: Int,
    val category: String,
    val description: String
)

data class Picture(
    val id: Int,
    val path: String,
    val description: String
)

data class CategoryTax(
    val category: Int,
    val description: String,
    val percentage: Int
)

data class Stock(
    val stockId: Int,
    val location: String,
    val quantity: Int
)