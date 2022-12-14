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

    // ?????? ????????? ?????? (?????? ??????. all ?????? ??????)
    public ApiResponseDto<List<ReviewMainResponseDto>> getReviewList(String search, Pageable pageable) {
        try{

            Page<Review> reviewList = reviewRepository.findBySearch(search, pageable);
            Pagination pagination = Pagination.getPagination(reviewList);

            List<ReviewMainResponseDto> reviewMainResponseDtoList = reviewList.map(this::response).toList();

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "????????? ??????????????????.", reviewMainResponseDtoList, pagination);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "????????? ??????????????????. " + e.getMessage());
        }
    }


    // ?????? ??????
    public ApiResponseDto register(User user, ReviewRegisterRequestDto reviewRegisterRequestDto) {
        try{


            // ?????? ????????? ???????????? ??? ???????????? ??????
            Long userId = user.getId();
            Long reservationId = reviewRegisterRequestDto.getReservationId();

            Optional<Reservation> optionalReservation = reservationRepository.findByUserIdAndReservationId(userId, reservationId);

            if(optionalReservation.isEmpty()){
                return ApiResponseDto.of(ResponseStatusCode.UNAUTHORIZED.getValue(), "?????? ????????? ???????????? ?????? ??????????????? ???????????? ????????? ???????????????.");
            }

            // ????????????. ????????? ???????????? ?????? ??? ?????? ?????? ?????? ??????
            Optional<Review> optionalReview = reviewRepository.findByReservationId(reservationId);
            if(optionalReview.isPresent()){
                return ApiResponseDto.of(ResponseStatusCode.BAD_REQUEST.getValue(), "?????? ????????? ?????? ?????? ?????????.");
            }

            // ?????????
            // ????????? ?????? : ????????? ????????? ???????????? S3?????? ????????????.
            // ????????? ?????? ????????????
            // S3?????? ????????? ??????


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

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "?????? ????????? ??????????????????.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "?????? ????????? ??????????????????. " + e.getMessage());
        }
    }

    // ????????? ?????? ????????? (S3) -> url ??????
    public String saveImage(MultipartFile upload) {
        try{
            if(upload == null){
                return null;
            }

            String bucketName = bucket + "/review/upload";


            return "https://shyneeds.s3.ap-northeast-2.amazonaws.com/review/upload/" + itemS3Uploader.uploadLocal(upload, bucketName);


        } catch (Exception e){

            e.printStackTrace();
            return null;
        }
    }

    private ReviewMainResponseDto response(Review review) {

        // user name ????????? (???**) ??????
        Optional<User> optionalUser = userRepository.findById(Long.valueOf(review.getUserId()));
        String author = "????????????";
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

    // ???** ??????
    private String getMaskingAuthor(String username){
        StringBuilder sb = new StringBuilder();
        char[] userNameCharList = username.toCharArray();
        sb.append(userNameCharList[0]);
        for (int i = 0; i < userNameCharList.length-1; i++) {
            sb.append("*");
        }

        return sb.toString();
    }

    // [Mypage] ?????? ????????? ?????? ??????
    public ApiResponseDto<List<ReviewMainResponseDto>> getMyReviewList(User user, Pageable pageable) {
        try{

            Page<Review> userReviewList = reviewRepository.findByUserId(user.getId(), pageable);

            Pagination pagination = Pagination.getPagination(userReviewList);

            List<ReviewMainResponseDto> reviewMainResponseDtoList = userReviewList.map(this::response).stream().toList();

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "????????? ??????????????????.", reviewMainResponseDtoList, pagination);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "????????? ??????????????????. " + e.getMessage());
        }
    }

    // ?????? ?????? ??????
    public ApiResponseDto<ReviewResponseDto> getReview(User user, Long reviewId) {
        try{
            Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("?????? ????????? ?????? ??? ????????????."));


            // ????????? ??????
            Review increaseLookupReview = review.increaseLookup();

            Review detailsReview = reviewRepository.save(increaseLookupReview);

            // 0 ????????? ?????????
            Long loginUserId = user.getId();
            ReviewResponseDto reviewResponseDto = this.responseDetails(loginUserId, detailsReview);

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "????????? ??????????????????.", reviewResponseDto);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "????????? ??????????????????. " + e.getMessage());
        }
    }


    // ?????? ?????? ?????? ??????
    private ReviewResponseDto responseDetails(Long loginUserId, Review review) {

        // ???????????? ?????? ?????????
        String author = "????????????";
        Optional<User> optionalUser = userRepository.findById(review.getUserId());
        boolean isLike = false;
        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            author = this.getMaskingAuthor(user.getName());

            // ????????? ????????? ????????? ??????
            Optional<ReviewLike> optionalReviewLike = reviewLikeRepository.findIsLike(loginUserId, review.getId());

            if(optionalReviewLike.isEmpty()){
                isLike = false;
            } else {
                ReviewLike reviewLike = optionalReviewLike.get();
                isLike = reviewLike.isLikeFlg() ? true : false;
            }
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
                .reservationId(review.getReservationId())
                .title(review.getTitle())
                .mainImage(review.getMainImage())
                .updatedAt(review.getUpdatedAt())
                .author(author)
                .lookupCount(review.getLookupCount())
                .likeCount(this.getLikeCount(review.getId()))
                .isLike(isLike)
                .contents(review.getContents())
                .visitPackageResponseDto(visitPackageResponseDto)
                .build();
    }

    // ????????? ????????? ??????
    private int getLikeCount(Long reviewId){

        // ?????? ????????? ????????? ????????? ????????? ????????? ??? ????????? ????????? ??????, ????????? ????????? ??????
        List<ReviewLikeCount> reviewLikeList = reviewLikeRepository.findByReviewLike(reviewId);
        int reviewLikeCount = 0;

        for (ReviewLikeCount r : reviewLikeList) {
            reviewLikeCount += r.getCnt();
        }

        return reviewLikeCount;
    }

    // ?????? ??????
    public ApiResponseDto updateReview(User user, ReviewUpdateRequestDto reviewUpdateRequestDto) {
        try{

            Long userId = user.getId();
            Long reviewId = reviewUpdateRequestDto.getId();

            userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("?????? ?????? ????????? ?????? ??? ????????????."));
            Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("?????? ????????? ?????? ??? ????????????."));

            if (review.getUserId() != user.getId()) {
                return ApiResponseDto.of(ResponseStatusCode.UNAUTHORIZED.getValue(), "?????? ????????? ????????????.");
            }

            Review updatedReview = review.update(
                    reviewUpdateRequestDto.getTitle(),
                    reviewUpdateRequestDto.getMainImage(),
                    reviewUpdateRequestDto.getContents()
            );


            reviewRepository.save(updatedReview);

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "????????? ??????????????????.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "????????? ??????????????????. " + e.getMessage());
        }
    }

    // ?????? ??????
    public ApiResponseDto deleteReview(User user, Long reviewId) {
        try{

            userRepository.findById(user.getId()).orElseThrow(() -> new NoSuchElementException("?????? ?????? ????????? ?????? ??? ????????????."));
            Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NoSuchElementException("?????? ????????? ?????? ??? ????????????."));

            if (review.getUserId() != user.getId()) {
                return ApiResponseDto.of(ResponseStatusCode.UNAUTHORIZED.getValue(), "?????? ????????? ????????????.");
            }

            Review deleteReview = review.delete();

            reviewRepository.save(deleteReview);

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "????????? ??????????????????.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "????????? ??????????????????. " + e.getMessage());
        }
    }


    // ?????? ?????? ????????? ??????
    public List<BestReviewResponseDto> getBestReviewList(){
        try{
            return reviewRepository.findBestReviewList().stream().map(this::responseBestReview).toList();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private BestReviewResponseDto responseBestReview(Review review){


        String author = "????????????";
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
