package com.groggystuff.test

import com.groggystuff.rest.client.RestTokenClient
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 *
 */
@TestMixin(GrailsUnitTestMixin)
class RestServiceClientSpec extends Specification {

    RestTokenClient restTokenClient

    def setup() {
        restTokenClient = new RestTokenClient(
                protocol: "http",
                uri: "localhost",
                port: "8080",
                appContext: "",
                loginContext: "/api/login",
                username: "demoUser1",
                password: "pwd"
        )
    }

    def cleanup() {
    }

    void "index tasks"() {
        setup:
        def eventData = [:]
        def response = null
        eventData.put('path', '/api/tasks')
        eventData.put('methodType', 'GET')
        eventData.put('contentType', 'application/json')
        when:
        response = restTokenClient.executeCall(eventData)
        then:
        response != null
        println response
    }

    def "show task"() {
        setup:
        def eventData = [:]
        def response = null
        eventData.put('path', '/api/tasks/show/14')
        eventData.put('methodType', 'GET')
        eventData.put('contentType', 'application/json')
        when:
        response = restTokenClient.executeCall(eventData)
        then:
        response != null
        println response
    }

    def "save task"() {
        setup:
        String json = '{"assignedTo":["demoUser1"],"description":"REST SERVICE CREATE","dueDate":"2018-12-15T15:14:00Z","name":"REST SERVICE CREATE","startDate":"2017-06-02T15:14:00Z","taskStatus":{"id":1},"taskType":{"id":2}}'
        def response = null
        def eventData = [:]
        eventData.put('path', '/api/tasks/save')
        eventData.put('methodType', 'POST')
        eventData.put('contentType', 'application/json')
        eventData.put('data', json)
        when:
        response = restTokenClient.executeCall(eventData)
        then:
        response != null
        println response
    }

    def "update task"() {
        setup:
        String json = '{"description":"REST SERVICE UPDATE 2!!!","dateCreated":"2017-05-31T13:58:29Z"}'
        def response = null
        def eventData = [:]
        eventData.put('path', '/api/tasks/update/2')
        eventData.put('methodType', 'POST')
        eventData.put('contentType', 'application/json')
        eventData.put('data', json)
        when:
        response = restTokenClient.executeCall(eventData)
        then:
        response != null
        println response
    }

    def "delete task"() {
        setup:
        def response = null
        def eventData = [:]
        eventData.put('path', '/api/tasks/delete/2')
        eventData.put('methodType', 'DELETE')
        eventData.put('contentType', 'application/json')
        when:
        response = restTokenClient.executeCall(eventData)
        then:
        response != null
        println response
    }

    def "post comment on task"() {
        setup:
        String json = '{"commentText":"Hola muchachos"}'
        def response = null
        def eventData = [:]
        eventData.put('path', '/api/comments/post/task/1')
        eventData.put('methodType', 'POST')
        eventData.put('contentType', 'application/json')
        eventData.put('data', json)
        when:
        response = restTokenClient.executeCall(eventData)
        then:
        response != null
        println response
    }

    def "remove comment on task"() {
        setup:
        def response = null
        def eventData = [:]
        eventData.put('path', '/api/comments/remove/task/1?commentId=1')
        eventData.put('methodType', 'POST')
        eventData.put('contentType', 'application/json')
        when:
        response = restTokenClient.executeCall(eventData)
        then:
        response != null
        println response
    }

    def "list comments for task"() {
        setup:
        def response = null
        def eventData = [:]
        eventData.put('path', '/api/comments/get/task/1')
        eventData.put('methodType', 'POST')
        eventData.put('contentType', 'application/json')
        when:
        response = restTokenClient.executeCall(eventData)
        then:
        response != null
        println response
    }

    def "list simple comments for task"() {
        setup:
        def response = null
        def eventData = [:]
        eventData.put('path', '/api/comments/list/task/1')
        eventData.put('methodType', 'POST')
        eventData.put('contentType', 'application/json')
        when:
        response = restTokenClient.executeCall(eventData)
        then:
        response != null
        println response
    }

}