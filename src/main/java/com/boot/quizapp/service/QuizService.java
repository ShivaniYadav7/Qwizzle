package com.boot.quizapp.service;

import com.boot.quizapp.dao.QuestionDao;
import com.boot.quizapp.dao.QuizDao;
import com.boot.quizapp.model.Question;
import com.boot.quizapp.model.QuestionWrapper;
import com.boot.quizapp.model.Quiz;
import com.boot.quizapp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        try {
            List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestions(questions);
            Quiz savedQuiz = quizDao.save(quiz);
            return new ResponseEntity<>(savedQuiz.getId().toString(), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        try {
            Optional<Quiz> quiz = quizDao.findById(id);
            if (quiz.isEmpty()) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
            }
            List<Question> questionsFromDB = quiz.get().getQuestions();
            List<QuestionWrapper> questionsForUser = new ArrayList<>();
            for (Question q : questionsFromDB) {
                QuestionWrapper qw = new QuestionWrapper(
                        q.getId(),
                        q.getQuestionTitle(),
                        q.getOption1(),
                        q.getOption2(),
                        q.getOption3(),
                        q.getOption4()
                );
                questionsForUser.add(qw);
            }
            return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        try {
            Optional<Quiz> quiz = quizDao.findById(id);
            if (quiz.isEmpty()) {
                return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
            }
            List<Question> questions = quiz.get().getQuestions();
            int right = 0;
            for (int i = 0; i < responses.size(); i++) {
                if (responses.get(i).getResponse().equals(questions.get(i).getRightAnswer())) {
                    right++;
                }
            }
            return new ResponseEntity<>(right, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
        }
    }
}