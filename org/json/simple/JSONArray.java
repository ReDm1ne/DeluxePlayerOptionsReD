/*
 * Decompiled with CFR 0.152.
 */
package org.json.simple;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONAware;
import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

public class JSONArray
extends ArrayList
implements List,
JSONAware,
JSONStreamAware {
    private static final long serialVersionUID = 3957988303675231981L;

    public static void writeJSONString(List list, Writer writer) {
        if (list == null) {
            writer.write("null");
            return;
        }
        boolean bl = true;
        Iterator iterator = list.iterator();
        writer.write(91);
        while (iterator.hasNext()) {
            if (bl) {
                bl = false;
            } else {
                writer.write(44);
            }
            Object e = iterator.next();
            if (e == null) {
                writer.write("null");
                continue;
            }
            JSONValue.writeJSONString(e, writer);
        }
        writer.write(93);
    }

    public void writeJSONString(Writer writer) {
        JSONArray.writeJSONString(this, writer);
    }

    public static String toJSONString(List list) {
        if (list == null) {
            return "null";
        }
        boolean bl = true;
        StringBuffer stringBuffer = new StringBuffer();
        Iterator iterator = list.iterator();
        stringBuffer.append('[');
        while (iterator.hasNext()) {
            if (bl) {
                bl = false;
            } else {
                stringBuffer.append(',');
            }
            Object e = iterator.next();
            if (e == null) {
                stringBuffer.append("null");
                continue;
            }
            stringBuffer.append(JSONValue.toJSONString(e));
        }
        stringBuffer.append(']');
        return stringBuffer.toString();
    }

    public String toJSONString() {
        return JSONArray.toJSONString(this);
    }

    public String toString() {
        return this.toJSONString();
    }
}

