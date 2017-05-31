package com.groggystuff.rest.client

import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import org.apache.http.protocol.HTTP
import org.grails.web.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RestTokenClient {

    private Logger _log = LoggerFactory.getLogger(getClass())

    protected Logger getLog() { _log }

    private String protocol
    private String uri
    private String port

    private String appContext
    private String loginContext
    private String validateContext = "/api/validate"

    private String username
    private String password

    private String token

    private String getUrl() {
        if (port == "80") {
            protocol + "://" + uri + appContext
        } else {
            protocol + "://" + uri + ":" + port + appContext
        }
    }

    private String getAuthUrl() {
        if (port == "80") {
            protocol + "://" + uri + appContext + loginContext
        } else {
            protocol + "://" + uri + ":" + port + appContext + loginContext
        }
    }

    private String getValidateUrl() {
        if (port == "80") {
            protocol + "://" + uri + appContext + validateContext
        } else {
            protocol + "://" + uri + ":" + port + appContext + validateContext
        }
    }

    private def authenticate() {

        HttpClient httpclient = HttpClientBuilder.create().build()
        HttpPost httppost = new HttpPost(getAuthUrl())
        JSONObject json = new JSONObject()

        // Add your data
        json.put("username", username)
        json.put("password", password)
        StringEntity se = new StringEntity(json.toString())
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"))
        se.setContentEncoding("UTF-8")
        httppost.setEntity(se)

        try {
            HttpResponse response = httpclient.execute(httppost)

            if (response != null) {
                StatusLine status = response.getStatusLine()

                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))

                String line = ""
                String data = ""
                while ((line = rd.readLine()) != null) {
                    data += line
                }

                if (status.getStatusCode() == 200) {
                    JSONObject userPreferences = new JSONObject(data)
                    String receievedToken = userPreferences.getString("access_token")
                    token = receievedToken
                }

            }
        }
        catch (ConnectException ex) {
            log.error "Connection refused", ex.fillInStackTrace()
        }

    }

    // test method
    private def validate() {
        HttpClient httpclient = HttpClientBuilder.create().build()
        HttpPost httppost = new HttpPost(getValidateUrl())
        httppost.setHeader(new BasicHeader("Accept", "application/json"))
        httppost.setHeader("Authorization", "Bearer " + token)
        try {
            HttpResponse response = httpclient.execute(httppost)
            if (response != null) {
                StatusLine status = response.getStatusLine()
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
                String line = ""
                String data = ""
                while ((line = rd.readLine()) != null) {
                    data += line
                }
            }
        }
        catch (ConnectException ex) {
            log.error "Connection refused", ex.fillInStackTrace()
        }
    }

    /**
     * GET, POST, PUT, DELETE
     * @param eventData
     */
    def executeCall(Object eventData) {
        // init
        String path = eventData?.path
        String methodType = eventData.methodType ?: 'POST'
        def data = eventData?.data ?: ""
        int attempt = eventData.attempt == null ? 3 : eventData.attempt
        String contentType = eventData?.contentType ?: null
        if (path == null) {
            log.error("No http path specified for publishing data")
            return "PATH_ERROR"
        }
        // check && execute
        if (token != null) {
            HttpClient httpclient = HttpClientBuilder.create().build()
            def httpMethod
            if (methodType.toUpperCase() == "POST") {
                httpMethod = (HttpPost) getHttpPostObject(path)
            } else if (methodType.toUpperCase() == "GET") {
                httpMethod = (HttpGet) getHttpGetObject(path)
            } else if (methodType.toUpperCase() == "DELETE") {
                httpMethod = (HttpDelete) getHttpDeleteObject(path)
            } else {
                log.error("Method type not implemented")
                return
            }

            if (contentType != null) {
                httpMethod.addHeader("Content-Type", contentType)
            }

            if (httpMethod instanceof HttpPost) {
                if (data != null) {
                    if (data instanceof String) {
                        StringEntity payload = new StringEntity(data)
                        payload.setContentEncoding('UTF-8')
                        if (contentType != null) {
                            payload.setContentType(new BasicHeader("Content-Type", contentType))
                        }
                        httpMethod.setEntity(payload)
                    } else if (data instanceof List) {
                        httpMethod.setEntity(new UrlEncodedFormEntity(data, "UTF-8"))
                    }
                }
            }

            HttpResponse response = httpclient.execute(httpMethod)

            if (response != null) {
                StatusLine status = response.getStatusLine()

                if (status.statusCode == 204) {
                    if (httpMethod instanceof HttpDelete) {
                        log.debug "Deleted"
                        return "DELETED"
                    } else {
                        log.debug "Deleted"
                        return "SUCCESS"
                    }
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))

                String line = ""
                String respData = ""
                while ((line = rd.readLine()) != null) {
                    respData += line
                }

                if (status.getStatusCode() == 200) {
                    log.debug "Successful publishing of data"
                    log.debug respData
                    return respData
                } else if (status.statusCode == 201) {
                    log.debug "CREATED"
                    log.debug(respData)
                    return respData
                } else if (status.statusCode == 400) {
                    log.debug "Bad Request"
                    log.debug(respData)
                    return respData
                } else if (status.getStatusCode() == 401) {
                    log.debug "${status.reasonPhrase}"
                    log.debug "Method was not allowed."
                    log.debug "Token has expired. Reauthenticate."
                    token = null
                    if (attempt != 0) {
                        authenticate()
                        attempt--
                        eventData.attempt = attempt
                        publishData(eventData)
                    } else {
                        log.debug("All attempts passed with errors")
                        return "RESPONSE_ERROR"
                    }
                } else if (status.statusCode == 403) {
                    log.debug("Forbidden")
                    log.debug(respData)
                    return respData
                } else if (status.getStatusCode() == 404) {
                    log.debug "${status.reasonPhrase}"
                    return "NOT_FOUND"
                } else if (status.getStatusCode() == 405) {
                    log.debug "${status.reasonPhrase}"
                    return "METHOD_NOT_ALLOWED"
                }
            } else {
                log.debug("No response received for method call")
                return "NO_RESPONSE"
            }
        } else {
            if (attempt != 0) {
                authenticate()
                attempt--
                eventData.attempt = attempt
                executeCall(eventData)
            } else {
                log.debug("All attempts passed no response")
                return "AUTHENTICATION_ERROR"
            }
        }
    }

    private HttpGet getHttpGetObject(String path) {
        HttpGet httpget = new HttpGet(getUrl() + path)
        httpget.setHeader(new BasicHeader("Accept", "application/json"))
        httpget.setHeader("Authorization", "Bearer " + token)
        httpget.setHeader("Accept-Charset", "UTF-8")
        return httpget
    }

    private HttpPost getHttpPostObject(String path) {
        HttpPost httppost = new HttpPost(getUrl() + path)
        httppost.setHeader(new BasicHeader("Accept", "application/json"))
        httppost.setHeader("Authorization", "Bearer " + token)
        httppost.setHeader("Accept-Charset", "UTF-8")
        return httppost
    }

    private HttpDelete getHttpDeleteObject(String path) {
        HttpDelete httpDelete = new HttpDelete(getUrl() + path)
        httpDelete.setHeader(new BasicHeader("Accept", "application/json"))
        httpDelete.setHeader("Authorization", "Bearer " + token)
        httpDelete.setHeader("Accept-Charset", "UTF-8")
        return httpDelete
    }

    String getUsername() {
        return username
    }

    void setUsername(String username) {
        this.username = username
    }

    String getPassword() {
        return password
    }

    void setPassword(String password) {
        this.password = password
    }

    String getLoginContext() {
        return loginContext
    }

    void setLoginContext(String loginContext) {
        this.loginContext = loginContext
    }

    void setProtocol(String protocol) {
        this.protocol = protocol
    }

    void setUri(String uri) {
        this.uri = uri
    }

    void setPort(String port) {
        this.port = port
    }

    void setAppContext(String appContext) {
        this.appContext = appContext
    }

    String getProtocol() {

        return protocol
    }

    String getUri() {
        return uri
    }

    String getPort() {
        return port
    }

    String getAppContext() {
        return appContext
    }


}