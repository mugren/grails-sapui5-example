package com.groggystuff.service.web.rest

import com.groggystuff.controller.RestControllerTrait
import com.groggystuff.property.NameProperty
import grails.transaction.Transactional
import grails.web.http.HttpHeaders

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class EnhancedRestfulController<T> extends RestServiceOperation implements RestControllerTrait {

    static responseFormats = ['json', 'xml']

    EnhancedRestfulController(Class resource) {
        super(resource)
    }

    protected void doCreate() {
        if (handleReadOnly()) {
            return
        }
        respond createResource()
    }

    protected def doList() {
        def list = resource.list(params)
        int count = list.size()
        [(NameProperty.DOMAIN_INSTANCE_LIST): list, (NameProperty.DOMAIN_INSTANCE_COUNT): count]
    }

    protected void doShow(Long id) {
        def instance = queryForResource(id)
        if (instance == null) {
            notFound()
            return
        }

        respond instance
    }

    protected def doGet(Long id) {
        def instance = queryForResource(id)
        if (instance == null) {
            notFound()
            return
        }

        [(NameProperty.DOMAIN_INSTANCE): instance]
    }

    protected void doEdit(Long id) {
        if (handleReadOnly()) {
            return
        }
        def instance = queryForResource(id)
        if (instance == null) {
            notFound()
            return
        }

        respond instance
    }

    @Transactional
    protected void doSave() {
        def user = springSecurityService?.principal

        if (handleReadOnly()) {
            return
        }
        def instance = createResource()

        instance.creator = user?.username

        instance.validate()
        if (instance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond instance.errors, view: 'create' // STATUS CODE 422
            return
        }

        saveResource instance

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [classMessageArg, instance.id])
                redirect instance
            }
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link(resource: controllerName, action: 'show', id: instance.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null))
                respond instance, [status: CREATED, view: 'show']
            }
        }
    }

    @Transactional
    protected void doUpdate(Long id) {
        def user = springSecurityService?.principal

        if (handleReadOnly()) {
            return
        }

        def instance = queryForResource(id)
        if (instance == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }


        bindData(instance, getObjectToBind(), [exclude: ['creator', 'dateCreated', 'lastUpdated']])

        if (instance.hasProperty('lastModifiedBy')) {
            instance.lastModifiedBy = user?.username
        }

        if (instance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond instance.errors, view: 'edit' // STATUS CODE 422
            return
        }

        updateResource instance
        request.withFormat {
            form multipartForm {
                if (!request.xhr) {
                    flash.message = message(code: 'default.updated.message', args: [classMessageArg, instance.id])
                    redirect instance
                    return
                }
                respond instance, [status: OK]
            }
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link(resource: controllerName, action: 'show', id: instance.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null))
                respond instance, [status: OK]
            }
        }
    }

    @Transactional
    protected void doDelete(Long id) {
        if (handleReadOnly()) {
            return
        }

        def instance = queryForResource(id)
        if (instance == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        instance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [classMessageArg, instance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT } // NO CONTENT STATUS CODE
        }
    }

}
