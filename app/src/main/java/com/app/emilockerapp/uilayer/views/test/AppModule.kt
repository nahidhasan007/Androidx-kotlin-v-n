package com.app.emilockerapp.uilayer.views.test
import org.koin.dsl.module

val appModule = module {
   single { Introduction() }
   single { Occupation() }
   factory { TestClass(get(), get()) }
}