package com.example.shopify_app.features.signup.data.model.UpdateCustomer

data class UpdateCustomer ( val customer: UpdateCustomerModel)
data class UpdateCustomerModel (val note : String, val multipass_identifier : String)