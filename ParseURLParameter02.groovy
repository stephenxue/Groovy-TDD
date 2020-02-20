package test.com
import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.*;

Message processData(Message message) {
    def params = [:]
    def urlParameters = message.getHeaders().get("CamelHttpQuery")
    // Parse all URL parameters into message payload
    urlParameters.split("&").each {
        it ->
            String[] pair = it.split("=")
            params[pair[0]] = (pair as List)[1]
    }

    // Parse all header parameters into message payload
    message.getHeaders().each {
        it ->
            params << it
    }
    message.setBody(JsonOutput.toJson(params))
    message.setHeader('Content-Type', 'application/json');
    return message
}

