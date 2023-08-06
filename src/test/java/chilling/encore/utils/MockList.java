package chilling.encore.utils;

import chilling.encore.domain.*;
import chilling.encore.utils.domain.*;
import lombok.Getter;

import java.util.List;

@Getter
public class MockList {
    private User user;

    public MockList(User user) {
        this.user = user;
    }

    private TeacherInfo teacherInfo = new MockTeacherInfo().getTeacherInfo(user);
    private Lecture lecture1 = new MockLecture().getLecture(1L, "강남구", teacherInfo, user);
    private Lecture lecture2 = new MockLecture().getLecture(2L, "강남구", teacherInfo, user);
    private Lecture lecture3 = new MockLecture().getLecture(3L, "동작구", teacherInfo, user);
    private List<Lecture> lectures = List.of(lecture1, lecture2, lecture3);
    private LectureMessage lectureMessage1 = new MockLectureMessage().getMockLectureMessage(1L, lecture1, user);
    private LectureMessage lectureMessage2 = new MockLectureMessage().getMockLectureMessage(1L, lecture2, user);
    private LectureMessage lectureMessage3 = new MockLectureMessage().getMockLectureMessage(1L, lecture3, user);
    private List<LectureMessage> lectureMessages = List.of(lectureMessage1, lectureMessage2, lectureMessage3);
    private LearningCenter learningCenter1 = new MockLearningCenter().getMockLearningCenter(1L, "강남구", "test");
    private LearningCenter learningCenter2 = new MockLearningCenter().getMockLearningCenter(2L, "강남구", "test");
    private LearningCenter learningCenter3 = new MockLearningCenter().getMockLearningCenter(3L, "동작구", "test");
    private Program program1 = new MockProgram().getMockProgram(1L, learningCenter1, "test");
    private Program program2 = new MockProgram().getMockProgram(2L, learningCenter2, "test");
    private Program program3 = new MockProgram().getMockProgram(3L, learningCenter3, "test");
    private ListenTogether listenTogether1 = new MockListenTogether().getMockListenTogether(1L, user, program1);
    private ListenTogether listenTogether2 = new MockListenTogether().getMockListenTogether(2L, user, program2);
    private ListenTogether listenTogether3 = new MockListenTogether().getMockListenTogether(3L, user, program3);
    private Participants participants1 = new MockParticipants().getMockParticipants(1L, listenTogether1, user);
    private Participants participants2 = new MockParticipants().getMockParticipants(1L, listenTogether2, user);
    private Participants participants3 = new MockParticipants().getMockParticipants(1L, listenTogether3, user);
    private List<ListenTogether> listenTogethers = List.of(listenTogether1, listenTogether2, listenTogether3);
    private List<Participants> participants = List.of(participants1, participants2, participants3);
    private Review review1 = new MockReview().getMockReview(1L, user, program1);
    private Review review2 = new MockReview().getMockReview(2L, user, program2);
    private Review review3 = new MockReview().getMockReview(3L, user, program3);
    private List<Review> reviews = List.of(review1, review2, review3);
}
