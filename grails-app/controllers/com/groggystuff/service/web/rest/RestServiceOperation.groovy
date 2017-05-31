package com.groggystuff.service.web.rest

import grails.rest.RestfulController

abstract class RestServiceOperation<T> extends RestfulController {

    def springSecurityService

    RestServiceOperation(Class resource) {
        super(resource)
    }

    protected abstract void doCreate()

    protected abstract void doEdit(Long id)

    protected abstract def doList()

    protected abstract def doGet(Long id)

    protected abstract void doShow(Long id)

    protected abstract void doSave()

    protected abstract void doUpdate(Long id)

    protected abstract void doDelete(Long id)

}