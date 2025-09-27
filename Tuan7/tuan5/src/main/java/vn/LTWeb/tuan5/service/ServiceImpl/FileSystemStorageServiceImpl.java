package vn.LTWeb.tuan5.service.ServiceImpl;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.LTWeb.tuan5.config.StorageProperties;
import vn.LTWeb.tuan5.exception.StorageException;
import vn.LTWeb.tuan5.service.StorageService;

@Service
public class FileSystemStorageServiceImpl implements StorageService {
    private final Path rootLocation;

    // Hàm này giúp tạo tên file duy nhất để tránh trùng lặp
    @Override
    public String getSorageFilename(MultipartFile file, String id) {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        return "img_" + id + "." + ext; // Thêm prefix để dễ quản lý
    }

    // Constructor này sẽ được Spring tự động gọi
    public FileSystemStorageServiceImpl(StorageProperties properties) {
        // Lấy đường dẫn từ file properties và giải quyết nó thành đường dẫn tuyệt đối
        this.rootLocation = Paths.get(properties.getLocation()).toAbsolutePath().normalize();
    }

    @Override
    public void store(MultipartFile file, String storeFilename) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file");
            }
            // Giải quyết đường dẫn tới file đích
            Path destinationFile = this.rootLocation.resolve(Paths.get(storeFilename)).normalize().toAbsolutePath();

            // Kiểm tra an ninh, ngăn chặn việc lưu file ra ngoài thư mục uploads
            if (!destinationFile.getParent().equals(this.rootLocation)) {
                throw new StorageException("Cannot store file outside current directory");
            }
            // Copy file vào thư mục đích, ghi đè nếu đã tồn tại
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            throw new StorageException("Failed to store file: ", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            throw new StorageException("Can not read file: " + filename);
        } catch (Exception e) {
            throw new StorageException("Could not read file: " + filename);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public void delete(String storeFilename) throws Exception {
        Path destinationFile = rootLocation.resolve(Paths.get(storeFilename)).normalize().toAbsolutePath();
        Files.deleteIfExists(destinationFile); // Dùng deleteIfExists để tránh lỗi nếu file không tồn tại
    }

    @Override
    public void init() {
        try {
            // Tạo thư mục uploads nếu nó chưa tồn tại
            Files.createDirectories(rootLocation);
            System.out.println("Initialized storage at: " + rootLocation.toString());
        } catch (Exception e) {
            throw new StorageException("Could not initialize storage directory: ", e);
        }
    }
}