package ru.kotlin.senin

val testRequestData = RequestData("username", "password", "test-owner", "test-repository")

data class TestCommitInfo(
    val sha: String,
    val commit: CommitDetails,
    val author: UserInfo?,
    val files: List<Change>,
    val timeFromStart: Long,
)

data class TestCommitWithChanges(val timeFromStart: Long, val commit: CommitWithChanges)

const val COMMITS_REQUEST_DELAY = 1000L

val user1ShortInfo = AuthorInfo("Vasiliy Pupkin", "2020-12-08T21:30:48Z")
val user1Info = UserInfo("pupkin", 1, "User")

val user2ShortInfo = AuthorInfo("Ivanov Ivan", "2019-12-08T21:30:48Z")
val user2Info = UserInfo("ivanov", 2, "User")

val user3ShortInfo = AuthorInfo("github-classroom[bot]", "2020-11-08T21:30:48Z")
val user3Info = UserInfo("bot3", 3, "Bot")

val user4ShortInfo = AuthorInfo("Deleted user", "2016-11-08T21:30:48Z")

fun committedByUser1(message: String) = CommitDetails(user1ShortInfo, message)

fun committedByUser2(message: String) = CommitDetails(user2ShortInfo, message)

fun committedByUser3(message: String) = CommitDetails(user3ShortInfo, message)

fun committedByUser4(message: String) = CommitDetails(user4ShortInfo, message)

const val FILE_1 = "a.txt"
const val FILE_2 = "b.txt"
const val FILE_3 = "c.txt"

val changeFile1_1 = Change("1", FILE_1, 5, 5, 10) // user1   - 10
val changeFile2_1 = Change("2", FILE_2, 10, 0, 10) // user1   - 10
val changeFile3_1 = Change("3", FILE_3, 5, 0, 5) // user2 - 5
val changeFile1_2 = Change("4", FILE_1, 0, 5, 5) // bot - ignore
val changeFile2_2 = Change("5", FILE_2, 5, 5, 10) // deleted user - ignore
val changeFile3_2 = Change("6", FILE_3, 10, 0, 10) // user1   - 10
val changeFile1_3 = Change("7", FILE_1, 10, 5, 15) // user1   - 15
val changeFile2_3 = Change("8", FILE_2, 10, 10, 20) // user2 - 20
val changeFile2_4 = Change("9", FILE_2, 0, 10, 10) // user2 - 10

val testCommits =
    listOf(
        TestCommitInfo("1", committedByUser1("Commit 1"), user1Info, listOf(changeFile1_1, changeFile2_1), 400L),
        TestCommitInfo("2", committedByUser2("Commit 2"), user2Info, listOf(changeFile3_1), 600L),
        TestCommitInfo("3", committedByUser3("Commit by Bot"), user3Info, listOf(changeFile1_2), 800L),
        TestCommitInfo("4", committedByUser4("Old Commit"), null, listOf(changeFile2_2), 1000L),
        TestCommitInfo("5", committedByUser1("Commit 5"), user1Info, listOf(changeFile3_2, changeFile1_3), 1200L),
        TestCommitInfo("6", committedByUser2("Commit 6"), user2Info, listOf(changeFile2_3, changeFile2_4), 1400L),
    )

val testCommitsWithChanges =
    testCommits.map {
        TestCommitWithChanges(it.timeFromStart, CommitWithChanges(it.sha, it.author, it.files))
    }

val commits =
    testCommits.map {
        Commit(it.sha, it.commit, it.author)
    }

data class TestResults(val timeFromStart: Long, val login2Stat: Map<String, UserStatistics>)

val expectedFinalResults =
    TestResults(
//        COMMITS_REQUEST_DELAY + testCommits.maxOf { it.timeFromStart },
        COMMITS_REQUEST_DELAY + 3600,
        mapOf(
            user1Info.login to UserStatistics(2, sortedSetOf(FILE_1, FILE_2, FILE_3), 45),
            user2Info.login to UserStatistics(2, sortedSetOf(FILE_2, FILE_3), 35),
        ),
    )

// TODO: In your case progress may by different
val progressResults =
    listOf(
        TestResults(
            COMMITS_REQUEST_DELAY + 400L,
            mapOf(
                user1Info.login to UserStatistics(1, sortedSetOf(FILE_1, FILE_2), 20),
            ),
        ),
        TestResults(
            COMMITS_REQUEST_DELAY + 1000L,
            mapOf(
                user1Info.login to UserStatistics(1, sortedSetOf(FILE_1, FILE_2), 20),
                user2Info.login to UserStatistics(1, sortedSetOf(FILE_3), 5),
            ),
        ),
        TestResults(
            COMMITS_REQUEST_DELAY + 2200L,
            mapOf(
                user1Info.login to UserStatistics(2, sortedSetOf(FILE_1, FILE_2, FILE_3), 45),
                user2Info.login to UserStatistics(1, sortedSetOf(FILE_3), 5),
            ),
        ),
        expectedFinalResults,
    )
