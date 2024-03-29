package chilling.encore.utils;

import chilling.encore.domain.LearningCenter.entity.LearningCenter;
import chilling.encore.domain.center.entity.Center;
import chilling.encore.domain.lecture.entity.Lecture;
import chilling.encore.domain.lecture.entity.LectureMessage;
import chilling.encore.domain.listenTogether.entity.ListenTogether;
import chilling.encore.domain.listenTogether.entity.Participants;
import chilling.encore.domain.program.entity.Program;
import chilling.encore.domain.review.entity.Review;
import chilling.encore.domain.user.entity.User;
import chilling.encore.utils.domain.*;
import lombok.Data;

import java.util.List;

@Data
public class MockList {
    public MockList(User user) {
        this.user = user;
        teacherInfo = new MockTeacherInfo(1L, user);
        lecture1 = new MockLecture(1L, teacherInfo, "강남구");
        lecture2 = new MockLecture(2L, teacherInfo, "강서구");
        lecture3 = new MockLecture(3L, teacherInfo, "동작구");
        lectures = List.of(lecture1, lecture2, lecture3);
        lectureMessage1 = new MockLectureMessage().getMockLectureMessage(1L, lecture1, user);
        lectureMessage2 = new MockLectureMessage().getMockLectureMessage(1L, lecture2, user);
        lectureMessage3 = new MockLectureMessage().getMockLectureMessage(1L, lecture3, user);
        learningCenter1 = new MockLearningCenter().getMockLearningCenter(1L, "강남구", "test");
        learningCenter2 = new MockLearningCenter().getMockLearningCenter(2L, "강남구", "test");
        learningCenter3 = new MockLearningCenter().getMockLearningCenter(3L, "동작구", "test");
        program1 = new MockProgram().getMockProgram(1L, learningCenter1, "test");
        program2 = new MockProgram().getMockProgram(2L, learningCenter2, "test");
        program3 = new MockProgram().getMockProgram(3L, learningCenter3, "test");
        listenTogether1 = new MockListenTogether(1L, user, program1);
        listenTogether2 = new MockListenTogether(2L, user, program2);
        listenTogether3 = new MockListenTogether(3L, user, program3);
        participants1 = new MockParticipants().getMockParticipants(1L, listenTogether1, user);
        participants2 = new MockParticipants().getMockParticipants(1L, listenTogether2, user);
        participants3 = new MockParticipants().getMockParticipants(1L, listenTogether3, user);
        review1 = new MockReview(1L, user, program1);
        review2 = new MockReview(2L, user, program2);
        review3 = new MockReview(3L, user, program3);
        center1 = new MockCenter(1L, "a", "강남구", 10, "test");
        center2 = new MockCenter(1L, "a", "동작구", 10, "test");
        center3 = new MockCenter(1L, "a", "송파구", 10, "test");
        center4 = new MockCenter(1L, "a", "강서구", 10, "test");
        centers = List.of(center1, center2, center3, center4);
        listenTogethers = List.of(listenTogether1, listenTogether2, listenTogether3);
        reviews = List.of(review1, review2, review3);
        participants = List.of(participants1, participants2, participants3);
        lectureMessages = List.of(lectureMessage1, lectureMessage2, lectureMessage3);

    }
    private User user;
    private MockTeacherInfo teacherInfo;
    private MockLecture lecture1;
    private MockLecture lecture2;
    private MockLecture lecture3;
    private List<Lecture> lectures;
    private LectureMessage lectureMessage1;
    private LectureMessage lectureMessage2;
    private LectureMessage lectureMessage3;
    private LearningCenter learningCenter1;
    private LearningCenter learningCenter2;
    private LearningCenter learningCenter3;
    private Program program1;
    private Program program2;
    private Program program3;
    private MockListenTogether listenTogether1;
    private MockListenTogether listenTogether2;
    private MockListenTogether listenTogether3;
    private Participants participants1;
    private Participants participants2;
    private Participants participants3;
    private MockReview review1;
    private MockReview review2;
    private MockReview review3;
    private MockCenter center1;
    private MockCenter center2;
    private MockCenter center3;
    private MockCenter center4;
    private List<Center> centers;
    private List<ListenTogether> listenTogethers;
    private List<Review> reviews;
    private List<Participants> participants;
    private List<LectureMessage> lectureMessages;
}
