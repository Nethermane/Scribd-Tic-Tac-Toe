package nishimura.bruce.scribbed.model

import nishimura.bruce.scribbed.model.exception.AlreadyDefinedSquareException
import nishimura.bruce.scribbed.model.exception.GameAlreadyCompleteException
import nishimura.bruce.scribbed.model.exception.OutOfBoundsMoveException
import org.junit.Assert
import org.junit.Test

internal class TicTacToeGameStateTest {

    @Test
    fun withMoveXFirst() {
        var state = TicTacToeGameState()
        state = state.withMove(0,0)
        Assert.assertEquals(XOState.X, state.board.getAt(0,0))
    }

    @Test
    fun withMoveOSecond() {
        var state = TicTacToeGameState()
        state = state.withMove(0,0)
        state = state.withMove(1,0)
        Assert.assertEquals(XOState.O, state.board.getAt(1,0))
    }

    @Test(expected = AlreadyDefinedSquareException::class)
    fun placeOnSameSpotThrowsError() {
        TicTacToeGameState()
            .withMove(0,0)
            .withMove(0,0)
    }

    @Test(expected = OutOfBoundsMoveException::class)
    fun placeOutOfBoundsThrowsException() {
        TicTacToeGameState(3).withMove(4,0)
    }

    @Test(expected = GameAlreadyCompleteException::class)
    fun tryMoveAfterGameOverThrowsException() {
        TicTacToeGameState()
            .withMove(0,0)
            .withMove(2,2)
            .withMove(0,1)
            .withMove(2,1)
            .withMove(0,2)
            .withMove(1,1)

    }

    @Test
    fun getGameOutcomeRow() {
        var xWinRow = TicTacToeGameState()
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,xWinRow.gameStatus)
        xWinRow = xWinRow.withMove(0,0)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,xWinRow.gameStatus)
        xWinRow = xWinRow.withMove(2,2)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,xWinRow.gameStatus)
        xWinRow = xWinRow.withMove(0,1)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,xWinRow.gameStatus)
        xWinRow = xWinRow.withMove(2,1)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,xWinRow.gameStatus)
        xWinRow = xWinRow.withMove(0,2)
        Assert.assertEquals(TicTacToeGameStatus.X_WIN,xWinRow.gameStatus)
    }

    @Test
    fun getGameOutcomeColumn() {
        var xWinColumn = TicTacToeGameState()
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,xWinColumn.gameStatus)
        xWinColumn = xWinColumn.withMove(0,0)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,xWinColumn.gameStatus)
        xWinColumn = xWinColumn.withMove(2,2)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,xWinColumn.gameStatus)
        xWinColumn = xWinColumn.withMove(1,0)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,xWinColumn.gameStatus)
        xWinColumn = xWinColumn.withMove(1,2)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,xWinColumn.gameStatus)
        xWinColumn = xWinColumn.withMove(2,0)
        Assert.assertEquals(TicTacToeGameStatus.X_WIN,xWinColumn.gameStatus)
    }

    @Test
    fun getGameOutcomeTopLeftDiagonal() {
        var oWinTopLeftDiagonal = TicTacToeGameState()
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinTopLeftDiagonal.gameStatus)
        oWinTopLeftDiagonal = oWinTopLeftDiagonal.withMove(2,1)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinTopLeftDiagonal.gameStatus)
        oWinTopLeftDiagonal = oWinTopLeftDiagonal.withMove(0,0)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinTopLeftDiagonal.gameStatus)
        oWinTopLeftDiagonal = oWinTopLeftDiagonal.withMove(2,0)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinTopLeftDiagonal.gameStatus)
        oWinTopLeftDiagonal = oWinTopLeftDiagonal.withMove(1,1)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinTopLeftDiagonal.gameStatus)
        oWinTopLeftDiagonal = oWinTopLeftDiagonal.withMove(0,2)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinTopLeftDiagonal.gameStatus)
        oWinTopLeftDiagonal = oWinTopLeftDiagonal.withMove(2,2)
        Assert.assertEquals(TicTacToeGameStatus.O_WIN,oWinTopLeftDiagonal.gameStatus)
    }

    @Test
    fun getGameOutcomeBottomLeftDiagonal() {
        var oWinBottomLeftDiagonal = TicTacToeGameState()
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(2,1)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(0,2)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(1,0)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(1,1)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(1,2)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(2,0)
        Assert.assertEquals(TicTacToeGameStatus.O_WIN,oWinBottomLeftDiagonal.gameStatus)
    }


    /**
     * X O X
     * O X X
     * O X O
     */
    @Test
    fun getGameOutcomeDraw() {
        var oWinBottomLeftDiagonal = TicTacToeGameState()
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(0,0)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(1,0)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(2,0)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(0,1)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(1,1)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(0,2)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(2,1)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(2,2)
        Assert.assertEquals(TicTacToeGameStatus.IN_PROGRESS,oWinBottomLeftDiagonal.gameStatus)
        oWinBottomLeftDiagonal = oWinBottomLeftDiagonal.withMove(1,2)
        Assert.assertEquals(TicTacToeGameStatus.DRAW,oWinBottomLeftDiagonal.gameStatus)
    }
}