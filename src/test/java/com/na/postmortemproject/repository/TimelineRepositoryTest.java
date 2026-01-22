package com.na.postmortemproject.repository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.na.postmortemproject.entity.Timeline;

@DataJpaTest
@ActiveProfiles("test")
class TimelineRepositoryTest {

    @Autowired
    private TimelineRepository timelineRepository;

    @Test
    void testCreateTimeline() {
        // Given
        Timeline timeline = new Timeline();
        timeline.setTitle("Test Incident Timeline");
        timeline.setTimelineContent("## Incident Summary\n\nTest content here");
        timeline.setFileMetadata("{\"files\": [\"file1.log\", \"file2.txt\"], \"count\": 2}");

        // When
        Timeline saved = timelineRepository.save(timeline);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Test Incident Timeline");
        assertThat(saved.getTimelineContent()).isEqualTo("## Incident Summary\n\nTest content here");
        assertThat(saved.getCreatedAt()).isNotNull();
        System.out.println("✓ CREATE test passed - ID: " + saved.getId());
    }

    @Test
    void testReadTimeline() {
        // Given
        Timeline timeline = new Timeline();
        timeline.setTitle("Read Test");
        timeline.setTimelineContent("Content for reading");
        Timeline saved = timelineRepository.save(timeline);

        // When
        Optional<Timeline> found = timelineRepository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Read Test");
        assertThat(found.get().getTimelineContent()).isEqualTo("Content for reading");
        System.out.println("✓ READ test passed - Found timeline: " + found.get().getTitle());
    }

    @Test
    void testUpdateTimeline() {
        // Given
        Timeline timeline = new Timeline();
        timeline.setTitle("Original Title");
        timeline.setTimelineContent("Original Content");
        Timeline saved = timelineRepository.save(timeline);

        // When
        saved.setTitle("Updated Title");
        saved.setTimelineContent("Updated Content");
        Timeline updated = timelineRepository.save(saved);

        // Then
        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getTitle()).isEqualTo("Updated Title");
        assertThat(updated.getTimelineContent()).isEqualTo("Updated Content");
        assertThat(updated.getCreatedAt()).isEqualTo(saved.getCreatedAt()); // Should not change
        System.out.println("✓ UPDATE test passed - Updated to: " + updated.getTitle());
    }

    @Test
    void testDeleteTimeline() {
        // Given
        Timeline timeline = new Timeline();
        timeline.setTitle("To Delete");
        timeline.setTimelineContent("Content to be deleted");
        Timeline saved = timelineRepository.save(timeline);
        Long id = saved.getId();

        // When
        timelineRepository.deleteById(id);

        // Then
        Optional<Timeline> found = timelineRepository.findById(id);
        assertThat(found).isEmpty();
        System.out.println("✓ DELETE test passed - Timeline deleted");
    }

    @Test
    void testFindAllByOrderByCreatedAtDesc() throws InterruptedException {
        // Given
        Timeline timeline1 = new Timeline();
        timeline1.setTitle("First Timeline");
        timeline1.setTimelineContent("Content 1");
        timelineRepository.save(timeline1);

        // Small delay to ensure different timestamps
        Thread.sleep(10);

        Timeline timeline2 = new Timeline();
        timeline2.setTitle("Second Timeline");
        timeline2.setTimelineContent("Content 2");
        timelineRepository.save(timeline2);

        // When
        List<Timeline> timelines = timelineRepository.findALLByOrderByCreatedAtDesc();

        // Then
        assertThat(timelines).hasSizeGreaterThanOrEqualTo(2);
        assertThat(timelines.get(0).getTitle()).isEqualTo("Second Timeline"); // Most recent first
        System.out.println("✓ FIND ALL ORDERED test passed - Found " + timelines.size() + " timelines");
    }

    @Test
    void testJsonbFileMetadata() {
        // Given - Complex JSON structure
        String jsonMetadata = """
            {
                "files": [
                    {
                        "name": "incident.log",
                        "size": 1024,
                        "type": "text/plain",
                        "uploadedAt": "2026-01-22T10:30:00Z"
                    },
                    {
                        "name": "screenshot.png",
                        "size": 204800,
                        "type": "image/png",
                        "uploadedAt": "2026-01-22T10:31:00Z"
                    }
                ],
                "totalSize": 205824,
                "count": 2
            }
            """;

        Timeline timeline = new Timeline();
        timeline.setTitle("JSONB Test");
        timeline.setTimelineContent("Testing JSONB storage");
        timeline.setFileMetadata(jsonMetadata);

        // When
        Timeline saved = timelineRepository.save(timeline);
        Timeline retrieved = timelineRepository.findById(saved.getId()).orElseThrow();

        // Then
        assertThat(retrieved.getFileMetadata()).isNotNull();
        assertThat(retrieved.getFileMetadata()).contains("incident.log");
        assertThat(retrieved.getFileMetadata()).contains("screenshot.png");
        assertThat(retrieved.getFileMetadata()).contains("\"count\": 2");
        System.out.println("✓ JSONB test passed - Metadata stored and retrieved successfully");
    }

    @Test
    void testNullFileMetadata() {
        // Given
        Timeline timeline = new Timeline();
        timeline.setTitle("No Metadata");
        timeline.setTimelineContent("Content without files");
        timeline.setFileMetadata(null);

        // When
        Timeline saved = timelineRepository.save(timeline);
        Timeline retrieved = timelineRepository.findById(saved.getId()).orElseThrow();

        // Then
        assertThat(retrieved.getFileMetadata()).isNull();
        System.out.println("✓ NULL METADATA test passed - Null values handled correctly");
    }
}