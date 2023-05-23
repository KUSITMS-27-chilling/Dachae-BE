package chilling.encore.service;

import chilling.encore.domain.*;
import chilling.encore.dto.CommentsDto;
import chilling.encore.dto.CommentsDto.CreateCommentsRequest;
import chilling.encore.exception.ListenException.NoSuchIdxException;
import chilling.encore.exception.ReviewException;
import chilling.encore.exception.UserException;
import chilling.encore.global.config.redis.RedisRepository;
import chilling.encore.global.config.security.util.SecurityUtils;
import chilling.encore.repository.springDataJpa.ListenCommentRepository;
import chilling.encore.repository.springDataJpa.ListenTogetherRepository;
import chilling.encore.repository.springDataJpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ListenCommentService {
    private final ListenCommentRepository listenCommentRepository;
    private final ListenTogetherRepository listenTogetherRepository;
    private final RedisRepository redisRepository;
    private final UserRepository userRepository;
    private final String LISTEN = "listen";
    private int cnt;

    public void listenCommentSave(Long listenIdx, CreateCommentsRequest createCommentsRequest) {
        User user = SecurityUtils.getLoggedInUser().orElseThrow(() -> new ClassCastException("NotLogin"));
        if (isMention(listenIdx, createCommentsRequest, user))
            return;
        // 멘션이 없는 경우
        notMention(listenIdx, createCommentsRequest, user);
    }

    private boolean isMention(Long listenIdx, CreateCommentsRequest createCommentsRequest, User user) {
        if (createCommentsRequest.getMention() != null) { // 멘션이 달렸을 경우 (태그O)
            ListenTogether listenTogether = listenTogetherRepository.findById(listenIdx).orElseThrow(() -> new NoSuchIdxException());
            String listenUserIdx = listenTogether.getUser().getUserIdx().toString();
            String mention = createCommentsRequest.getMention();
            String mentionUserIdx = userRepository.findByNickName(mention)
                    .orElseThrow(() -> new UserException.NoSuchIdxException()).getUserIdx()
                    .toString(); //태그된 사람의 Idx

            validMention(listenIdx, createCommentsRequest, user, listenTogether, listenUserIdx, mention, mentionUserIdx);

            if (Long.parseLong(listenUserIdx) == user.getUserIdx())
                return true;

            addCommentToRedis(listenIdx.toString(), createCommentsRequest, user, listenTogether, listenUserIdx, null);

            saveComments(createCommentsRequest, user, listenTogether);
            return true;

        }  // 멘션이 달렸을 경우 (태그O) 완료
        return false;
    }

    private void validMention(Long listenIdx, CreateCommentsRequest createCommentsRequest, User user, ListenTogether listenTogether, String listenUserIdx, String mention, String mentionUserIdx) {
        if (!mentionUserIdx.equals(listenUserIdx))  {
            // 태그된 사용자 != 게시글 작성자
            if (Long.parseLong(mentionUserIdx) != user.getUserIdx()) {
                //댓글 작성자 != 태그된 사용자
                addCommentToRedis(listenIdx.toString(), createCommentsRequest, user, listenTogether, mentionUserIdx, mention);
            }
        }
    }

    private void notMention(Long listenIdx, CreateCommentsRequest createCommentsRequest, User user) {
        ListenTogether listenTogether = listenTogetherRepository.findById(listenIdx).orElseThrow(() -> new NoSuchIdxException());
        String listenUserIdx = listenTogether.getUser().getUserIdx().toString();
        if (user.getUserIdx() != Long.parseLong(listenUserIdx)) {
            addCommentToRedis(listenIdx.toString(), createCommentsRequest, user, listenTogether, listenUserIdx, null);
        }

        saveComments(createCommentsRequest, user, listenTogether);
    }

    private void addCommentToRedis(String listenIdx, CreateCommentsRequest createCommentsRequest, User user, ListenTogether listenTogether, String listenUserIdx, String mention) {
        redisRepository.addNotification(
                listenUserIdx,
                UUID.randomUUID().toString(),
                listenTogether.getTitle(),
                LISTEN,
                listenIdx,
                user.getNickName(),
                createCommentsRequest.getContent(),
                mention,
                LocalDateTime.now()
        ); //게시글 알림
    }

    private void saveComments(CreateCommentsRequest createCommentsRequest, User user, ListenTogether listenTogether) {
        ListenComments listenComments;
        if (createCommentsRequest.getParentIdx() == null) {
            listenComments = saveParentComments(createCommentsRequest, user, listenTogether);
        } else {
            listenComments = saveChildComments(createCommentsRequest, user, listenTogether);
        }

        listenCommentRepository.save(listenComments);
    }


    private ListenComments saveChildComments(CreateCommentsRequest createCommentsRequest, User user, ListenTogether listenTogether) {
        ListenComments parent = listenCommentRepository.findById(createCommentsRequest.getParentIdx()).orElseThrow(() -> new NoSuchIdxException());

        return ListenComments.builder()
                .user(user)
                .listenTogether(listenTogether)
                .isDelete(false)
                .content(createCommentsRequest.getContent())
                .parent(parent)
                .build();
    }

    private ListenComments saveParentComments(CreateCommentsRequest createCommentsRequest, User user, ListenTogether listenTogether) {
        return ListenComments.builder()
                .user(user)
                .listenTogether(listenTogether)
                .isDelete(false)
                .content(createCommentsRequest.getContent())
                .build();
    }

    public CommentsDto.ListenCommentResponse getListenComments(Long listenIdx) {
        List<ListenComments> listenComments = listenCommentRepository.findAllByListenTogether_ListenIdxOrderByCreatedAtAsc(listenIdx);
        cnt = 0;
        List<CommentsDto.SelectListenComment> selectListenComment = new ArrayList<>();
        for (int i = 0; i < listenComments.size(); i++) {
            List<CommentsDto.ChildListenComment> childListenComments = getChildListenComments(listenComments.get(i));
            if (childListenComments == null) continue;
//            selectListenComment.add(SelectListenComment.from(listenComments.get(i), childListenComments));
        }
        cnt += selectListenComment.size();
        return CommentsDto.ListenCommentResponse.builder()
                .cnt(cnt)
                .listenCommentList(selectListenComment).build();
    }

    @Nullable
    private List<CommentsDto.ChildListenComment> getChildListenComments(ListenComments listenComments) {
        List<CommentsDto.ChildListenComment> childListenComments = new ArrayList<>();
        List<ListenComments> childs = listenComments.getChild();
        if (listenComments.getParent() != null)
            return null;
        for (int j = 0; j < childs.size(); j++) {
//            childListenComments.add(CommentsDto.ChildListenComment.from(childs.get(j)));
        }
        cnt += childListenComments.size();
        return childListenComments;
    }
}
