import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject

import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import internal.GlobalVariable as GlobalVariable

import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

import com.kms.katalon.core.webui.driver.DriverFactory

class BasicListeners {

    // Flag penanda apakah sedang dijalankan dalam suite
    static boolean isSuiteMode = false
    // Daftar test suite yang mau dikecualikan
    static List<String> excludedSuites = [
        "Test Suites/Detail books one by one"
    ]

    @BeforeTestSuite
    def beforeSuite(TestSuiteContext testSuiteContext) {
        println("=== BeforeTestSuite: " + testSuiteContext.getTestSuiteId())

        if (excludedSuites.contains(testSuiteContext.getTestSuiteId())) {
            println("⚠️ Suite ini termasuk excluded, jalan seperti standalone")
            isSuiteMode = false
        } else {
            isSuiteMode = true
            WebUI.openBrowser('')
            WebUI.navigateToUrl(GlobalVariable.baseUrl)
        }
    }

    @AfterTestSuite
    def afterSuite(TestSuiteContext testSuiteContext) {
        println("=== AfterTestSuite: " + testSuiteContext.getTestSuiteId())

        if (isSuiteMode && DriverFactory.getWebDriver() != null) {
            WebUI.closeBrowser()
        }

        isSuiteMode = false
    }

    @BeforeTestCase
    def beforeCase(TestCaseContext testCaseContext) {
        if (!isSuiteMode) { // kalau standalone atau excluded suite
            println("=== BeforeTestCase (standalone/excluded): " + testCaseContext.getTestCaseId())
            WebUI.openBrowser('')
            WebUI.navigateToUrl(GlobalVariable.baseUrl)
        } else {
            println("=== BeforeTestCase (suite): " + testCaseContext.getTestCaseId())
        }
    }

    @AfterTestCase
    def afterCase(TestCaseContext testCaseContext) {
        println("=== AfterTestCase: " + testCaseContext.getTestCaseId() +
                " | Status: " + testCaseContext.getTestCaseStatus())

        if (!isSuiteMode) { // standalone atau excluded, tutup browser tiap case
            if (DriverFactory.getWebDriver() != null) {
                WebUI.closeBrowser()
            }
        }
    }
}