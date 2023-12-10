package app.searchBar;


import app.Admin;
import app.audio.LibraryEntry;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static app.searchBar.FilterUtils.filterByAlbum;
import static app.searchBar.FilterUtils.filterByArtist;
import static app.searchBar.FilterUtils.filterByDescription;
import static app.searchBar.FilterUtils.filterByFollowers;
import static app.searchBar.FilterUtils.filterByGenre;
import static app.searchBar.FilterUtils.filterByLyrics;
import static app.searchBar.FilterUtils.filterByName;
import static app.searchBar.FilterUtils.filterByOwner;
import static app.searchBar.FilterUtils.filterByPlaylistVisibility;
import static app.searchBar.FilterUtils.filterByReleaseYear;
import static app.searchBar.FilterUtils.filterByTags;

public class SearchBar {
    private List<LibraryEntry> results;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    @Getter
    private String lastSearchType;

    @Getter
    private LibraryEntry lastSelected;
    /**
     * Gets the results of the search
     * @return results
     */
    public List<LibraryEntry> getResults() {
        return results;
    }

    /**
     * Sets the results of the search
     * @param results results
     */
    public void setResults(final List<LibraryEntry> results) {
        this.results = results;
    }

    /**
     * Gets the user.
     * @return user
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets the last search type.
     * @return last search type
     */
    public String getLastSearchType() {
        return lastSearchType;
    }

    /**
     * Sets the last search type.
     * @param lastSearchType last search type
     */
    public void setLastSearchType(final String lastSearchType) {
        this.lastSearchType = lastSearchType;
    }

    /**
     * Gets the last selected result.
     * @return
     */
    public LibraryEntry getLastSelected() {
        return lastSelected;
    }

    /**
     * Sets the last selected result.
     * @param lastSelected last selected result
     */
    public void setLastSelected(final LibraryEntry lastSelected) {
        this.lastSelected = lastSelected;
    }

    /**
     * Constructor.
     * @param user user
     */
    public SearchBar(final String user) {
        this.results = new ArrayList<>();
        this.user = user;
    }

    /**
     * Clears the selection.
     */
    public void clearSelection() {
        lastSelected = null;
        lastSearchType = null;
    }

    /**
     * Searches for a song, playlist, podcast, album, artist, or host.
     * @param filters filters
     * @param type type
     * @return results
     */
    public List<LibraryEntry> search(final Filters filters, final String type) {
        List<LibraryEntry> entries;
        Admin admin = Admin.getInstance();
        switch (type) {
            case "song":
                entries = new ArrayList<>(admin.getSongs());

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
                entries = new ArrayList<>(admin.getPlaylists());

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
                entries = new ArrayList<>(admin.getPodcasts());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                break;
            case "artist":
                entries = new ArrayList<>(admin.getArtistHelpers());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }
                break;
            case "album":
                entries = new ArrayList<>(admin.getAlbumSearchHelpers());
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
                entries = new ArrayList<>(admin.getHostHelpers());
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

    /**
     * Selects the result of a search.
     * @param itemNumber item number
     * @return selected result
     */
    public LibraryEntry select(final Integer itemNumber) {
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
