package com.groggystuff.controller

import grails.converters.JSON

import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.OK

trait RestControllerTrait {

    void badRequest() {
        request.withFormat {
            form multipartForm {
                flash.message = g.message(code: 'default.badRequest.message', default: "Bad request")
                redirect action: "index", method: "GET"
            }
            '*' { render status: BAD_REQUEST }
        }
    }

    void notFound() {
        if (request?.xhr) {
            render(["NOT_FOUND": true, "code": 404] as JSON)
        }
    }

    void messageClient(String message, String code, int status = OK.value()) {
        String messageTxt = message
        if (code != null) {
            messageTxt = g.message(code: code, default: message)
        }
        respond text: messageTxt, status: status
    }
}