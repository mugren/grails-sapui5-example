package com.groggystuff.office

import com.groggystuff.discuss.DiscussEntity
import grails.databinding.BindUsing

class Task implements DiscussEntity {

    String name
    String description
    String taskTagId

    Date startDate
    Date dueDate

    Date dateCreated
    Date lastUpdated

    String creator
    String lastModifiedBy

    @BindUsing({ obj, source ->
        Set list = new HashSet()
        def data = source['assignedTo']
        if (data instanceof String &&
                (data != '' && data != 'null')) {
            list.add(data)
        } else if (data instanceof String[]) {
            data.each {
                list.add(it)
            }
        } else if (data instanceof Collection) {
            list.addAll(data)
        }
        return list
    })
    Set<String> assignedTo


    TaskStatus taskStatus
    TaskType taskType

    static constraints = {
        taskTagId nullable: true
        description nullable: true
        creator nullable: true
        assignedTo(nullable: false, minSize: 1)
        taskStatus(nullable: true)
        taskType(nullable: true)
        discussion nullable: true
        startDate nullable: true
        dueDate nullable: true
        lastUpdated nullable: true
        dateCreated nullable: true
        lastModifiedBy nullable: true
    }

    @Override
    String toString() {
        name
    }

    def afterInsert() {
        if (id != null) {
            taskTagId = "#" + id.toString()
        }
    }

}
