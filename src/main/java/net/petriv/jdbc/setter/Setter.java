package net.petriv.jdbc.setter;

import java.sql.PreparedStatement;

public abstract class Setter<T> {
   abstract public T setOne(T obj, PreparedStatement ps);
}
