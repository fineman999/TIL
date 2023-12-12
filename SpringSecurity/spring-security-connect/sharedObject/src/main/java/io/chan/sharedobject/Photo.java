package io.chan.sharedobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo implements Serializable {
    private String userId;
    private String photoId;
    private String photoTitle;
    private String photoDescription;

    public static Photo of(String userId, String photoId, String photoTitle, String photoDescription) {
        return Photo.builder()
                .userId(userId)
                .photoId(photoId)
                .photoTitle(photoTitle)
                .photoDescription(photoDescription)
                .build();
    }
}
