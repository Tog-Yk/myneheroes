package net.togyk.myneheroes.power;

public interface VariableLinkedPower {
    Object get(String name);
    boolean canSet(String name, Object variable);
    void set(String name, Object variable);
}
