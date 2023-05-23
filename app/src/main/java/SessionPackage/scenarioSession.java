package SessionPackage;

public class scenarioSession {
    String scenario, stat;

    public scenarioSession(String scenario, String stat) {
        this.scenario = scenario;
        this.stat = stat;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
