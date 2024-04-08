package com.example.tourguidemegaphone;

import android.os.Parcelable;

import java.io.Serializable;

public class TourModel implements Serializable {

    String _id;
    String tourTitle;
    String tourCountry;
    String tourCity;
    String tourDescription;
    String tourStartTime;
    String tourDuration;
    double tourPrice;
    String tourGuideEmail;

    public TourModel( String _id, String tourTitle, String tourCountry, String tourCity, String tourDescription, String tourStartTime, String tourDuration, double tourPrice, String tourGuideEmail) {
        this._id = _id;
        this.tourTitle = tourTitle;
        this.tourCountry = tourCountry;
        this.tourCity = tourCity;
        this.tourDescription = tourDescription;
        this.tourStartTime = tourStartTime;
        this.tourDuration = tourDuration;
        this.tourPrice = tourPrice;
        this.tourGuideEmail = tourGuideEmail;
    }
    public TourModel(String tourTitle, String tourCountry, String tourCity, String tourDescription, String tourStartTime, String tourDuration, double tourPrice, String tourGuideEmail) {
        this.tourTitle = tourTitle;
        this.tourCountry = tourCountry;
        this.tourCity = tourCity;
        this.tourDescription = tourDescription;
        this.tourStartTime = tourStartTime;
        this.tourDuration = tourDuration;
        this.tourPrice = tourPrice;
        this.tourGuideEmail = tourGuideEmail;
    }

    public String getTourGuideEmail() {
        return tourGuideEmail;
    }

    public void setTourGuideEmail(String tourGuideEmail) {
        this.tourGuideEmail = tourGuideEmail;
    }

    public TourModel(String tourTitle, String tourCountry, String tourCity, String tourDescription, String tourStartTime, String tourDuration, double tourPrice) {
            this.tourTitle = tourTitle;
            this.tourCountry = tourCountry;
            this.tourCity = tourCity;
            this.tourDescription = tourDescription;
            this.tourStartTime = tourStartTime;
            this.tourDuration = tourDuration;
            this.tourPrice = tourPrice;

        }

        public String getTourTitle() {
            return tourTitle;
        }

        public void setTourTitle(String tourTitle) {
            this.tourTitle = tourTitle;
        }

        public String getTourCountry() {
            return tourCountry;
        }

        public void setTourCountry(String tourCountry) {
            this.tourCountry = tourCountry;
        }

        public String getTourCity() {
            return tourCity;
        }

        public void setTourCity(String tourCity) {
            this.tourCity = tourCity;
        }

        public String getTourDescription() {
            return tourDescription;
        }

        public void setTourDescription(String tourDescription) {
            this.tourDescription = tourDescription;
        }

        public String getTourStartTime() {
            return tourStartTime;
        }

        public void setTourStartTime(String tourStartTime) {
            this.tourStartTime = tourStartTime;
        }

        public String getTourDuration() {
            return tourDuration;
        }

        public void setTourDuration(String tourDuration) {
            this.tourDuration = tourDuration;
        }

        public double getTourPrice() {
            return tourPrice;
        }

        public void setTourPrice(double tourPrice) {
            this.tourPrice = tourPrice;
        }
        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }
    }


