package com.javainstitute.tickethub.model;

public class CardData {
    private int imageResource;
    private String imageResourceMain;
    private int imageResourceStar;
    private int imageResourceIcon;

    private String profileName;

    private String ticketId;

    public CardData(int imageResource, String imageResourceMain, int imageResourceStar, int imageResourceIcon, String profileName, String ticketId) {
        this.imageResource = imageResource;
        this.imageResourceMain = imageResourceMain;
        this.imageResourceStar = imageResourceStar;
        this.imageResourceIcon = imageResourceIcon;
        this.profileName = profileName;
        this.ticketId = ticketId;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getImageResourceMain() {
        return imageResourceMain;
    }

    public void setImageResourceMain(String imageResourceMain) {
        this.imageResourceMain = imageResourceMain;
    }

    public int getImageResourceStar() {
        return imageResourceStar;
    }

    public void setImageResourceStar(int imageResourceStar) {
        this.imageResourceStar = imageResourceStar;
    }

    public int getImageResourceIcon() {
        return imageResourceIcon;
    }

    public void setImageResourceIcon(int imageResourceIcon) {
        this.imageResourceIcon = imageResourceIcon;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
}
