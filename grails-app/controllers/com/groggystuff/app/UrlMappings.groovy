package com.groggystuff.app

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
                controller(validator: {
                    return (it in ['login', 'logout', 'tasks', 'application'])
                })
            }
        }

        "/api/$controller/$action?/$id?(.$format)?" {
            controller = $controller
            action = $action
            constraints {
                controller(validator: {
                    return (it in ['login', 'logout', 'tasks'])
                })
            }
        }

        "/codex/$action/$className/$id?(.$format)?" {
            controller = "codex"
            action = $action
        }

        "/api/codex/$action/$className/$id?(.$format)?" {
            controller = "codex"
            action = $action
        }

        "/comments/$action/$className/$id?(.$format)?" {
            controller = "comments"
            action = $action
        }

        "/api/comments/$action/$className/$id?(.$format)?" {
            controller = "comments"
            action = $action
        }

        "/"(view: '/ui/index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
