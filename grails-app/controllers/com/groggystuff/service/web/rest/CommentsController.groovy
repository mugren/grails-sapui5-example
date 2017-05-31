package com.groggystuff.service.web.rest

import com.groggystuff.controller.RestControllerTrait
import com.groggystuff.discuss.CommentService
import com.groggystuff.discuss.DiscussEntity
import grails.converters.JSON
import grails.core.GrailsApplication
import grails.plugin.springsecurity.SpringSecurityService
import org.grails.web.json.JSONObject

import java.text.ParseException

class CommentsController implements RestControllerTrait {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [post: ["PUT", "POST"], remove: ["POST", "DELETE"]]

    GrailsApplication grailsApplication
    CommentService commentService
    SpringSecurityService springSecurityService

    private static final String packagePrefix = "com.groggystuff.office."

    private DiscussEntity getDomainInstance(String className, String domainId) {
        Class clazz = grailsApplication.getArtefact('Domain', packagePrefix + className.capitalize())?.clazz
        if (clazz != null) {
            Long id = null
            if (domainId != null) {
                try {
                    id = Long.parseLong(domainId)
                    DiscussEntity domainInstance = clazz.get(id)
                    return domainInstance
                } catch (ParseException ex) {
                    log.error(ex.message, ex.fillInStackTrace())
                    return null
                }
            }
        }
        return null
    }

    private String extractComment() {
        String jsonString = request.inputStream.getText('UTF-8')
        JSONObject jsonObject = (JSONObject) JSON.parse(jsonString)
        String commentText = null
        jsonObject.entrySet().each {
            if (it.key == 'commentText') {
                commentText = it.value
            }
        }
        return commentText
    }

    /**
     * Post comment via service
     *
     *
     * JSON: "{commentText:'Some weird comment'}"
     *
     * @return
     */
    def post() {
        if (params.className == null || params.id == null) {
            badRequest()
            return
        }
        doPost(params.className as String, params.id as String)
    }

    protected void doPost(String className, String id) {
        DiscussEntity domainInstance = getDomainInstance(className, id)
        if (domainInstance == null) {
            badRequest()
            return
        }

        String comment = extractComment()
        if (comment == null) {
            messageClient("Comment is missing", 'default.comment.missing.message', 400)
            return
        }

        def user = springSecurityService?.principal
        def (message, status) = commentService.postComment(domainInstance, comment, (String) user?.username)
        messageClient(message, null, status)
    }

    /**
     * Remove comment via service
     *
     *
     * @return
     */
    def remove() {
        if (params.className == null || params.id == null) {
            badRequest()
            return
        }
        doRemove(params.className as String, params.id as String, params.commentId as String)
    }

    protected void doRemove(String className, String id, String commentId) {
        DiscussEntity domainInstance = getDomainInstance(className, id)

        if (domainInstance == null) {
            notFound()
            return
        }

        def (message, status) = commentService.deleteComment(domainInstance?.discussion, commentId)
        messageClient(message, null, status)
    }

    /**
     * List comments
     *
     *
     * @return
     */
    def get() {
        if (params.className == null || params.id == null) {
            badRequest()
            return
        }
        doGet(params.className as String, params.id as String)
    }

    protected void doGet(String className, String id) {
        DiscussEntity domainInstance = getDomainInstance(className, id)

        if (domainInstance == null) {
            notFound()
            return
        }

        def model = [:]
        def (message, status) = commentService.prepareComments(domainInstance, model, params)
        def renderObject = [message: message, status: status, comments: model.get('discussion')]
        respond renderObject
    }

    /**
     * List ony comments text, date and creator
     *
     * @return
     */
    def list() {
        if (params.className == null || params.id == null) {
            badRequest()
            return
        }
        doList(params.className as String, params.id as String)
    }

    protected def doList(String className, String id) {
        DiscussEntity domainInstance = getDomainInstance(className, id)

        if (domainInstance == null) {
            notFound()
            return
        }

        def model = [:]
        commentService.prepareComments(domainInstance, model, params)
        def discussion = model.get('discussion')
        [comments: discussion, count: discussion?.size() ?: 0]
    }
}
