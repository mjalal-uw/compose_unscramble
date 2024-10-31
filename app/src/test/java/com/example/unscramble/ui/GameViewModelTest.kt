package com.example.unscramble.ui

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Test


class GameViewModelTest {

    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {

        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord =
            getUnscrambledWord(scrambledWord = currentGameUiState.currentScrambledWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
        assertFalse(currentGameUiState.isGuessedWordWrong)

    }

    @Test
    fun gameViewModel_WrongWordGuessed_ScoreNotUpdatedAndErrorFlagSet() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord =
            getUnscrambledWord(currentGameUiState.currentScrambledWord)
        val incorrectGuess = correctPlayerWord + "x"
        viewModel.updateUserGuess(incorrectGuess)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        assertNotEquals(correctPlayerWord, incorrectGuess)
        assertTrue(currentGameUiState.isGuessedWordWrong)
        assertEquals(SCORE_AFTER_FIRST_INCORRECT_ANSWER, currentGameUiState.score)

    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val currentUiState = viewModel.uiState.value
        val unScrambledWord = getUnscrambledWord(currentUiState.currentScrambledWord)
        assertNotEquals(unScrambledWord, currentUiState.currentScrambledWord)
        assertEquals(0, currentUiState.score)
        assertEquals(1, currentUiState.currentWordCount)
        assertFalse(currentUiState.isGameOver)
        assertFalse(currentUiState.isGuessedWordWrong)

    }

    @Test
    fun gameViewModel_AllWordsGuessedCorrectly_GameEnded() {
        var currentUiState = viewModel.uiState.value
        var expectedScore = 0
        repeat(MAX_NO_OF_WORDS) {
            val correctGuess = getUnscrambledWord(currentUiState.currentScrambledWord)
            viewModel.updateUserGuess(correctGuess)
            viewModel.checkUserGuess()
            currentUiState = viewModel.uiState.value
            expectedScore += SCORE_INCREASE


            assertEquals(expectedScore, currentUiState.score)
        }

        assertTrue(currentUiState.isGameOver)
        assertFalse(currentUiState.isGuessedWordWrong)
        assertEquals(MAX_NO_OF_WORDS, currentUiState.currentWordCount)


    }

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = 20
        private const val SCORE_AFTER_FIRST_INCORRECT_ANSWER = 0
    }

}