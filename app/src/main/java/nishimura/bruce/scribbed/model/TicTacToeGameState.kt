package nishimura.bruce.scribbed.model

import nishimura.bruce.scribbed.model.exception.AlreadyDefinedSquareException
import nishimura.bruce.scribbed.model.exception.GameAlreadyCompleteException
import nishimura.bruce.scribbed.model.exception.InvalidTicTacToeMoveException
import nishimura.bruce.scribbed.model.exception.OutOfBoundsMoveException

/**
 * Represents the game state of a tic tac toe game
 */
data class TicTacToeGameState(
    val dimensions: Int = 3,
    val playerTurn: XOState = XOState.X,
    val board: TicTacToeBoard = TicTacToeBoardImpl(dimensions)
) {
    /**
     * @return a copy of the game state after the provided move is executed
     * @throws InvalidTicTacToeMoveException if move is invalid
     */
    @Throws(InvalidTicTacToeMoveException::class)
    fun withMove(xCoord: Int, yCoord: Int): TicTacToeGameState {
        if (gameStatus != TicTacToeGameStatus.IN_PROGRESS)
            throw GameAlreadyCompleteException()
        val newBoard = board.withMove(xCoord, yCoord, playerTurn)
        val newTurn = if (playerTurn == XOState.X) XOState.O else XOState.X
        return copy(board = newBoard, playerTurn = newTurn)
    }

    val gameStatus by lazy { board.getGameStatus() }
}

private data class TicTacToeBoardImpl(
    private val dimensions: Int,
    private val boardState: List<XOState> = MutableList(dimensions * dimensions) { XOState.BLANK }
) : TicTacToeBoard {
    /**
     * @xCoord 0 indexed x coordinate, indices away from left
     * @yCoord 0 indexed y coordinate, indices away from top
     * @return a deep copy of the current board with the specified move applied
     */
    override fun withMove(xCoord: Int, yCoord: Int, playerTurn: XOState): TicTacToeBoard {
        if (xCoord >= dimensions || yCoord >= dimensions)
            throw OutOfBoundsMoveException()
        if (getAt(xCoord, yCoord) != XOState.BLANK) {
            throw AlreadyDefinedSquareException()
        }
        val updatedBoard = boardState.toMutableList()
        updatedBoard[yCoord * dimensions + xCoord] = playerTurn
        return TicTacToeBoardImpl(dimensions, updatedBoard)
    }

    override fun getGameStatus(): TicTacToeGameStatus {
        getAnyWinner()?.let {
            return if (it == XOState.X) {
                TicTacToeGameStatus.X_WIN
            } else {
                TicTacToeGameStatus.O_WIN
            }
        }
        if (boardState.filterNot { it == XOState.BLANK }.size == dimensions * dimensions) {
            return TicTacToeGameStatus.DRAW
        }
        return TicTacToeGameStatus.IN_PROGRESS
    }

    override fun getAt(xCoord: Int, yCoord: Int): XOState {
        return boardState[yCoord * dimensions + xCoord]
    }

    /**
     * @return winner based on current board state
     */
    private fun getAnyWinner(): XOState? = getRowWinner()
        ?: getColumnWinner()
        ?: getTopLeftDiagonalWinner()
        ?: getBottomLeftDiagonalWinner()

    /**
     * @return the first found row that's filled with an XOState or null if none exist
     */
    private fun getRowWinner(): XOState? = boardState
        .asSequence()
        .windowed(dimensions, dimensions) { it.distinct() } //Find distinct values in each row
        .firstOrNull { it.size == 1 && it.first() != XOState.BLANK } //Find where they all match and are not blank
        ?.firstOrNull() //Return the XOState of a winning row or null

    /**
     * @return the first found column that's filled with a single XOState or null if none exist
     */
    private fun getColumnWinner(): XOState? = (0 until dimensions) //Index of each column
        .map { //Map each column to it's distinct values
            boardState.drop(it) // Start at right column
                .windowed(1, dimensions) //Sample each row
                .flatten() //join them to a list
                .distinct() //take distinct elements
        }
        .firstOrNull { it.size == 1 && it.first() != XOState.BLANK } //Find all same value and not blank
        ?.firstOrNull() //Return the XOState of a winning column or null

    /**
     * @return XOState if top-left to bottom-right diagonal all match a single XOState, else null
     */
    private fun getTopLeftDiagonalWinner(): XOState? = boardState
        .asSequence()
        .windowed(1, dimensions + 1) // traverse diagonally
        .flatten()
        .distinct()
        .toList()
        .takeIf { it.size == 1 && it.first() != XOState.BLANK } // Take if all same non-blank XOState
        ?.firstOrNull() //Return the XOState of the winning diagonal or null

    /**
     * @return XOState if bottom-left to top-right diagonal all match a single XOState, else null
     */
    private fun getBottomLeftDiagonalWinner(): XOState? = boardState
        .drop(dimensions - 1)
        .dropLast(dimensions - 1)
        .asSequence()
        .windowed(1, dimensions - 1)
        .flatten()
        .distinct()
        .toList()
        .takeIf { it.size == 1 && it.first() != XOState.BLANK }
        ?.firstOrNull() //Return the XOState of the winning diagonal or null
}