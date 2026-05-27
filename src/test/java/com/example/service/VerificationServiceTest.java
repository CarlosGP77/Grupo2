package com.example.service;

import com.example.model.VerifiedImage;
import com.example.repository.VerifiedImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VerificationServiceTest {

    @Mock
    private VerifiedImageRepository repository;

    @Mock
    private NextcloudService nextcloudService;

    @InjectMocks
    private VerificationService verificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadImageSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file", "test.jpg", "image/jpeg", "test content".getBytes()
        );

        when(nextcloudService.uploadFile(any(), any())).thenReturn(true);
        when(repository.save(any())).thenReturn(new VerifiedImage("test.jpg", "/path", 100L, "image/jpeg"));

        VerifiedImage result = verificationService.uploadImage(file);

        assertNotNull(result);
        assertEquals("test.jpg", result.getFilename());
    }

    @Test
    void testUploadImageEmptyFile() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[0]);

        assertThrows(IllegalArgumentException.class, () -> verificationService.uploadImage(file));
    }

    @Test
    void testGetPendingImages() {
        when(repository.findByStatus(VerifiedImage.VerificationStatus.PENDING))
            .thenReturn(List.of(new VerifiedImage("test1.jpg", "/path1", 100L, "image/jpeg")));

        List<VerifiedImage> result = verificationService.getPendingImages();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testVerifyImage() {
        VerifiedImage image = new VerifiedImage("test.jpg", "/path", 100L, "image/jpeg");
        image.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(image));
        when(repository.save(any())).thenReturn(image);

        VerifiedImage result = verificationService.verifyImage(
            1L, VerifiedImage.VerificationStatus.APPROVED, "Good image", "Admin"
        );

        assertNotNull(result);
        assertEquals(VerifiedImage.VerificationStatus.APPROVED, result.getStatus());
        assertEquals("Admin", result.getVerifiedBy());
    }

    @Test
    void testDeleteImage() {
        VerifiedImage image = new VerifiedImage("test.jpg", "/path", 100L, "image/jpeg");
        image.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(image));
        when(nextcloudService.deleteFile("test.jpg")).thenReturn(true);

        verificationService.deleteImage(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testGetStatistics() {
        when(repository.findAll()).thenReturn(List.of(
            new VerifiedImage("test1.jpg", "/path1", 100L, "image/jpeg"),
            new VerifiedImage("test2.jpg", "/path2", 200L, "image/jpeg")
        ));

        var stats = verificationService.getStatistics();

        assertNotNull(stats);
        assertEquals(2, stats.get("totalImages"));
    }
}
