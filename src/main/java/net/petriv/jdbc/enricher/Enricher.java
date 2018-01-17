package net.petriv.jdbc.enricher;

import java.sql.SQLException;

public interface Enricher<T> {
   // public static final Enricher NULL = new Enricher();

    public void enrich (T record) throws SQLException;
}
