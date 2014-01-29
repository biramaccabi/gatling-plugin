package com.excilys.ebi.gatling.jenkins.targetenvgraphs;

public enum Environment {
    FOXTROT("foxtrot"),
    STAGE("stage"),
    PROD("prod"),
    BETA("beta");

    public final String name;

    private Environment(String name) {
        this.name = name;
    }
}
