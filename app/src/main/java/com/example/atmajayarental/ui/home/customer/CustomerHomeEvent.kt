package com.example.atmajayarental.ui.home.customer

import com.example.atmajayarental.ui.auth.AuthEvent

sealed class CustomerHomeEvent {
    object OnButtonPromoPressed : CustomerHomeEvent()
    object OnButtonDaftarMobilPressed : CustomerHomeEvent()
    object OnButtonProfilPressed : CustomerHomeEvent()
    object OnButtonTransaksiPressed : CustomerHomeEvent()
    object OnButtonLogoutPressed : CustomerHomeEvent()
}