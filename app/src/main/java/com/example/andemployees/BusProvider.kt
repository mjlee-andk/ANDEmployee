package com.example.andemployees

import com.squareup.otto.Bus
import com.squareup.otto.ThreadEnforcer

object BusProvider {
    init{

    }
    private val bus = Bus(ThreadEnforcer.ANY)

    fun getInstance(): Bus{
        return bus
    }

}