package com.thinkvitals.rabbitmq;
public enum ReloadDataType {
    none("None"),
    client("Client"),
    hospital("Hospital"),
    hcp("HCP"),
    hcpRole("HCP Role"),
    user("User"),
    pathway("Pathway"),
    role("Role"),
    nok("Next Of Kin"),
    note("Note"),
    gp("General Practitioner"),
    notification("Notification"),
    supportTeam("Support Team"),
    conversation("Conversations"),
    dischargeInformation("Discharge Information"),
    healthRecord("Health Record"),
    healthActivity("Health Activity"),
    watchConfig("Watch Config"),
    userPathway("User Pathway"),
    userPathwayDetail("User Pathway Detail"),
    userStaticList("User Static List"),
    userManagement("User Management"),
    userSymptom("User Symptom"),
    ownedPathway("Owned Pathway"),
    ownedHCP("Owned HCP"),
    reminder("Reminder"),
    staticList("Static List");

    ReloadDataType(String value) {
        this.value = value;
    }
    private String value;
    public String getValue() {
        return this.value;
    }

}
