package app.searchBar;

import app.audio.LibraryEntry;

import java.util.ArrayList;
import java.util.List;

public class FilterUtils {
    /**
     * Filters the entries by name
     * @param entries entries
     * @param name name
     * @return filtered entries
     */
    public static List<LibraryEntry> filterByName(final List<LibraryEntry> entries,
                                                  final String name) {
        List<LibraryEntry> result = new ArrayList<>();
        for (LibraryEntry entry : entries) {
            if (entry.matchesName(name)) {
                result.add(entry);
            }
        }
        return result;
    }

    /**
     * Filters the entries by album
     * @param entries entries
     * @param album album
     * @return filtered entries
     */
    public static List<LibraryEntry> filterByAlbum(final List<LibraryEntry> entries,
                                                   final String album) {
        return filter(entries, entry -> entry.matchesAlbum(album));
    }

    /**
     * Filters the entries by tags
     * @param entries entries
     * @param tags tags
     * @return filtered entries
     */
    public static List<LibraryEntry> filterByTags(final List<LibraryEntry> entries,
                                                  final ArrayList<String> tags) {
        return filter(entries, entry -> entry.matchesTags(tags));
    }

    /**
     * Filters the entries by lyrics
     * @param entries entries
     * @param lyrics lyrics
     * @return filtered entries
     */
    public static List<LibraryEntry> filterByLyrics(final List<LibraryEntry> entries,
                                                    final String lyrics) {
        return filter(entries, entry -> entry.matchesLyrics(lyrics));
    }

    /**
     * Filters the entries by genre
     * @param entries entries
     * @param genre genre
     * @return filtered entries
     */
    public static List<LibraryEntry> filterByGenre(final List<LibraryEntry> entries,
                                                   final String genre) {
        return filter(entries, entry -> entry.matchesGenre(genre));
    }

    /**
     * Filters the entries by artist
     * @param entries entries
     * @param artist artist
     * @return filtered entries
     */
    public static List<LibraryEntry> filterByArtist(final List<LibraryEntry> entries,
                                                   final String artist) {
        return filter(entries, entry -> entry.matchesArtist(artist));
    }
    /**
     * Filters the entries by release year
     * @param entries entries
     * @param releaseYear release year
     * @return filtered entries
     */
    public static List<LibraryEntry> filterByReleaseYear(final List<LibraryEntry> entries,
                                                        final String releaseYear) {
        return filter(entries, entry -> entry.matchesReleaseYear(releaseYear));
    }

    /**
     * Filters the entries by owner
     * @param entries entries
     * @param user user
     * @return filtered entries
     */
    public static List<LibraryEntry> filterByOwner(final List<LibraryEntry> entries,
                                                  final String user) {
        return filter(entries, entry -> entry.matchesOwner(user));
    }
    /**
     * Filters the entries by description
     * @param entries entries
     * @param description description
     * @return filtered entries
     */
    public static List<LibraryEntry> filterByDescription(final List<LibraryEntry> entries,
                                                         final String description) {
        return filter(entries, entry -> entry.matchesDescription(description));
    }
    /**
     * Filters the entries by playlist visibility
     * @param entries entries
     * @param user user
     * @return filtered entries
     */
    public static List<LibraryEntry> filterByPlaylistVisibility(final List<LibraryEntry> entries,
                                                               final String user) {
        return filter(entries, entry -> entry.isVisibleToUser(user));
    }
    /**
     * Filters the entries by followers
     * @param entries entries
     * @param followers followers
     * @return filtered entries
     */
    public static List<LibraryEntry> filterByFollowers(final List<LibraryEntry> entries,
                                                       final String followers) {
        return filter(entries, entry -> entry.matchesFollowers(followers));
    }

    /**
     * Filters the entries by criteria
     * @param entries entries
     * @param criteria criteria
     * @return filtered entries
     */
    private static List<LibraryEntry> filter(final List<LibraryEntry> entries,
                                            final FilterCriteria criteria) {
        List<LibraryEntry> result = new ArrayList<>();
        for (LibraryEntry entry : entries) {
            if (criteria.matches(entry)) {
                result.add(entry);
            }
        }
        return result;
    }

    @FunctionalInterface
    private interface FilterCriteria {
        boolean matches(LibraryEntry entry);
    }
}
