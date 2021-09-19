package com.bitwiserain.remindme

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestInstancePostProcessor

class TestCoroutineExtension : TestInstancePostProcessor, BeforeAllCallback, AfterEachCallback, AfterAllCallback {
    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    override fun postProcessTestInstance(testInstance: Any, context: ExtensionContext) {
        (testInstance as? CoroutineTest)?.let {
            it.testCoroutineScope = testCoroutineScope
            it.testCoroutineDispatcher = testCoroutineDispatcher
        }
    }

    override fun beforeAll(context: ExtensionContext) {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    override fun afterEach(context: ExtensionContext) {
        testCoroutineScope.cleanupTestCoroutines()
    }

    override fun afterAll(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}

