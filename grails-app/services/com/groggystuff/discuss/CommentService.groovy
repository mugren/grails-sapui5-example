package com.groggystuff.discuss

import grails.transaction.Transactional

import java.text.ParseException

@Transactional
class CommentService {

    def postComment(DiscussEntity domainInstance, String commentText, String username) {
        String message = "No discussion thread available"
        int status = 404
        if (domainInstance.hasProperty('discussion')) {
            try {
                Comment comment = new Comment()
                comment.creator = username ?: "anonymous"
                comment.text = commentText
                CommentDiscussion discussion = domainInstance?.discussion
                if (discussion == null) {
                    discussion = new CommentDiscussion()
                    discussion.pinnedToDomain = domainInstance?.class
                    discussion.pinnedToId = domainInstance?.id
                    discussion.creator = domainInstance?.creator
                    discussion.addToComments(comment)
                    discussion.save(flush: true)
                    domainInstance.discussion = discussion
                    domainInstance.save(flush: true)
                } else {
                    discussion.addToComments(comment)
                    discussion.save(flush: true, failOnError: true)
                }
                message = "Comment posted"
                status = 200
            }
            catch (Exception ex) {
                log.error ex.message, ex.fillInStackTrace()
                message = "Error occurred"
                status = 500
            }
        }
        return [message, status]
    }

    def deleteComment(CommentDiscussion discussion, String commentId) {
        String message = "Comment doesn't exist"
        int status = 404
        if (commentId != null) {
            Long id = null
            try {
                id = Long.parseLong(commentId)
            } catch (ParseException ex) {
                log.error(ex.message, ex.fillInStackTrace())
                message = "commentId in incorrect type"
                status = 500
            }
            if (id != null) {
                Comment comment = Comment.findByIdAndCommentDiscussion(id, discussion)
                if (comment != null) {
                    try {
                        comment.delete(flush: true)
                        message = 'Comment deleted'
                        status = 200
                    }
                    catch (Exception ex) {
                        log.error ex.message, ex.fillInStackTrace()
                        message = "Error occurred"
                        status = 500
                    }
                }
            }
        }
        return [message, status]
    }

    def prepareComments(DiscussEntity domainInstance, Map model, Map params) {
        String message = "No discussion thread available"
        int status = 404
        try {
            if (domainInstance.hasProperty('discussion')) {
                def discussion = domainInstance?.discussion
                if (discussion != null && discussion?.comments != null) {
                    List comments = discussion.comments
                    if (params?.sortComments != null) {
                        CommentComparator commentComparator = new CommentComparator(params.sortComments as String)
                        comments.sort(commentComparator)
                        model.put('discussion', comments)
                    } else {
                        CommentComparator commentComparator = new CommentComparator()
                        comments.sort(commentComparator)
                        model.put('discussion', comments)
                    }
                    message = 'Comments listed'
                    status = 200
                }
            }
        } catch (Exception ex) {
            log.error ex.message, ex.fillInStackTrace()
            message = "Error occurred"
            status = 500
        }
        return [message, status]
    }
}
