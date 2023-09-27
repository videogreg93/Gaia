package gaia.managers

import gaia.managers.events.EventInstance
import gaia.managers.events.EventListener


class EventManager {
    private val listeners = HashMap<String, ArrayList<EventListener<EventInstance>>>()
    private val toRemove = ArrayList<Pair<String, EventListener<*>>>()

    inline fun <reified T : EventInstance> subscribeTo(listener: EventListener<*>) {
        val id = T::class.simpleName ?: error("Cannot use event listener with abstract class")
        subscribeTo(id, listener)
    }

    fun subscribeTo(eventType: String, listener: EventListener<*>) {
        val list = listeners.getOrDefault(eventType, ArrayList())
        (listener as? EventListener<EventInstance>)?.let {
            list.add(it)
            listeners.set(eventType, list)
        }
    }

    inline fun <reified T : EventInstance> unsubscribe(listener: EventListener<*>) {
        val id = T::class.simpleName ?: error("Cannot use event listener with abstract class")
        unsubscribe(id, listener)
    }

    fun unsubscribe(eventType: String, listener: EventListener<*>) {
        toRemove.add(Pair(eventType, listener))
    }

    fun cleanup() {
        toRemove.forEach {
            val eventType = it.first
            val listener = it.second
            val list = listeners.getOrDefault(eventType, ArrayList())
            (listener as? EventListener<EventInstance>)?.let {
                list.remove(it)
                listeners.set(eventType, list)
            }
        }
        toRemove.clear()
    }

    fun sendEvent(event: EventInstance) {
        val list = ArrayList(listeners.getOrDefault(event.identifier, ArrayList()))
        list.forEach {
            it.onEvent(event)
        }
    }

}
