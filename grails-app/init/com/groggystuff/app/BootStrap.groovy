package com.groggystuff.app

import com.groggystuff.office.Task
import com.groggystuff.office.TaskStatus
import com.groggystuff.office.TaskType
import com.groggystuff.security.Role
import com.groggystuff.security.User
import com.groggystuff.security.UserRole
import com.groggystuff.security.utils.UserHelperService

class BootStrap {

    UserHelperService userHelperService

    def init = { servletContext ->

        // Create users
        User demoUser1 = User.findByUsername("demoUser1") ?: new User(
                username: "demoUser1", password: "pwd", fullName: "Demosten User Primus").save(flush: true)
        User demoUser2 = User.findByUsername("demoUser2") ?: new User(
                username: "demoUser2", password: "pwd", fullName: "Demosten User Secundus").save(flush: true)
        User demoUser3 = User.findByUsername("demoUser3") ?: new User(
                username: "demoUser3", password: "pwd", fullName: "Demosten User Trois").save(flush: true)
        Role roleUser = Role.findByAuthority("ROLE_USER") ?: new Role(authority: "ROLE_USER").save(flush: true)
        UserRole.create(demoUser1, roleUser, true)
        UserRole.create(demoUser2, roleUser, true)
        UserRole.create(demoUser3, roleUser, true)
        userHelperService.putUser(demoUser1)
        userHelperService.putUser(demoUser2)
        userHelperService.putUser(demoUser3)

        // Create codices
        TaskStatus waiting = TaskStatus.findByStatusName("Waiting To Start") ?: new TaskStatus(statusName: "Waiting To Start").save(flush: true)
        TaskStatus inProgress = TaskStatus.findByStatusName("In Progress") ?: new TaskStatus(statusName: "In Progress").save(flush: true)
        TaskStatus closed = TaskStatus.findByStatusName("Closed") ?: new TaskStatus(statusName: "Closed").save(flush: true)

        TaskType bugFix = TaskType.findByTypeName("Bug Fix") ?: new TaskType(typeName: "Bug Fix").save(flush: true)
        TaskType newFeature = TaskType.findByTypeName("New Feature") ?: new TaskType(typeName: "New Feature").save(flush: true)
        TaskType testing = TaskType.findByTypeName("Testing") ?: new TaskType(typeName: "Testing").save(flush: true)

        for (int i = 0; i < 14; i++) {
            int j = i + 1
            if (i % 3 == 0) {
                Task.findByName("Task-${j}") ?:
                        new Task(name: "Task-${j}", description: "This is task number ${j}", startDate: new Date() + 3, dueDate: new Date() + 7,
                                creator: "demoUser1", assignedTo: ["demoUser2", "demoUser3"], taskStatus: waiting, taskType: bugFix).save(flush: true)
            }
            if (i % 3 == 1) {
                Task.findByName("Task-${j}") ?:
                        new Task(name: "Task-${j}", description: "This is task number ${j}", startDate: new Date() - 3, dueDate: new Date() + 7,
                                creator: "demoUser2", assignedTo: ["demoUser1", "demoUser3"], taskStatus: inProgress, taskType: newFeature).save(flush: true)
            }
            if (i % 3 == 2) {
                Task.findByName("Task-${j}") ?:
                        new Task(name: "Task-${j}", description: "This is task number ${j}", startDate: new Date() + 3, dueDate: new Date() + 7,
                                creator: "demoUser3", assignedTo: ["demoUser1", "demoUser2"], taskStatus: closed, taskType: testing).save(flush: true)
            }
        }
    }
    def destroy = {
    }
}
