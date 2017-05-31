package com.groggystuff.security.utils

import com.groggystuff.security.User
import grails.transaction.Transactional

@Transactional
class UserHelperService {

    private static Map<String, User> users = new HashMap()

    void putUser(User user) {
        if (user != null) {
            users.put(user.username, user)
        }
    }

    static String printFullUsername(String username) {
        users.get(username)?.fullName ?: username
    }

    static String printFullUsernames(Set<String> users) {
        return render(users)
    }

    private static String render(lookupUsers) {
        StringBuilder render = new StringBuilder("")
        for (user in lookupUsers) {
            User object = users.get(user)
            if (object != null && object.fullName != null) {
                render.append(object.fullName)
            } else {
                render.append(user)
            }
            render.append(", ")
        }
        return render.substring(0, render.lastIndexOf(","))
    }
}
