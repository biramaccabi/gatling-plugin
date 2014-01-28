package com.excilys.ebi.gatling.jenkins.targetenvgraphs;

public enum EnvironmentEnum {
    FOXTROT("foxtrot"),
    STAGE("stage"),
    PROD("prod"),
    BETA("beta");

    public final String name;

    private EnvironmentEnum(String name) {
        this.name = name;
    }
}
