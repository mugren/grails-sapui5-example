package com.groggystuff.discuss

class Comment {

    String creator

    String text
    Date dateCreated
    Date lastUpdated
    Integer points = 0
    Boolean isEdited = false

    CommentDiscussion replyThread

    static belongsTo = [commentDiscussion: CommentDiscussion]

    static mapping = {
        sort "dateCreated"
    }

    static constraints = {
        creator nullable: false
        text nullable: false, maxSize: 30000
        dateCreated nullable: true
        lastUpdated nullable: true
        points nullable: true
        replyThread nullable: true
        isEdited nullable: true
    }

}
