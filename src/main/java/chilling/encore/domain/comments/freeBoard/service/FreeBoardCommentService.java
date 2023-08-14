package chilling.encore.domain.comments.freeBoard.service;

import chilling.encore.domain.freeBoard.entity.FreeBoard;
import chilling.encore.domain.comments.freeBoard.entity.FreeBoardComments;
import chilling.encore.domain.user.entity.User;
import chilling.encore.domain.comments.dto.CommentsDto;
import chilling.encore.domain.comments.dto.CommentsDto.ChildFreeComment;
import chilling.encore.domain.comments.dto.CommentsDto.CreateCommentsRequest;
import chilling.encore.domain.comments.dto.CommentsDto.FreeCommentResponse;
import chilling.encore.domain.freeBoard.exception.FreeException.NoSuchIdxException;
import chilling.encore.domain.user.exception.UserException;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.domain.comments.freeBoard.repository.jpa.FreeBoardCommentRepository;
import chilling.encore.domain.freeBoard.repository.jpa.FreeBoardRepository;
import chilling.encore.domain.user.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FreeBoardCommentService {
    private final FreeBoardRepository freeBoardRepository;
    private final FreeBoardCommentRepository freeBoardCommentRepository;
    private final RedisRepository redisRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final String FREE = "free";
    private int cnt;

    public void freeCommentSave(Long freeIdx, CreateCommentsRequest createCommentsRequest) {
        User user = securityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        if (isMention(freeIdx, createCommentsRequest, user))
            return;
        // 멘션이 없는 경우
        notMention(freeIdx, createCommentsRequest, user);
    }

    private boolean isMention(Long freeIdx, CreateCommentsRequest createCommentsRequest, User user) {
        if (createCommentsRequest.getMention() != null) { // 멘션이 달렸을 경우 (태그O)
            FreeBoard freeBoard = freeBoardRepository.findById(freeIdx).orElseThrow(() -> new NoSuchIdxException());
            String freeUserIdx = freeBoard.getUser().getUserIdx().toString();
            String mention = createCommentsRequest.getMention();
            String mentionUserIdx = userRepository.findByNickName(mention)
                    .orElseThrow(() -> new UserException.NoSuchIdxException()).getUserIdx()
                    .toString(); //태그된 사람의 Idx

            validMention(freeIdx, createCommentsRequest, user, freeBoard, freeUserIdx, mention, mentionUserIdx);

            if (Long.parseLong(freeUserIdx) == user.getUserIdx())
                return true;

            addCommentToRedis(freeIdx.toString(), createCommentsRequest, user, freeBoard, freeUserIdx, null);

            saveComments(createCommentsRequest, user, freeBoard);
            return true;

        }  // 멘션이 달렸을 경우 (태그O) 완료
        return false;
    }

    private void validMention(Long freeIdx, CreateCommentsRequest createCommentsRequest, User user, FreeBoard freeBoard, String freeUserIdx, String mention, String mentionUserIdx) {
        if (!mentionUserIdx.equals(freeUserIdx))  {
            // 태그된 사용자 != 게시글 작성자
            if (Long.parseLong(mentionUserIdx) != user.getUserIdx()) {
                //댓글 작성자 != 태그된 사용자
                addCommentToRedis(freeIdx.toString(), createCommentsRequest, user, freeBoard, mentionUserIdx, mention);
            }
        }
    }

    private void notMention(Long freeIdx, CreateCommentsRequest createCommentsRequest, User user) {
        FreeBoard freeBoard = freeBoardRepository.findById(freeIdx).orElseThrow(() -> new NoSuchIdxException());
        String freeUserIdx = freeBoard.getUser().getUserIdx().toString();
        if (user.getUserIdx() != Long.parseLong(freeUserIdx)) {
            addCommentToRedis(freeIdx.toString(), createCommentsRequest, user, freeBoard, freeUserIdx, null);
        }

        saveComments(createCommentsRequest, user, freeBoard);
    }

    private void addCommentToRedis(String freeIdx, CreateCommentsRequest createCommentsRequest, User user, FreeBoard freeBoard, String freeUserIdx, String mention) {
        redisRepository.addNotification(
                freeUserIdx,
                UUID.randomUUID().toString(),
                freeBoard.getTitle(),
                FREE,
                freeIdx,
                user.getNickName(),
                createCommentsRequest.getContent(),
                mention,
                LocalDateTime.now()
        ); //게시글 알림
    }

    private void saveComments(CreateCommentsRequest createCommentsRequest, User user, FreeBoard freeBoard) {
        FreeBoardComments freeBoardComments;
        if (createCommentsRequest.getParentIdx() == null) {
            freeBoardComments = saveParentComments(createCommentsRequest, user, freeBoard);
        } else {
            freeBoardComments = saveChildComments(createCommentsRequest, user, freeBoard);
        }

        freeBoardCommentRepository.save(freeBoardComments);
    }

    private FreeBoardComments saveChildComments(CreateCommentsRequest createCommentsRequest, User user, FreeBoard freeBoard) {
        FreeBoardComments parent = freeBoardCommentRepository.findById(createCommentsRequest.getParentIdx()).orElseThrow(() -> new NoSuchIdxException());

        return FreeBoardComments.builder()
                .user(user)
                .freeBoard(freeBoard)
                .isDelete(false)
                .content(createCommentsRequest.getContent())
                .parent(parent)
                .build();
    }

    private FreeBoardComments saveParentComments(CreateCommentsRequest createCommentsRequest, User user, FreeBoard freeBoard) {
        return FreeBoardComments.builder()
                .user(user)
                .freeBoard(freeBoard)
                .isDelete(false)
                .content(createCommentsRequest.getContent())
                .build();
    }

    public FreeCommentResponse getFreeComments(Long freeBoardIdx) {
        List<FreeBoardComments> freeBoardComments = freeBoardCommentRepository.findAllByFreeBoard_FreeBoardIdxOrderByCreatedAtAsc(freeBoardIdx);
        cnt = 0;
        List<CommentsDto.SelectFreeComment> selectFreeComment = new ArrayList<>();
        for (int i = 0; i < freeBoardComments.size(); i++) {
            List<ChildFreeComment> childFreeComments = getChildFreeComments(freeBoardComments.get(i));
            if (childFreeComments == null) continue;
//            selectFreeComment.add(SelectFreeComment.from(freeBoardComments.get(i), childFreeComments));
        }
        cnt += selectFreeComment.size();
        return FreeCommentResponse.builder()
                .cnt(cnt)
                .freeCommentList(selectFreeComment)
                .build();
    }

    @Nullable
    private List<ChildFreeComment> getChildFreeComments(FreeBoardComments freeBoardComments) {
        List<ChildFreeComment> childFreeComments = new ArrayList<>();
        List<FreeBoardComments> childs = freeBoardComments.getChild();
        if (freeBoardComments.getParent() != null)
            return null;
        for (int j = 0; j < childs.size(); j++) {
//            childFreeComments.add(ChildFreeComment.from(childs.get(j)));
        }
        cnt += childFreeComments.size();
        return childFreeComments;
    }
}
