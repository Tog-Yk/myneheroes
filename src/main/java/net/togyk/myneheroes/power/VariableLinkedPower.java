package net.togyk.myneheroes.power;

public interface VariableLinkedPower {
    default float getFloat(String name) {return 0;}
    default boolean setFloat(String name, float flt) {return false;}
    default boolean canSetFloat(String name, float flt) {return false;}

    default int getInt(String name){return 0;}
    default boolean setInt(String name, int integer) {return false;}
    default boolean canSetInt(String name, int integer) {return false;}

    default double getDouble(String name){return 0;}
    default boolean setDouble(String name, double dbl) {return false;}
    default boolean canSetDouble(String name, double dbl) {return false;}

    default boolean getBoolean(String name) {return false;}
    default boolean setBoolean(String name, boolean bool) {return false;}
    default boolean canSetBoolean(String name, boolean bool) {return bool != this.getBoolean(name);}
}
