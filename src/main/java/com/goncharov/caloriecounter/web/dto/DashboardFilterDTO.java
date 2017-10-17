package com.goncharov.caloriecounter.web.dto;

import javax.persistence.criteria.From;

/**
 * @author Anton Goncharov
 */
public class DashboardFilterDTO {

    private String fromDate;
    private String fromTime;
    private String toDate;
    private String toTime;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    @Override
    public String toString() {
        return "DashboardFilterDTO{" +
                "fromDate='" + fromDate + '\'' +
                ", fromTime='" + fromTime + '\'' +
                ", toDate='" + toDate + '\'' +
                ", toTime='" + toTime + '\'' +
                '}';
    }
}
