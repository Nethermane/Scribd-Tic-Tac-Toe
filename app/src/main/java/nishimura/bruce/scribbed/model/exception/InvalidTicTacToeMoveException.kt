package nishimura.bruce.scribbed.model.exception

sealed class InvalidTicTacToeMoveException(message: String) : RuntimeException(message)

class OutOfBoundsMoveException : InvalidTicTacToeMoveException("Move outside of board bounds")

class AlreadyDefinedSquareException :
    InvalidTicTacToeMoveException("Square already defined, invalid move")

class GameAlreadyCompleteException :
    InvalidTicTacToeMoveException("Game has ended, please start a new one")