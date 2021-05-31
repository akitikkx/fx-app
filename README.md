# FX App

`FX App` is a Jetpack-powered, MVVM `Kotlin` Single-Activity Android application that allows the calculate the current
 FX rate for two currency values as well as view through graphs, the past thirty-day history for FX Market API's
 default currency pairs - EURUSD, GBPUSD, and USDJPY as well as a paged list of the same historical data.
 
 ## Pre-requisites
 Before running app, you will need to acquire a FX Market API key from https://fxmarketapi.com/ and add this to the
 `gradle.properties` file:
 
 ```
 FxMarketApiKey=""
 ```
 
 Without this key, the app will output errors as it will not be able to connect to FX Market API and retrieve the
 historical data as well as the current FX rate when requested from the converter screen.
 
 ## Things to note
 The FX APP uses `WorkManager` to schedule two tasks and these are setup on app load: 
 
 - The list of currency pairs supported by FX Market API. These are cached into the Room database and retrieved 
 for the dropdowns displayed on the converter screen
 
 - The request for this project was to use the endpoint `https://fxmarketapi.com/apihistorical`. This endpoint 
 does not support date ranges but only a single date at a time. Because of this to get historical 
 data for the past thirty days, thirty separate requests are performed by the `RefreshHistoryWorker`, and 
 on app load this is noticeable.
 
 - `Paging 3` is used on the graph screen below the graphs to display all the historical data retrieved from
  FX Market API and cached for the default currencies.
  
 - The Android GraphView library https://github.com/jjoe64/GraphView used in this project has a significant impact on the UI especially in conjunction with Paging 3. This is noticeable most especially on emulator devices on the graph screen with delayed rendering.
 
 ## Architecture
 `FX APP` is built using Kotlin and the following Jetpack components:
 
 - ViewModel & LiveData 
 - Navigation
 - Paging 3
 - SavedStateHandle with custom AbstractSavedStateViewModelFactory
 - View Binding
 - Data Binding
 - Room
 - WorkManager
 
 and the following additional libraries:
 
 - Hilt
 - Retrofit & OkHttp
 - Glide
 - Material Design
 - Android GraphView
 
 ## Code and directory structure
 
 ```
 > adapters
   |_ CommonBindingAdapter.kt
   |_ ConverterBindingAdapter.kt
   |_ GraphBindingAdapter.kt
 > di
   |_ RepositoryModule.kt
   |_ RoomModule
 > domain
   |_ Conversion
   |_ Currency
   |_ History
 > network
   > models
     |_ NetworkConvertResponse.kt
     |_ NetworkCurrenciesResponse
     |_ NetworkHistoricalResponse
   |_ FXMarketConnectionInterceptor
   |_ FXMarketService.kt
 > repository
   |_ FXAppRepository
   |_ HistoryPagingSource
 > ui
   > converter
     |_ ConverterFragment
     |_ ConverterViewModel
   > graph
     |_ GraphFragment
     |_ GraphViewModel
     |_ HistoryLoadStateAdapter
     |_ HistoryPagingAdapter
 > util
   |_ DateUtil
> work
   |_ RefreshCurrenciesWorker
   |_ RefreshHistoryWorker
 |_ FxApplication
 |_ MainActivity.kt
 ```
 
 #### adapters
 All the binding adapters for the search and result screen/section as well as for loading images via Glide
 
 #### di
 All the related Dependency Injection classes such as modules for the Hilt component
 
 #### domain
 Contained here are data classes whose attributes match the preference of the application as compared to 
 what is received from the network. 
 
 #### network
 The FX Market API connection service
 
 #### network > models
 The data classes modelled against the responses from FX Market API
 
 #### repository
 All data to the app originates from the repository. For `FX APP`, the repository
 performs the network call through the connection service and emits the data which the viewModels listen for.
 The repository makes use of a paging source `HistoryPagingSource` to deliver pages for the history on request.
 
 ### work
 All the current worker classes that will be run in the background by `WorkManager`
 
 #### ui
 All the screens in the app separated into - landing and detail - and their respective fragments and viewModels
 
 #### FxApplication
 `@HiltAndroidApp` annotated Application class
 
 #### MainActivity
 The entry point for the app and the container for the `Navigation`'s `NavHostFragment`

## Screenshots
<img src="https://github.com/akitikkx/fx-app/blob/main/screenshots/fxapp_web_banner.png">

## License

MIT License

Copyright (c) 2021 Ahmed Tikiwa

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
