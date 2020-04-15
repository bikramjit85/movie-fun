package org.superbiz.moviefun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albums.Album;
import org.superbiz.moviefun.albums.AlbumFixtures;
import org.superbiz.moviefun.albums.AlbumsBean;
import org.superbiz.moviefun.movies.Movie;
import org.superbiz.moviefun.movies.MovieFixtures;
import org.superbiz.moviefun.movies.MoviesBean;

import java.util.Map;

@Controller
public class HomeController {

    private final MoviesBean moviesBean;
    private final AlbumsBean albumsBean;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;
    private final PlatformTransactionManager movieTransactionManager;
    private final PlatformTransactionManager albumTransactionManager;

    public HomeController(@Autowired  MoviesBean moviesBean, @Autowired AlbumsBean albumsBean, @Autowired MovieFixtures movieFixtures, @Autowired AlbumFixtures albumFixtures,
                          @Autowired PlatformTransactionManager movieTransactionManager,
                          @Autowired PlatformTransactionManager albumTransactionManager) {
        this.moviesBean = moviesBean;
        this.albumsBean = albumsBean;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
        this.movieTransactionManager = movieTransactionManager;
        this.albumTransactionManager = albumTransactionManager;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {
        TransactionTemplate movieTransactionTemplate = new TransactionTemplate(movieTransactionManager);
        for (Movie movie : movieFixtures.load()) {
            movieTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    moviesBean.addMovie(movie);
                }
            });

            //moviesBean.addMovie(movie);
        }

        TransactionTemplate albumTransactionTemplate = new TransactionTemplate(albumTransactionManager);
        for (Album album : albumFixtures.load()) {
            albumTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    albumsBean.addAlbum(album);
                }
            });
            // albumsBean.addAlbum(album);
        }

        model.put("movies", moviesBean.getMovies());
        model.put("albums", albumsBean.getAlbums());

        return "setup";
    }
}
