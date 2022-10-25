package com.example.shyneeds_be.domain.community.service;

import com.example.shyneeds_be.domain.community.model.dto.request.ReviewRegisterRequestDto;
import com.example.shyneeds_be.domain.community.model.dto.request.ReviewUpdateRequestDto;
import com.example.shyneeds_be.domain.community.model.dto.response.BestReviewResponseDto;
import com.example.shyneeds_be.domain.community.model.dto.response.ReviewMainResponseDto;
import com.example.shyneeds_be.domain.community.model.dto.response.ReviewResponseDto;
import com.example.shyneeds_be.domain.community.model.dto.response.VisitPackageResponseDto;
import com.example.shyneeds_be.domain.community.model.entity.Review;
import com.example.shyneeds_be.domain.community.model.entity.ReviewLike;
import com.example.shyneeds_be.domain.community.model.entity.ReviewLikeCount;
import com.example.shyneeds_be.domain.community.model.entity.VisitPackage;
import com.example.shyneeds_be.domain.community.repository.ReviewLikeRepository;
import com.example.shyneeds_be.domain.community.repository.ReviewRepository;
import com.example.shyneeds_be.domain.reservation.model.entity.Reservation;
import com.example.shyneeds_be.domain.reservation.repository.ReservationRepository;
import com.example.shyneeds_be.domain.travel_package.repository.TravelPackageRepository;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.domain.user.repository.UserRepository;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.Pagination;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import com.example.shyneeds_be.global.network.s3.ItemS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Transactional
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final TravelPackageRepository travelPackageRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    private final ItemS3Uploader itemS3Uploader;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 리뷰 리스트 메인 (검색 포함. all 전체 검색)
    public ApiResponseDto<List<ReviewMainResponseDto>> getReviewList(String search, Pageable pageable) {
        try{

            Page<Review> reviewList = reviewRepository.findBySearch(search, pageable);
            Pagination pagination = Pagination.getPagination(reviewList);

            List<ReviewMainResponseDto> reviewMainResponseDtoList = reviewList.map(this::response).toList();

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", reviewMainResponseDtoList, pagination);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다. " + e.getMessage());
        }
    }


    // 리뷰 등록
    public ApiResponseDto register(User user, ReviewRegisterRequestDto reviewRegisterRequestDto) {
        try{


            // 해당 유저의 예약확정 된 예약인지 확인
            Long userId = user.getId();
            Long reservationId = reviewRegisterRequestDto.getReservationId();

            Optional<Reservation> optionalReservation = reservationRepository.findByUserIdAndReservationId(userId, reservationId);

            if(optionalReservation.isEmpty()){
                return ApiResponseDto.of(ResponseStatusCode.UNAUTHORIZED.getValue(), "예약 정보가 일치하지 않은 회원이거나 예약상태 확인이 필요합니다.");
            }

            // 방어로직. 동일한 예약건에 대해 한 번만 리뷰 등록 가능
            Optional<Review> optionalReview = reviewRepository.findByReservationId(reservationId);
            if(optionalReview.isPresent()){
                return ApiResponseDto.of(ResponseStatusCode.BAD_REQUEST.getValue(), "이미 작성된 예약 번호 입니다.");
            }

            // 미구현
            // 이미지 삭제 : 변경된 이미지 리스트로 S3에서 삭제한다.
            // 이미지 이름 가져오기
            // S3에서 이미지 삭제


            Review newReview = Review.builder()
                    .title(reviewRegisterRequestDto.getTitle())
                    .mainImage(reviewRegisterRequestDto.getMainImage())
                    .userId(userId)
                    .contents(reviewRegisterRequestDto.getContents())
                    .reservationId(reservationId)
                    .dispFlg(true)
                    .deletedFlg(false)
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                    .build();

            reviewRepository.save(newReview);

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "리뷰 등록에 성공했습니다.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "리뷰 등록에 실패했습니다. " + e.getMessage());
        }
    }

    // 이미지 파일 업로드 (S3) -> url 반환
    public String saveImage(MultipartFile upload) {
        try{
            if(upload == null){
                return null;
            }

            String bucketName = bucket + "/review/upload";
            return itemS3Uploader.uploadLocal(upload, bucketName);


        } catch (Exception e){

            e.printStackTrace();
            return null;
        }
    }

    private ReviewMainResponseDto response(Review review) {

        // user name 마스킹 (김**) 처리
        Optional<User> optionalUser = userRepository.findById(Long.valueOf(review.getUserId()));
        String author = "탈퇴회원";
        if(optionalUser.isPresent()){
            author = getMaskingAuthor(optionalUser.get().getName());
        }
        return ReviewMainResponseDto.builder()
                .id(review.getId())
                .mainImage(review.getMainImage())
                .title(review.getTitle())
                .updatedAt(review.getUpdatedAt())
                .author(author)
                .build();
    }

    // 김** 처리
    private String getMaskingAuthor(String username){
        StringBuilder sb = new StringBuilder();
        char[] userNameCharList = username.toCharArray();
        sb.append(userNameCharList[0]);
        for (int i = 0; i < userNameCharList.length-1; i++) {
            sb.append("*");
        }

        return sb.toString();
    }

    // [Mypage] 내가 작성한 리뷰 조회
    public ApiResponseDto<List<ReviewMainResponseDto>> getMyReviewList(User user, Pageable pageable) {
        try{

            Page<Review> userReviewList = reviewRepository.findByUserId(user.getId(), pageable);

            Pagination pagination = Pagination.getPagination(userReviewList);

            List<ReviewMainResponseDto> reviewMainResponseDtoList = userReviewList.map(this::response).stream().toList();

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", reviewMainResponseDtoList, pagination);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다. " + e.getMessage());
        }
    }

    // 리뷰 상세 조회
    public ApiResponseDto<ReviewResponseDto> getReview(Long reviewId) {
        try{
            Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("해당 리뷰를 찾을 수 없습니다."));


            // 조회수 증가
            Review increaseLookupReview = review.increaseLookup();

            Review detailsReview = reviewRepository.save(increaseLookupReview);

            ReviewResponseDto reviewResponseDto = this.responseDetails(detailsReview);

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", reviewResponseDto);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다. " + e.getMessage());
        }
    }


    // 리뷰 상세 보기 응답
    private ReviewResponseDto responseDetails(Review review) {

        String author = "탈퇴회원";
        Optional<User> optionalUser = userRepository.findById(review.getUserId());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            author = this.getMaskingAuthor(user.getName());
        }


        VisitPackageResponseDto visitPackageResponseDto = null;
        Optional<VisitPackage> optionalVisitPackage = travelPackageRepository.findVisitPackage(review.getId());
        if(optionalVisitPackage.isPresent()){
            VisitPackage visitPackage = optionalVisitPackage.get();

            String mainImage = "https://shyneeds.s3.ap-northeast-2.amazonaws.com/package/"+
                    visitPackage.getTitle()+"/main/"+visitPackage.getMainImage();

             visitPackageResponseDto  = VisitPackageResponseDto.builder()
                     .id(visitPackage.getId())
                     .mainImage(mainImage)
                     .title(visitPackage.getTitle())
                     .build();
        }


        return ReviewResponseDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .updatedAt(review.getUpdatedAt())
                .author(author)
                .lookupCount(review.getLookupCount())
                .likeCount(this.getLikeCount(review.getId()))
                .contents(review.getContents())
                .visitPackageResponseDto(visitPackageResponseDto)
                .build();
    }

    // 리뷰의 좋아요 개수
    private int getLikeCount(Long reviewId){

        // 해당 리뷰에 로그인 유저의 좋아요 카운팅 후 짝수면 좋아요 증가, 짝수면 좋아요 감소
        List<ReviewLikeCount> reviewLikeList = reviewLikeRepository.findByReviewLike(reviewId);
        int reviewLikeCount = 0;

        for (ReviewLikeCount r : reviewLikeList) {
            reviewLikeCount += r.getCnt();
        }

        return reviewLikeCount;
    }

    // 리뷰 수정
    public ApiResponseDto updateReview(User user, ReviewUpdateRequestDto reviewUpdateRequestDto) {
        try{

            Long userId = user.getId();
            Long reviewId = reviewUpdateRequestDto.getId();

            userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당 유저 정보를 찾을 수 없습니다."));
            Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("리뷰 정보를 찾을 수 없습니다."));

            if (review.getUserId() != user.getId()) {
                return ApiResponseDto.of(ResponseStatusCode.UNAUTHORIZED.getValue(), "삭제 권한이 없습니다.");
            }

            Review updatedReview = review.update(
                    reviewUpdateRequestDto.getTitle(),
                    reviewUpdateRequestDto.getMainImage(),
                    reviewUpdateRequestDto.getContents()
            );


            reviewRepository.save(updatedReview);

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "수정에 성공했습니다.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "수정에 실패했습니다. " + e.getMessage());
        }
    }

    // 리뷰 삭제
    public ApiResponseDto deleteReview(User user, Long reviewId) {
        try{

            userRepository.findById(user.getId()).orElseThrow(() -> new NoSuchElementException("해당 유저 정보를 찾을 수 없습니다."));
            Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("리뷰 정보를 찾을 수 없습니다."));

            if (review.getUserId() != user.getId()) {
                return ApiResponseDto.of(ResponseStatusCode.UNAUTHORIZED.getValue(), "삭제 권한이 없습니다.");
            }

            Review deleteReview = review.delete();

            reviewRepository.save(deleteReview);

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "삭제에 성공했습니다.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "삭제에 실패했습니다. " + e.getMessage());
        }
    }


    // 메인 화면 베스트 후기
    public List<BestReviewResponseDto> getBestReviewList(){
        try{
            return reviewRepository.findBestReviewList().stream().map(this::responseBestReview).toList();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private BestReviewResponseDto responseBestReview(Review review){


        String author = "탈퇴회원";
        Optional<User> optionalUser = userRepository.findById(review.getUserId());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            author = this.getMaskingAuthor(user.getName());
        }

        return BestReviewResponseDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .mainImage(review.getMainImage())
                .author(author)
                .contents(review.getContents())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
