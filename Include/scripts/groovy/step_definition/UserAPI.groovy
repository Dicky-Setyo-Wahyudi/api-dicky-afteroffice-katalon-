package step_definition
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import groovy.json.JsonSlurper


class UserAPISteps {

	def response
	def requestBody = [:]

	@Given('The API endpoint is "(.*)"')
	def setAPIEndpoint(String url) {
		GlobalVariable.apiUrl = url
		KeywordUtil.logInfo("API URL set to: ${url}")
	}

	@And("The request body contains valid email and password")
	def prepareRegisterPayload() {
		requestBody = [
			email   : 'eve.holt@reqres.in',
			password: 'pistol'
		]
	}

	@When("I send a POST request to register")
	def sendRegisterRequest() {
		def request = findTestObject('API/PostRegister')
		request.setRestUrl(GlobalVariable.apiUrl)
		request.setBodyContent(new com.kms.katalon.core.testobject.impl.HttpTextBodyContent(
				new groovy.json.JsonBuilder(requestBody).toPrettyString(), "UTF-8", "application/json"))
		response = WS.sendRequest(request)
	}

	@When("I send a GET request to retrieve user list")
	def sendGetListUsersRequest() {
		def request = findTestObject('API/GetListUsers')
		request.setRestUrl(GlobalVariable.apiUrl)
		response = WS.sendRequest(request)
	}

	@When("I send a GET request to retrieve the user")
	def sendGetSingleUserRequest() {
		def request = findTestObject('API/GetSingleUser')
		request.setRestUrl(GlobalVariable.apiUrl)
		response = WS.sendRequest(request)
	}

	@Then("The response status code should be (\\d+)")
	def verifyStatusCode(int statusCode) {
		WS.verifyResponseStatusCode(response, statusCode)
		KeywordUtil.logInfo("Verified status code: ${statusCode}")
	}

	@And('The response should contain an "id" and a "token"')
	def verifyRegisterResponse() {
		WS.verifyElementPropertyValue(response, 'id', { it != null })
		WS.verifyElementPropertyValue(response, 'token', { it != null })
	}

	@And("The response should contain a list of users")
	def verifyListOfUsers() {
		def json = new JsonSlurper().parseText(response.getResponseText())
		assert json.data instanceof List && json.data.size() > 0
	}

	@And("The response should contain the user's first name and email")
	def verifyUserDetail() {
		WS.verifyElementPropertyValue(response, 'data.first_name', 'Janet')
		WS.verifyElementPropertyValue(response, 'data.email', 'janet.weaver@reqres.in')
	}
}