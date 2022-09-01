package nishimura.bruce.scribbed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nishimura.bruce.scribbed.model.TicTacToeGameState
import nishimura.bruce.scribbed.model.exception.InvalidTicTacToeMoveException

class TicTacToeViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(TicTacToeGameState())
    val gameState: StateFlow<TicTacToeGameState> = _gameState

    private val _error = MutableSharedFlow<InvalidTicTacToeMoveException>()
    val error: SharedFlow<InvalidTicTacToeMoveException> = _error

    /**
     * Wipes the board so that a new game can be played :)
     */
    fun clear() {
        _gameState.value = TicTacToeGameState()
    }

    /**
     * Clicks a tile at given coordinates and updates gameState to reflect this.
     */
    fun clickTile(xCoord: Int, yCoord: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                _gameState.value = _gameState.value.withMove(xCoord, yCoord)
            } catch (e: InvalidTicTacToeMoveException) {
                _error.emit(e)
            }
        }
    }
}