package com.ferox.game.world.entity

import com.ferox.game.task.impl.TickableTask

fun Entity.event(task: TickableTask.() -> Unit) {
    repeatingTask {
        task.invoke(it)
    }
}
