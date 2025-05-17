package net.togyk.myneheroes.power;

public interface VariableLinkedPower {
    default float getFloat(String name) {return 0;}
    default void setFloat(String name, float flt){}

    default int getInt(String name){return 0;}
    default void setInt(String name, int integer){}

    default double getDouble(String name){return 0;}
    default void setDouble(String name, double dbl){}

    default boolean getBoolean(String name) {return false;}
    default void setBoolean(String name, boolean bool) {}
}
