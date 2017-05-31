package com.groggystuff.office

import com.groggystuff.annotation.Codice


@Codice(domainClass = Task, fieldName = 'statusName')
class TaskStatus {

    String statusName

    static constraints = {
        statusName unique: true
    }

    @Override
    String toString() {
        return statusName
    }

    // custom onDelete trigger
    def beforeDelete() {
        Task.withNewSession { session ->
            def tasks = Task.findAllByTaskStatus(this)
            for (Task t : tasks) {
                t.taskStatus = null
                t.save()
            }
            session.flush()
            session.clear()
        }
    }

}
