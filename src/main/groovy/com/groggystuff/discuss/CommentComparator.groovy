package com.groggystuff.discuss

class CommentComparator implements Comparator<Comment> {

    String order = "asc"

    CommentComparator() {

    }

    CommentComparator(String order) {
        this.order = order
    }

    @Override
    int compare(Comment first, Comment second) {
        Date firstDate = first.dateCreated
        Date secondDate = second.dateCreated
        if (firstDate == secondDate) {
            return 0
        }
        if (order == "asc") {
            if (firstDate.before(secondDate))
                return -1
            else
                return 1
        } else if (order == "desc") {
            if (firstDate.before(secondDate))
                return 1
            else
                return -1
        } else {
            if (firstDate.before(secondDate))
                return -1
            else
                return 1
        }
    }
}
