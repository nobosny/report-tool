package models;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Nobosny on 2/18/2015.
 * models
 */
public class Params {
    private Date startDate;
    private Date endDate;

    private Integer mosquitoes;
    private Integer cockroaches;
    private Integer mice;
    private Integer biohazard;
    private Integer tick;

    private Integer houses;
    private Integer businesses;
    private Integer constructionsites;

    private Integer haveimage;

    public Params() {
        endDate = new Date();
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.YEAR, -5);
        startDate = cal.getTime();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (startDate != null) {
            this.startDate = startDate;
        }
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate != null) {
            this.endDate = endDate;
        }
    }

    public Integer getMosquitoes() {
        return mosquitoes;
    }

    public void setMosquitoes(Integer mosquitoes) {
        this.mosquitoes = mosquitoes;
    }

    public Integer getCockroaches() {
        return cockroaches;
    }

    public void setCockroaches(Integer cockroaches) {
        this.cockroaches = cockroaches;
    }

    public Integer getMice() {
        return mice;
    }

    public void setMice(Integer mice) {
        this.mice = mice;
    }

    public Integer getBiohazard() {
        return biohazard;
    }

    public void setBiohazard(Integer biohazard) {
        this.biohazard = biohazard;
    }

    public Integer getTick() {
        return tick;
    }

    public void setTick(Integer tick) {
        this.tick = tick;
    }

    public Integer getHouses() {
        return houses;
    }

    public void setHouses(Integer houses) {
        this.houses = houses;
    }

    public Integer getBusinesses() {
        return businesses;
    }

    public void setBusinesses(Integer businesses) {
        this.businesses = businesses;
    }

    public Integer getConstructionsites() {
        return constructionsites;
    }

    public void setConstructionsites(Integer constructionsites) {
        this.constructionsites = constructionsites;
    }

    public Integer getHaveimage() {
        return haveimage;
    }

    public void setHaveimage(Integer haveimage) {
        this.haveimage = haveimage;
    }

    @Override
    public String toString() {
        return "Params{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", mosquitoes=" + mosquitoes +
                ", cockroaches=" + cockroaches +
                ", mice=" + mice +
                ", biohazard=" + biohazard +
                ", houses=" + houses +
                ", businesses=" + businesses +
                ", constructionsites=" + constructionsites +
                ", haveimage=" + haveimage +
                '}';
    }
}
