package com.examples.kigaliride.utils

import android.content.Context

object DevicePrefs {

    private const val PREF_NAME = "kigali_ride_prefs"
    private const val KEY_CUSTOMER_PHONE = "customer_phone"
    private const val KEY_DRIVER_PHONE = "driver_phone"
    private const val KEY_DRIVER_PLATE = "driver_plate"

    fun saveCustomerPhone(context: Context, phone: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_CUSTOMER_PHONE, phone)
            .apply()
    }

    fun getCustomerPhone(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_CUSTOMER_PHONE, null)
    }

    fun saveDriver(context: Context, phone: String, plate: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_DRIVER_PHONE, phone)
            .putString(KEY_DRIVER_PLATE, plate)
            .apply()
    }

    fun getDriverPhone(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_DRIVER_PHONE, null)
    }

    fun getDriverPlate(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_DRIVER_PLATE, null)
    }

    private const val KEY_PASSCODE = "passcode"

    fun savePasscode(context: Context, passcode: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_PASSCODE, passcode)
            .apply()
    }

    fun getPasscode(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_PASSCODE, null)
    }
}