package com.bitwiserain.remindme

import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestCoroutineExtension::class)
interface CoroutineTest {
    var testCoroutineScope: TestCoroutineScope
    var testCoroutineDispatcher: TestCoroutineDispatcher
}
