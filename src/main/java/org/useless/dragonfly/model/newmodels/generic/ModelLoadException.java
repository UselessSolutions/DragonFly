package org.useless.dragonfly.model.newmodels.generic;

public class ModelLoadException extends Exception {
    public ModelLoadException(){
        super();
    }

    public ModelLoadException(String message){
        super(message);
    }
    public ModelLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelLoadException(Throwable cause) {
        super(cause);
    }
}
