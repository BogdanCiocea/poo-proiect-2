package app.searchBar;


import app.Admin;
import app.audio.LibraryEntry;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static app.searchBar.FilterUtils.*;
import static app.searchBar.FilterUtils.filterByFollowers;

public class SearchBar {
    private List<LibraryEntry> results;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    @Getter
    private String lastSearchType;

    @Getter
    private LibraryEntry lastSelected;

    public List<LibraryEntry> getResults() {
        return results;
    }

    public void setResults(List<LibraryEntry> results) {
        this.results = results;
    }

    public String getUser() {
        return user;
    }

    public String getLastSearchType() {
        return lastSearchType;
    }

    public void setLastSearchType(String lastSearchType) {
        this.lastSearchType = lastSearchType;
    }

    public LibraryEntry getLastSelected() {
        return lastSelected;
    }

    public void setLastSelected(LibraryEntry lastSelected) {
        this.lastSelected = lastSelected;
    }

    public SearchBar(String user) {
        this.results = new ArrayList<>();
        this.user = user;
    }

    public void clearSelection() {
        lastSelected = null;
        lastSearchType = null;
    }
    public List<LibraryEntry> search(Filters filters, String type) {
        List<LibraryEntry> entries;

        switch (type) {
            case "song":
                entries = new ArrayList<>(Admin.getSongs());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getAlbum() != null) {
                    entries = filterByAlbum(entries, filters.getAlbum());
                }

                if (filters.getTags() != null) {
                    entries = filterByTags(entries, filters.getTags());
                }

                if (filters.getLyrics() != null) {
                    entries = filterByLyrics(entries, filters.getLyrics());
                }

                if (filters.getGenre() != null) {
                    entries = filterByGenre(entries, filters.getGenre());
                }

                if (filters.getReleaseYear() != null) {
                    entries = filterByReleaseYear(entries, filters.getReleaseYear());
                }

                if (filters.getArtist() != null) {
                    entries = filterByArtist(entries, filters.getArtist());
                }

                break;
            case "playlist":
                entries = new ArrayList<>(Admin.getPlaylists());

                entries = filterByPlaylistVisibility(entries, user);

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getFollowers() != null) {
                    entries = filterByFollowers(entries, filters.getFollowers());
                }

                break;
            case "podcast":
                entries = new ArrayList<>(Admin.getPodcasts());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                break;
            case "artist":
                entries = new ArrayList<>(Admin.getArtistHelpers());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }
                break;
            case "album":
                entries = new ArrayList<>(Admin.getAlbumSearchHelpers());
                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getDescription() != null) {
                    entries = filterByDescription(entries, filters.getDescription());
                }

                Set<LibraryEntry> uniqueEntries = new LinkedHashSet<>(entries);
                entries = new ArrayList<>(uniqueEntries);
                break;
            case "host":
                entries = new ArrayList<>(Admin.getHostHelpers());
                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }
                break;
            default:
                entries = new ArrayList<>();
        }

        Set<LibraryEntry> uniqueEntries = new LinkedHashSet<>(entries);
        entries = new ArrayList<>(uniqueEntries);

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.results = entries;
        this.lastSearchType = type;

        return this.results;
    }

    public LibraryEntry select(Integer itemNumber) {
        if (this.results.size() < itemNumber) {
            results.clear();

            return null;
        } else {
            lastSelected =  this.results.get(itemNumber - 1);
            results.clear();

            return lastSelected;
        }
    }
}