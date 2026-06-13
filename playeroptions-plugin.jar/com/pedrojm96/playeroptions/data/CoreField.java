/*
 * Decompiled with CFR 0.152.
 */
package com.pedrojm96.playeroptions.data;

public class CoreField {
    private String name;
    private String type;
    private Class<?> classtype;

    public CoreField(String name, String type, Class<?> classtype) {
        this.name = name;
        this.type = type;
        this.classtype = classtype;
    }

    public String toString() {
        return String.valueOf(this.name) + " " + this.type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Class<?> getClasstype() {
        return this.classtype;
    }

    public void setClasstype(Class<?> classtype) {
        this.classtype = classtype;
    }
}

