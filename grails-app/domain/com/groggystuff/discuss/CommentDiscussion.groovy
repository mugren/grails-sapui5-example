package com.groggystuff.discuss

class CommentDiscussion {

    String creator

    Date dateCreated
    Date lastUpdated

    List<Comment> comments

    String pinnedToDomain
    Long pinnedToId

    static hasMany = [comments: Comment]
    static mappedBy = [comments: "commentDiscussion"]

    static constraints = {
        comments nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
        pinnedToDomain nullable: true
        pinnedToId nullable: true
        creator nullable: true
    }

}
