package test.com
import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.gateway.ip.core.customdev.processor.MessageImpl
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class UnitTest {
    // Global Variables
    private final static String TEST_PROGRAM = './src/test/com/ParseURLParameter02.groovy'
    private static String testURL = 'https://test.bsn.neo.ondemand.com/http/hello?client=NPL&system_id=e6975e7a-4c7f-11ea-b77f-2e728ce88125&scope=COMPANY&start_date=2020-01-02&redirect_uri=http://remote.com.au/Callback&category='
    private static Message msg

    static void main(String[] args) {
        Initialization()
    //GIVEN
        SetHeaders()
        SetBody()
    //WHEN
        ExecuteTest()
    //THEN
        //UnitTestCases()
        DisplayResultToConsole()
    }

    /*----------------------------------Unit Test----------------------------------------*/
    private static void UnitTestCases(){
        def jsonSlurper = new JsonSlurper()
        def payload = jsonSlurper.parseText(msg.getBody());

        UT_ParemterNameIsCorrect(payload)
        UT_HTTPURLIsCorrect(payload)
    }
    private static void UT_HTTPURLIsCorrect(payload){
        String uttpurl = 'https://test.bsn.neo.ondemand.com/http/hello'
        assert payload.find{it.key == 'CamelHttpUrl'}.value== uttpurl
    }
    private static void UT_ParemterNameIsCorrect(payload){
        Set parameterList = ["client", "system_id", "start_date", "redirect_uri","category"]
        int i
        payload.each {
            it ->
                if(parameterList.contains(it.key)) {
                    i++
                }
        }
        assert i == 5
    }

    /*---------------------------------Utility Methods-----------------------------------------*/
    private static void DisplayResultToConsole(){
        // Print Headers
        msg.getHeaders().each {
            println it
        }
        // Print body
        println msg.getBody()
    }
    private static void ExecuteTest(){
        GroovyShell shell = new GroovyShell()
        def script = shell.parse(new File(TEST_PROGRAM))
        script.processData(msg)
    }
    private static void SetBody(){
        def payload = [:]
        msg.setBody(JsonOutput.toJson(payload))
    }
    private static void SetHeaders(){
        // Parse URL
        def urlComponents = [:]
        (this.testURL =~ /([^?]*)+/)[0..-1].eachWithIndex{
            it,i->
                switch(i) {
                    case 0:
                        urlComponents [ 'CamelHttpUrl' ] = it [ 0 ];
                        break;
                    case 2:
                        urlComponents [ 'CamelHttpQuery' ] = it [ 0 ];
                        break;
                }
        }
        // Set URL parameters
        msg.setHeader('CamelHttpUrl',urlComponents.CamelHttpUrl)
        msg.setHeader('CamelHttpQuery', urlComponents.CamelHttpQuery)

        //Set your own header parameters for testing
    }
    private static void Initialization(){
        msg = new MessageImpl()
    }
}