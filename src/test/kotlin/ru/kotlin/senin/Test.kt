package ru.kotlin.senin

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.junit5.JUnit5Asserter.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class Test {
    @Test
    fun testLoadResults() =
        runTest {
            var index = 0
            val startTime = currentTime
            loadResults(MockGithubService, testRequestData) { results, _ ->
                val expected = progressResults[index++]
                println(expected)
                val time = currentTime - startTime
                assertEquals("Expected result after ${expected.timeFromStart} ms:", expected.timeFromStart, time)
                assertEquals("Wrong result after $time:", expected.login2Stat, results)
            }
        }
}
