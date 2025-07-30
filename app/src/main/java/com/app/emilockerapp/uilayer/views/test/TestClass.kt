package com.app.emilockerapp.uilayer.views.test

class TestClass(private val intro: Introduction, private val occupation: Occupation) {
    fun perform(): String = intro.sayAboutYourSelf() + occupation.yourOccupation()
}

class Introduction {
    fun sayAboutYourSelf(): String = "Hello i am Nahid Hasan\n"
}

class Occupation {
    fun yourOccupation(): String = "I am an Software Engineer(Mobile App)\n"
}