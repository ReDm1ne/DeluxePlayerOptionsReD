/*
 * Decompiled with CFR 0.152.
 */
package com.pedrojm96.playeroptions.data;

public class CoreWHERE {
    private String where = "";
    String[] args;

    public CoreWHERE(String ... args) {
        this.args = args;
        int i = 0;
        while (i < args.length) {
            String[] local;
            if (args[i].trim().contains(":") && (local = args[i].trim().split(":")).length >= 2) {
                String locaWhere = local[0].trim();
                String locaValue = local[1].trim();
                this.where = i == args.length - 1 ? String.valueOf(this.where) + locaWhere + " = '" + locaValue + "'" : String.valueOf(this.where) + locaWhere + " = '" + locaValue + "'" + " AND ";
            }
            ++i;
        }
    }

    public String get() {
        return this.where.trim();
    }

    public String[] get(int i) {
        return this.args[i].trim().split(":");
    }
}

