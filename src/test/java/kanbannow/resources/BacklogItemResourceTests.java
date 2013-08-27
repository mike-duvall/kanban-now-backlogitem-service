package kanbannow.resources;

import kanbannow.api.BacklogItem;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class BacklogItemResourceTests {


    @Test
    public void shouldReturnEmptyListOfBacklogItems() {
        // Given
        BacklogItemResource backlogItemResource = new BacklogItemResource();

        // When
        List<BacklogItem> backlogItemList = backlogItemResource.getBacklogItemsForUser();

        // Then
        assertThat(backlogItemList).isEmpty();
    }
}
