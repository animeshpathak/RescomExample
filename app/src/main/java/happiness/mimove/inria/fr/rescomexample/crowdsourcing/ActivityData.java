package happiness.mimove.inria.fr.rescomexample.crowdsourcing;

import com.ambientic.crowdsource.data.GoFlowValue;

/**
 * Created by pgr on 08/04/15.
 */
public class ActivityData extends GoFlowValue {
    private int currentHour;

    //
    private int cptActivityChange=0;
    private int secActivityStill=0;
    private int secActivityTitle=0;
    private int secActivityDrive=0;
    private int secActivityCycle=0;
    private int secActivityOther=0;

    //
    private int cptProximityChange=0;
    private int secProximityOn=0;
    private int secProximityOff=0;



    //
    private int cptlocationModeChange=0;
    private int cptlocation=0;
    private int secLocationOn=0;
    private int secLocationOff=0;
    private int secLocationGPSOk=0;
    private int secLocationNetwOk=0;
    private int secLocationNotOk=0;


    public int getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;
    }

    public boolean isCurrentHour(int currentHour) {
        return (this.currentHour == currentHour);
    }

    public int getCptActivityChange() {
        return cptActivityChange;
    }

    public void incrCptActivityChange(int increment) {
        this.cptActivityChange += increment;
    }

    public int getSecActivityStill() {
        return secActivityStill;
    }

    public void incrSecActivityStill(int increment) {
        this.secActivityStill += increment;
    }

    public int getSecActivityTitle() {
        return secActivityTitle;
    }

    public void incrSecActivityTitle(int increment) {
        this.secActivityTitle += increment;
    }

    public int getSecActivityDrive() {
        return secActivityDrive;
    }

    public void incrSecActivityDrive(int increment) {
        this.secActivityDrive += increment;
    }

    public int getSecActivityCycle() {
        return secActivityCycle;
    }

    public void incrSecActivityCycle(int increment) {
        this.secActivityCycle += increment;
    }

    public int getSecActivityOther() {
        return secActivityOther;
    }

    public void incrSecActivityOther(int increment) {
        this.secActivityOther += increment;
    }

    public int getCptProximityChange() {
        return cptProximityChange;
    }

    public void incrCptProximityChange(int increment) {
        this.cptProximityChange += increment;
    }

    public int getSecProximityOn() {
        return secProximityOn;
    }

    public void incrSecProximityOn(int increment) {
        this.secProximityOn+= increment;
    }

    public int getSecProximityOff() {
        return secProximityOff;
    }

    public void incrSecProximityOff(int increment) {
        this.secProximityOff += increment;
    }

    public int getCptlocationModeChange() {
        return cptlocationModeChange;
    }

    public void incrCptlocationModeChange(int increment) {
        this.cptlocationModeChange += increment;
    }

    public int getCptlocation() {
        return cptlocation;
    }

    public void incrCptlocation(int increment) {
        this.cptlocation+= increment;
    }

    public int getSecLocationOn() {
        return secLocationOn;
    }

    public void incrSecLocationOn(int increment) {
        this.secLocationOn += increment;
    }

    public int getSecLocationOff() {
        return secLocationOff;
    }

    public void incrSecLocationOff(int increment) {
        this.secLocationOff += increment;
    }

    public int getSecLocationGPSOk() {
        return secLocationGPSOk;
    }

    public void incrSecLocationGPSOk(int increment) {
        this.secLocationGPSOk += increment;
    }

    public int getSecLocationNetwOk() {
        return secLocationNetwOk;
    }

    public void incrSecLocationNetwOk(int increment) {
        this.secLocationNetwOk += increment;
    }

    public int getSecLocationNotOk() {
        return secLocationNotOk;
    }

    public void incrSecLocationNotOk(int increment) {
        this.secLocationNotOk += increment;
    }
}
