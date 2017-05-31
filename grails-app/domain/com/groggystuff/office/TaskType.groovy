package com.groggystuff.office

import com.groggystuff.annotation.Codice

@Codice(domainClass = Task, fieldName = 'typeName')
class TaskType {

    String typeName

    static constraints = {
        typeName unique: true
    }

    @Override
    String toString() {
        return typeName
    }

    // custom onDelete trigger
    def beforeDelete() {
        Task.withNewSession { session ->
            def tasks = Task.findAllByTaskType(this)
            for (Task t : tasks) {
                t.taskType = null
                t.save()
            }
            session.flush()
            session.clear()
        }
    }
}
