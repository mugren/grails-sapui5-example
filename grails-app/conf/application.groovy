import grails.plugin.springsecurity.*

// **** SPRING SECURITY ****
grails.plugin.springsecurity.active = true

grails.plugin.springsecurity.rejectIfNoRule = true
grails.plugin.springsecurity.securityConfigType = SecurityConfigType.InterceptUrlMap
grails.plugin.springsecurity.dao.hideUserNotFoundExceptions = false
grails.plugin.springsecurity.logout.postOnly = false
grails.plugin.springsecurity.sch.strategyName = "MODE_INHERITABLETHREADLOCAL"

grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.groggystuff.security.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.groggystuff.security.UserRole'
grails.plugin.springsecurity.authority.className = 'com.groggystuff.security.Role'

grails.plugin.springsecurity.filterChain.chainMap = [
        // rest auth chain
        [pattern: '/api/**',
         filters: 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'],
        // traditional auth chain
        [pattern: '/**',
         filters: 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter']
]

grails.plugin.springsecurity.securityConfigType = "InterceptUrlMap"
grails.plugin.springsecurity.interceptUrlMap = [
        [pattern: '/favicon.ico', access: ['permitAll']],
        [pattern: '/js/**', access: ['permitAll']],
        [pattern: '/css/**', access: ['permitAll']],
        [pattern: '/images/**', access: ['permitAll']],
        [pattern: '/login/**', access: ['permitAll']],
        [pattern: '/logout/**', access: ['permitAll']],
        [pattern: '/assets/**', access: ['permitAll']],
        [pattern: '/api/login', access: ['permitAll']],
        [pattern: '/api/validate', access: ['permitAll']],
        [pattern: '/api/logout', access: ['permitAll']],
        [pattern: '/dbconsole/**', access: ['permitAll']],
        [pattern: '/api/**', access: ['IS_AUTHENTICATED_REMEMBERED']],
        [pattern: '/tasks/**', access: ['IS_AUTHENTICATED_REMEMBERED']],
        [pattern: '/codex/**', access: ['IS_AUTHENTICATED_REMEMBERED']],
        [pattern: '/comments/**', access: ['IS_AUTHENTICATED_REMEMBERED']],
        [pattern: '/ui/**', access: ['IS_AUTHENTICATED_REMEMBERED']],
        [pattern: '/static/**', access: ['IS_AUTHENTICATED_REMEMBERED']],
        [pattern: '/', access: ['IS_AUTHENTICATED_REMEMBERED']]
]

// ++ SPRING REST SECURITY ++

grails.plugin.springsecurity.rest.login.active = true
grails.plugin.springsecurity.rest.login.endpointUrl = "/api/login"
grails.plugin.springsecurity.rest.login.failureStatusCode = 401
grails.plugin.springsecurity.rest.login.useJsonCredentials = true
grails.plugin.springsecurity.rest.login.usernamePropertyName = 'username'
grails.plugin.springsecurity.rest.login.passwordPropertyName = 'password'

grails.plugin.springsecurity.rest.token.validation.endpointUrl = "/api/validate"
//grails.plugin.springsecurity.rest.token.validation.enableAnonymousAccess = true

grails.plugin.springsecurity.rest.token.storage.jwt.useSignedJwt = true
grails.plugin.springsecurity.rest.token.storage.jwt.secret = 'Fcz5EDMJr9BPoBQpOoiyrQqNwxIlgfSL'
grails.plugin.springsecurity.rest.token.storage.jwt.expiration = 3600

// **** END OF SPRING SECURITY ****

