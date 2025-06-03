package HYU.FishShip.Feature.Posts.Dto;

import HYU.FishShip.Core.Entity.Attachment;
import HYU.FishShip.Core.Entity.Benefit;
import HYU.FishShip.Core.Entity.Posts;
import HYU.FishShip.Core.Entity.Requirement;


import java.util.List;
import java.util.stream.Collectors;

public class PostsMapper {

    public static Posts toEntity(PostsRequestDto dto) {
        Posts post = Posts.builder()
                .category(dto.getCategory())
                .title(dto.getTitle())
                .company(dto.getCompany())
                .location(dto.getLocation())
                .employment_type(dto.getEmployment_type())
                .start_date(dto.getStart_date())
                .end_date(dto.getEnd_date())
                .description(dto.getDescription())
                .build();

        List<Requirement> requirements = dto.getRequirements().stream()
                .map(req -> Requirement.builder().content(req).post(post).build())
                .toList();

        List<Benefit> benefits = dto.getBenefits().stream()
                .map(bnf -> Benefit.builder().content(bnf).post(post).build())
                .toList();

        List<Attachment> attachments = dto.getAttachments().stream()
                .map(a -> Attachment.builder().fileName(a.getFileName()).url(a.getUrl()).build())
                .toList();

        post.setRequirements(requirements);
        post.setBenefits(benefits);
        post.setAttachments(attachments);

        return post;
    }

    public static PostsResponseDto toDto(Posts post) {
        return PostsResponseDto.builder()
                .post_id(post.getPost_id())
                .category(post.getCategory())
                .title(post.getTitle())
                .company(post.getCompany())
                .location(post.getLocation())
                .employment_type(post.getEmployment_type())
                .applicationPeriod(new PostsResponseDto.ApplicationPeriod(
                        post.getStart_date(), post.getEnd_date()
                ))
                .description(post.getDescription())
                .requirements(post.getRequirements().stream()
                        .map(Requirement::getContent)
                        .collect(Collectors.toList()))
                .benefits(post.getBenefits().stream()
                        .map(Benefit::getContent)
                        .collect(Collectors.toList()))
                .attachments(post.getAttachments().stream()
                        .map(a -> new PostsResponseDto.AttachmentDto(a.getFileName(), a.getUrl()))
                        .collect(Collectors.toList()))
                .created_at(post.getCreated_at())
                .updated_at(post.getUpdated_at())
                .build();
    }
}