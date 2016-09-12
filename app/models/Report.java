package models;

import com.avaje.ebean.ExpressionList;
import play.data.format.Formats;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Nobosny on 1/8/2015.
 * models
 */

@Entity
public class Report extends Model {
    @Id
    public Long rid;
    @Formats.DateTime(pattern="M/d/yyyy")
    public Date recorddate;
    public Integer mosquitoes;
    public Integer cockroaches;
    public Integer mice;
    public Integer biohazard;
    public Integer tick;
    public String othervectors;

    public Integer houses;
    public Integer businesses;
    public Integer constructionsites;
    public String othersources;

    public String imagefile;
    public String description;

    public Double lat;
    public Double lon;

    public Date addeddate;

    public static final Finder<Long, Report> find = new Finder<Long, Report>(Long.class, Report.class);

    public static List<Report> findAllAfter(Date time) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.YEAR, -5);
        return find.where()
                .ge("recorddate", cal.getTime())
                //.ge("recorddate", cal.getTime())
                .ge("addeddate", time)
                .findList();
    }

    @Override
    public String toString() {
        return "Report{" +
                "rid=" + rid +
                ", recorddate=" + recorddate +
                ", mosquitoes=" + mosquitoes +
                ", cockroaches=" + cockroaches +
                ", biohazard=" + biohazard +
                ", tick=" + tick +
                ", othervectors='" + othervectors + '\'' +
                ", houses=" + houses +
                ", businesses=" + businesses +
                ", constructionsites=" + constructionsites +
                ", othersources='" + othersources + '\'' +
                ", imagefile='" + imagefile + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    /* Filters Queries */
    public static List<Report> getFiltered(Params params) {
        if (params == null) {
            params = new Params();
        }

        ExpressionList<Report> query = Report.find.where();

        query.ge("recorddate", params.getStartDate());
        query.le("recorddate", params.getEndDate());

        if (params.getMosquitoes() == 1) { query.eq("mosquitoes", params.getMosquitoes()); }
        if (params.getCockroaches() == 1) { query.eq("cockroaches", params.getCockroaches()); }

        if (params.getMice() == 1) { query.eq("mice", params.getMice()); }
        if (params.getBiohazard() == 1) { query.eq("biohazard", params.getBiohazard()); }
        if (params.getTick() == 1) { query.eq("tick", params.getTick()); }

        if (params.getHouses() == 1) { query.eq("houses", params.getHouses()); }
        if (params.getBusinesses() == 1) { query.eq("businesses", params.getBusinesses()); }
        if (params.getConstructionsites() == 1) { query.eq("constructionsites", params.getConstructionsites()); }

        List<Report> result = query.findList();

        return result;
    }
}
