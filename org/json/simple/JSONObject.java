/*
 * Decompiled with CFR 0.152.
 */
package org.json.simple;

import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONAware;
import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

public class JSONObject
extends HashMap
implements Map,
JSONAware,
JSONStreamAware {
    private static final long serialVersionUID = -503443796854799292L;

    public static void writeJSONString(Map map, Writer writer) {
        if (map == null) {
            writer.write("null");
            return;
        }
        boolean bl = true;
        Iterator iterator = map.entrySet().iterator();
        writer.write(123);
        while (iterator.hasNext()) {
            if (bl) {
                bl = false;
            } else {
                writer.write(44);
            }
            Map.Entry entry = iterator.next();
            writer.write(34);
            writer.write(JSONObject.escape(String.valueOf(entry.getKey())));
            writer.write(34);
            writer.write(58);
            JSONValue.writeJSONString(entry.getValue(), writer);
        }
        writer.write(125);
    }

    public void writeJSONString(Writer writer) {
        JSONObject.writeJSONString(this, writer);
    }

    public static String toJSONString(Map map) {
        if (map == null) {
            return "null";
        }
        StringBuffer stringBuffer = new StringBuffer();
        boolean bl = true;
        Iterator iterator = map.entrySet().iterator();
        stringBuffer.append('{');
        while (iterator.hasNext()) {
            if (bl) {
                bl = false;
            } else {
                stringBuffer.append(',');
            }
            Map.Entry entry = iterator.next();
            JSONObject.toJSONString(String.valueOf(entry.getKey()), entry.getValue(), stringBuffer);
        }
        stringBuffer.append('}');
        return stringBuffer.toString();
    }

    public String toJSONString() {
        return JSONObject.toJSONString(this);
    }

    private static String toJSONString(String string, Object object, StringBuffer stringBuffer) {
        stringBuffer.append('\"');
        if (string == null) {
            stringBuffer.append("null");
        } else {
            JSONValue.escape(string, stringBuffer);
        }
        stringBuffer.append('\"').append(':');
        stringBuffer.append(JSONValue.toJSONString(object));
        return stringBuffer.toString();
    }

    public String toString() {
        return this.toJSONString();
    }

    public static String toString(String string, Object object) {
        StringBuffer stringBuffer = new StringBuffer();
        JSONObject.toJSONString(string, object, stringBuffer);
        return stringBuffer.toString();
    }

    public static String escape(String string) {
        return JSONValue.escape(string);
    }
}

