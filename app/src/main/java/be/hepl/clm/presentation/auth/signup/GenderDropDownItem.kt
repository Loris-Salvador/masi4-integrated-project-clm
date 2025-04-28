package be.hepl.clm.presentation.auth.signup

import androidx.compose.ui.graphics.vector.ImageVector
import be.hepl.clm.domain.Gender

data class GenderDropDownItem(
    val gender: Gender,
    val icon: ImageVector,
    val title: String
)
