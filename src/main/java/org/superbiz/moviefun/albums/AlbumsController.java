package org.superbiz.moviefun.albums;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.superbiz.moviefun.blobstore.Blob;
import org.superbiz.moviefun.blobstore.BlobStore;
import org.superbiz.moviefun.blobstore.FileStore;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;

@Controller
@RequestMapping("/albums")
public class AlbumsController {

    @Autowired
    private AlbumsBean albumsBean;

    @Autowired
    private org.superbiz.moviefun.blobstore.FileStore fileStore;
/*
    public AlbumsController(AlbumsBean albumsBean) {
        this.albumsBean = albumsBean;
    }
    public AlbumsController(AlbumsBean albumsBean, FileStore fileStore) {
        this.albumsBean = albumsBean;
        this.fileStore = fileStore;
    }
*/

    @GetMapping
    public String index(Map<String, Object> model) {
        model.put("albums", albumsBean.getAlbums());
        return "albums";
    }

    @GetMapping("/{albumId}")
    public String details(@PathVariable long albumId, Map<String, Object> model) {
        model.put("album", albumsBean.find(albumId));
        return "albumDetails";
    }

    @PostMapping("/{albumId}/cover")
    public String uploadCover(@PathVariable long albumId, @RequestParam("file") MultipartFile uploadedFile) throws IOException {
       // saveUploadToFile(uploadedFile, getCoverFile(albumId));
        // String coverFileName = format("covers/%d", albumId);

        String contentType = uploadedFile.getContentType();
        InputStream inputStream = uploadedFile.getInputStream();
        Blob b = new Blob(albumId+"", inputStream, contentType);

        fileStore.put(b);
        return format("redirect:/albums/%d", albumId);
    }

    @GetMapping("/{albumId}/cover")
    public HttpEntity<byte[]> getCover(@PathVariable long albumId) throws IOException, URISyntaxException {
       /* Path coverFilePath = getExistingCoverPath(albumId);
        byte[] imageBytes = readAllBytes(coverFilePath);
        HttpHeaders headers1 = createImageHttpHeaders(coverFilePath, imageBytes);
*/
         Optional<Blob>  op = fileStore.get(albumId+"");

        byte[] bytes = IOUtils.toByteArray(op.get().inputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(op.get().contentType));
        headers.setContentLength(bytes.length);

        return new HttpEntity<>(bytes, headers);
    }


    private void saveUploadToFile1(@RequestParam("file") MultipartFile uploadedFile, File targetFile) throws IOException {
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(uploadedFile.getBytes());
        }
    }

    private HttpHeaders createImageHttpHeaders1(Path coverFilePath, byte[] imageBytes) throws IOException {
        String contentType = new Tika().detect(coverFilePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(imageBytes.length);
        return headers;
    }

    private File getCoverFile(@PathVariable long albumId) {
        String coverFileName = format("covers/%d", albumId);
        return new File(coverFileName);
    }

    private Path getExistingCoverPath1(@PathVariable long albumId) throws URISyntaxException {
        File coverFile = getCoverFile(albumId);
        Path coverFilePath;

        if (coverFile.exists()) {
            coverFilePath = coverFile.toPath();
        } else {
            coverFilePath = Paths.get(getSystemResource("default-cover.jpg").toURI());
        }

        return coverFilePath;
    }
}
