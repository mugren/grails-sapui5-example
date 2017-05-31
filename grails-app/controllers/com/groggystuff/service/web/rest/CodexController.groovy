package com.groggystuff.service.web.rest

import com.groggystuff.property.NameProperty
import com.groggystuff.annotation.Codice
import com.groggystuff.controller.RestControllerTrait
import grails.core.GrailsApplication

import java.lang.annotation.Annotation
import java.text.ParseException

class CodexController implements RestControllerTrait {

    GrailsApplication grailsApplication

    private static final String packagePrefix = "com.groggystuff.office."

    class Item {
        Long id
        String value
    }

    /**
     * request format:
     * /codex/get/{className}/{id}?
     *
     * for REST service:
     * /api/codex/get/{className}/{id}?
     *
     * @return JSON (show or list)
     */
    def get() {
        if (params.className == null) {
            badRequest()
            return
        }
        Long id = null
        if (params.id != null) {
            try {
                id = Long.parseLong(params.id)
            } catch (ParseException ex) {
                log.error(ex.message, ex.fillInStackTrace())
                badRequest()
                return
            }
        }
        doGet(params.className as String, id)
    }

    protected void doGet(String className, Long id) {
        Class clazz = grailsApplication.getArtefact("Domain", packagePrefix + className)?.clazz
        if (clazz != null) {
            Annotation annotation = clazz.getAnnotation(Codice.class)
            String fieldName = annotation.fieldName() ?: 'name'
            if (id != null) {
                // show
                def object = clazz.get(id)
                render view: 'show', model: [(NameProperty.DOMAIN_ID): id, className: clazz.name, fieldValue: object?."${fieldName}"]
                return
            } else {
                // list
                def list = clazz.list(params)
                def modList = list.collect {
                    new Item(id: it.id, value: it."${fieldName}")
                }
                render view: 'list', model: [className                           : clazz.name, (NameProperty.DOMAIN_INSTANCE_LIST): modList,
                                             (NameProperty.DOMAIN_INSTANCE_COUNT): clazz.count()]
                return
            }
        }
        badRequest()
    }

    /**
     * request format:
     * /codex/getByField/{className}/{fieldValue}*
     * for REST service:
     * /api/codex/getByField/{className}/{fieldValue}*
     * The mapping of the fieldValue with the corresponding fieldName depends
     * of the @Codice annotation applied to the Class associated with the className
     *
     * @return JSON (codex id)
     */
    def getByField() {
        if (params.className == null || params.id == null) {
            badRequest()
            return
        }
        doGetByField(params.className as String, params.id as String)
    }

    protected void doGetByField(String className, String fieldValue) {
        if (fieldValue != null) {
            Class clazz = grailsApplication.getArtefact("Domain", packagePrefix + className)?.clazz
            if (clazz != null) {
                Annotation annotation = clazz.getAnnotation(Codice.class)
                String fieldName = annotation.fieldName() ?: 'name'
                def object = clazz.findWhere((fieldName): fieldValue)
                if (object == null) {
                    notFound()
                    return
                }
                render view: 'showId', model: [(NameProperty.DOMAIN_ID): object.id, className: clazz.name, fieldName: fieldName]
                return
            }
        }
        badRequest()
    }

}
