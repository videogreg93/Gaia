package com.gregory.system.events

interface EventListener<in T: EventInstance> {
    fun onEvent(event: T)
}
