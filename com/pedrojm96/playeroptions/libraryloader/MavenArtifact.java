/*
 * Decompiled with CFR 0.152.
 */
package com.pedrojm96.playeroptions.libraryloader;

import java.net.URL;

public class MavenArtifact {
    public String groupId;
    public String artifactId;
    public String version;
    public String repo;

    public MavenArtifact(String string, String string2, String string3, String string4) {
        this.groupId = string;
        this.artifactId = string2;
        this.version = string3;
        this.repo = string4;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String string) {
        this.groupId = string;
    }

    public String getArtifactId() {
        return this.artifactId;
    }

    public void setArtifactId(String string) {
        this.artifactId = string;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String string) {
        this.version = string;
    }

    public URL getUrl() {
        String string = this.repo;
        if (!string.endsWith("/")) {
            string = String.valueOf(String.valueOf(string)) + "/";
        }
        string = String.valueOf(String.valueOf(string)) + "%s/%s/%s/%s-%s.jar";
        String string2 = String.format(string, this.groupId.replace(".", "/"), this.artifactId, this.version, this.artifactId, this.version);
        return new URL(string2);
    }

    public String getPath() {
        String string = String.valueOf(this.groupId.replace('.', '/')) + '/' + this.artifactId + '/' + this.version + '/' + this.artifactId + '-' + this.version + ".jar";
        return string;
    }

    public void setRepo(String string) {
        this.repo = string;
    }

    public String getRepo() {
        return this.repo;
    }
}

