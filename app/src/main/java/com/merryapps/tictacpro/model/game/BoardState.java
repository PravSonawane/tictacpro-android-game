package com.merryapps.tictacpro.model.game;

/**
 * The state of the board.
 * Created by mephisto on 9/17/16.
 */
enum BoardState {

    /** Denotes a board with all cells having the token {@link Token#E} */
    NEW,
    /** Denotes a board that has yet not been won by any side. This board is also necessarily not
     in the state {@link BoardState#NEW} and {@link BoardState#DRAW} */
    IN_PROGRESS,
    /** Denotes a board that was won by the player playing the token {@link Token#X} */
    WON_BY_X,
    /** Denotes a board that was won by the player playing the token {@link Token#O} */
    WON_BY_O,
    /** Denotes a board state after a game is a draw */
    DRAW,

}
