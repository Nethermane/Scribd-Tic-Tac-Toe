package nishimura.bruce.scribbed.model

import nishimura.bruce.scribbed.model.exception.InvalidTicTacToeMoveException

interface TicTacToeBoard {

    /**
     * @return a copy of the board after making a move
     */
    @Throws(InvalidTicTacToeMoveException::class)
    fun withMove(xCoord: Int, yCoord: Int, playerTurn: XOState): TicTacToeBoard

    /**
     * @return the game result
     */
    fun getGameStatus(): TicTacToeGameStatus

    /**
     * @return the value of the board at a given coordinate
     */
    fun getAt(xCoord: Int, yCoord: Int): XOState
}