package app.audio.Collections;

import app.audio.LibraryEntry;

import java.util.List;

public class HostHelper extends LibraryEntry {
    private String name;
    private List<Podcast> podcasts;
    //private String announcements;

    public HostHelper(String name, List<Podcast> podcasts) {
        super(name);
        this.name = name;
        this.podcasts = podcasts;
    }
    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPodcasts(List<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

//    public String getAnnouncements() {
//        return announcements;
//    }
//
//    public void setAnnouncements(String announcements) {
//        this.announcements = announcements;
//    }
}
