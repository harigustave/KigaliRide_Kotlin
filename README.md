# KigaliRide
This project **KigaliRide** is for addressing the problem of urban mobility in Kigali City.
It helps riders looking for services in the following categories **Taxi Cars**, **Rent Cars**, **Relocation Cars** to instantly find nearest(2KM) available drivers.
No need for drivers and riders to go around in the roads to physically look for each other as it was being done by most users currently.

## MVP's Current Features
- Welcome screen
- Customer verification
- Choose ride service
- Available rides
- Contact driver bottom sheet
- Driver verification
- Driver dashboard
- High-accuracy GPS update for driver
- Backend integration with Railway API

## Project's Files (In respect to design patterns for smartphone dev)
- `MainActivity.kt` -> app entry point
- `ui/navigation/KigaliRideApp.kt` -> app navigation and snackbar handling
- `ui/viewmodel/AppViewModel.kt` -> UI state and backend calls
- `data/network/ApiService.kt` -> Retrofit endpoints
- `data/repository/KigaliRideRepository.kt` -> API call wrapper
- `ui/screens/*` -> all screen composables
