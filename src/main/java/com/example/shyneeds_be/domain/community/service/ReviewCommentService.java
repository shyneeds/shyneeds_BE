package com.example.shyneeds_be.domain.community.service;

import com.example.shyneeds_be.domain.community.model.dto.request.ReviewCommentRequestDto;
import com.example.shyneeds_be.domain.community.model.dto.request.ReviewCommentUpdateRequestDto;
import com.example.shyneeds_be.domain.community.model.dto.response.ReviewCommentResponseDto;
import com.example.shyneeds_be.domain.community.model.entity.ReviewComment;
import com.example.shyneeds_be.domain.community.repository.ReviewCommentRepository;
import com.example.shyneeds_be.domain.member.model.entity.Member;
import com.example.shyneeds_be.domain.member.repository.MemberRepository;
import com.example.shyneeds_be.domain.member.service.MemberService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.Pagination;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class ReviewCommentService {


    private final ReviewCommentRepository reviewCommentRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    // 댓글 등록하기
    public ApiResponseDto register(User user, ReviewCommentRequestDto commentRequestDto) {
        try{
            Member member = memberService.findMemberByJwt(user);
            ReviewComment newReviewComment = ReviewComment.builder()
                    .reviewId(commentRequestDto.getReviewId())
                    .userId(member.getId())
                    .comment(commentRequestDto.getComment())
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                    .build();

            reviewCommentRepository.save(newReviewComment);

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "댓글 등록에 성공했습니다.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "등록에 실패했습니다. " + e.getMessage());
        }
    }

    // 댓글 리스트 불러오기
    public ApiResponseDto<List<ReviewCommentResponseDto>> getCommentList(Long reviewId, Pageable pageable) {
        try{

            Page<ReviewComment> commentList = reviewCommentRepository.findByReviewId(reviewId, pageable);
            Pagination pagination = Pagination.getPagination(commentList);
            List<ReviewCommentResponseDto> reviewCommentResponseDtoList = commentList.map(this::response).stream().toList();
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", reviewCommentResponseDtoList, pagination);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다. " + e.getMessage());
        }
    }

    // 댓글 불러오기
    public ApiResponseDto<ReviewCommentResponseDto> getComment(User user, Long commentId) {
        try{
            Member member = memberService.findMemberByJwt(user);
            ReviewComment comment = reviewCommentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다."));

            if(!comment.getUserId().equals(member.getId())){
                return ApiResponseDto.of(ResponseStatusCode.UNAUTHORIZED.getValue(), "조회 권한이 없습니다.");
            }
            ReviewCommentResponseDto reviewCommentResponseDto = this.response(comment);
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", reviewCommentResponseDto);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다. " + e.getMessage());
        }
    }

    // 댓글 수정하기
    public ApiResponseDto updateComment(User user, ReviewCommentUpdateRequestDto reviewCommentUpdateRequestDto){
        try{

            Member member = memberService.findMemberByJwt(user);
            Long memberId = member.getId();
            Long commentId = reviewCommentUpdateRequestDto.getCommentId();
            String comment = reviewCommentUpdateRequestDto.getComment();

            if(!memberId.equals(reviewCommentRepository.findById(commentId).get().getUserId())){
                return ApiResponseDto.of(ResponseStatusCode.UNAUTHORIZED.getValue(), "수정 권한이 없습니다.");
            }

            ReviewComment reviewComment = reviewCommentRepository.findByUserIdAndCommentId(memberId, commentId).orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));

            ReviewComment updatedComment = reviewComment.update(comment);

            reviewCommentRepository.save(updatedComment);

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "댓글 수정에 성공했습니다.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "댓글 수정에 실패했습니다. " + e.getMessage());
        }
    }

    // 댓글 삭제하기
    public ApiResponseDto deleteComment(User user, Long commentId){
        try{
            Member member = memberService.findMemberByJwt(user);
            ReviewComment reviewComment = reviewCommentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));

            if(!reviewComment.getUserId().equals(member.getId())){
                return ApiResponseDto.of(ResponseStatusCode.UNAUTHORIZED.getValue(), "댓글 삭제 권한이 없습니다.");
            }
            ReviewComment deletedComment = reviewComment.delete();

            reviewCommentRepository.save(deletedComment);

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "댓글 삭제에 성공했습니다.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "댓글 삭제에 실패했습니다. " + e.getMessage());
        }
    }

    private ReviewCommentResponseDto response(ReviewComment reviewComment) {

        Long memberId = null;
        String memberName = "탈퇴회원";

        Optional<Member> optionalUser = memberRepository.findById(reviewComment.getUserId());
        if(optionalUser.isPresent()){
            Member member = optionalUser.get();
            memberId = member.getId();
            memberName = member.getName();
        }

        return ReviewCommentResponseDto.builder()
                .id(reviewComment.getId())
                .reviewId(reviewComment.getReviewId())
                .memberId(memberId)
                .memberName(memberName)
                .comment(reviewComment.getComment())
                .updatedAt(reviewComment.getUpdatedAt())
                .build();
    }


}
