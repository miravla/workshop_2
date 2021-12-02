package com.example.UtemSmartParkingApplication.guardapplication;
public class ParkingModel {


        private String text;
        private boolean isSelected = false;

        public ParkingModel(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }


        public boolean isSelected() {
            return isSelected;
        }

}
