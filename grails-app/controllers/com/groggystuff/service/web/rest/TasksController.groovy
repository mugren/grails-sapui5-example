package com.groggystuff.service.web.rest

import com.groggystuff.office.Task
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TasksController extends EnhancedRestfulController<Task> {

    static responseFormats = ['json', 'xml']

    static allowedMethods = [save: "POST", update: ["PUT", "POST"], patch: "PATCH", delete: "DELETE"]


    TasksController() {
        super(Task)
    }

    TasksController(Class resource) {
        super(resource)
    }

    /**
     *
     * Gets a single resource with beautified JSON
     *
     * @param id The id of the resource
     * @return The rendered resource (as JSON) or a 404 if it doesn't exist
     */
    def get() {
        if (params.id == null) {
            badRequest()
            return
        }
        doGet(params.id as Long)
    }

    /**
     *
     * Displays a form to create a new resource
     */
    def create() {
        doCreate()
    }

    /**
     * Shows a single resource
     * @param id The id of the resource
     * @return The rendered resource (as JSON) or a 404 if it doesn't exist
     */
    def show() {
        if (params.id == null) {
            badRequest()
            return
        }
        doShow(params.id as Long)
    }


    /**
     *
     * Saves a resource and returns JSON of the new object
     */
    @Transactional
    def save() {
        doSave()
    }

    /**
     *
     * Returns JSON of the queried object with the values (same as show actually)
     *
     * @return
     */
    def edit() {
        if (params.id == null) {
            badRequest()
            return
        }
        doEdit(params.id as Long)
    }

    /**
     * Updates a resource for the given id
     * @param id
     */
    @Transactional
    def patch() {
        update()
    }

    /**
     *
     * Updates a resource for the given id
     * @param id
     *
     * returns JSON of the updated object
     */
    @Transactional
    def update() {
        if (params.id == null) {
            badRequest()
            return
        }
        doUpdate(params.id as Long)
    }

    /**
     *
     * Lists all resources with simple output format (id,name)
     *
     * @return A list of resources as JSON
     */
    def list() {
        doList()
    }

    /**
     *
     * Deletes a resource for the given id
     * @param id The id
     *
     * returns status code and no body
     */
    @Transactional
    def delete() {
        if (params.id == null) {
            badRequest()
            return
        }
        doDelete(params.id as Long)
    }

}
