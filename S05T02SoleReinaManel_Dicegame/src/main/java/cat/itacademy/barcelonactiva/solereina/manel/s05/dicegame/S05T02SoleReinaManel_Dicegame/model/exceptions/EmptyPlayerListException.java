package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.exceptions;

public class EmptyPlayerListException extends RuntimeException{
    public EmptyPlayerListException() {
    }

    public EmptyPlayerListException(String message) {
        super(message);
    }
}
