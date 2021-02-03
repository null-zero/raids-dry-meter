package com.raidsdrymeter.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UniqueEntry {
    private final String name;
    private final int id;
    private int quantity;
    private long price;

    public long getTotal()
    {
        return this.quantity * this.price;
    }

}
