package io.github.knightmareleon.shared.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.test.TestType;
import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.exceptions.DataAccessException;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;

public class LocalSetsDao implements SetsDao{

    private final Connection connection;

    private final String SET_TABLE_NAME = "study_set";
    private final String STD_QUESTION_TABLE_NAME = "standard_question";
    private final String TOF_QUESTION_TABLE_NAME = "tof_question";
    private final String CHOICE_TABLE_NAME = "choice";

    private final String INSERT_STUDY_SET = 
        "INSERT INTO " + this.SET_TABLE_NAME + 
        "(title, subject, imgpath, created_on, last_taken_on) " +
        "VALUES(?, ?, ?, ?, ?)";

    private final String INSERT_STD_QUESTION =
        "INSERT INTO " + this.STD_QUESTION_TABLE_NAME +
        "(type, description, set_id) " +
        "VALUES(?, ?, ?)";

    private final String INSERT_TOF_QUESTION = 
        "INSERT INTO " + this.TOF_QUESTION_TABLE_NAME +
        "(description, is_true, set_id) " + 
        "VALUES (?, ?, ?)";

    private final String INSERT_CHOICE =
        "INSERT INTO " + this.CHOICE_TABLE_NAME + 
        "(description, answer, q_id) " + 
        "VALUES (?, ?, ?)";

    private final String EXISTS =
        "SELECT COUNT(*) FROM " + this.SET_TABLE_NAME +
        " WHERE title = ? AND subject = ?";

    private final String TOTAL_ROWS = 
        "SELECT COUNT(*) FROM " + this.SET_TABLE_NAME;

    private final String SET_LIST = 
        "SELECT * FROM " + this.SET_TABLE_NAME + 
        " LIMIT ? OFFSET ?";

    private final String STD_QUESTION_LIST =
        "SELECT * FROM " + this.STD_QUESTION_TABLE_NAME + 
        " WHERE set_id = ?";

    private final String CHOICES_LIST = 
        "SELECT * FROM " + this.CHOICE_TABLE_NAME +
        " WHERE q_id = ?";

    private final String TOF_QUESTION_LIST =
        "SELECT * FROM " + this.TOF_QUESTION_TABLE_NAME + 
        " WHERE set_id = ?";

    private final String DELETE_SET =
        "DELETE FROM " + this.SET_TABLE_NAME + 
        " WHERE id = ?";

    public LocalSetsDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void save(StudySet studySet) throws DataAccessException{
        try {
            PreparedStatement stmt = this.connection.prepareStatement(
                this.INSERT_STUDY_SET
            );

            stmt.setString(1, studySet.getTitle());
            stmt.setString(2, studySet.getSubject());
            stmt.setString(3, studySet.getimgpath());
            stmt.setString(4, studySet.getDateCreatedOn().toString());
            stmt.setString(5, 
            studySet.getDataLastTakeOn() == null ? 
                null : studySet.getDataLastTakeOn().toString()
            );

            stmt.executeUpdate();

            ResultSet setIDRS = stmt.getGeneratedKeys();
            setIDRS.next();
            int setID = (int) setIDRS.getLong(1);

            for (Question question : studySet.getQuestions()){
                switch (question.getType()) {
                    case QuestionType.TRUE_OR_FALSE -> {
                        PreparedStatement tofstmt = this.connection.prepareStatement(
                                this.INSERT_TOF_QUESTION
                        );  

                        int is_true = question.getAnswerIndices().get(0);
                        tofstmt.setString(1, question.getDescription());
                        tofstmt.setInt(2, is_true);
                        tofstmt.setInt(3, setID);

                        tofstmt.executeUpdate();
                    }
                    case QuestionType.ENUMERATION -> {
                        PreparedStatement enumstmt = this.connection.prepareStatement(
                            this.INSERT_STD_QUESTION
                        );  
                        
                        enumstmt.setInt(1, QuestionType.ENUMERATION.getCode());
                        enumstmt.setString(2, question.getDescription());
                        enumstmt.setInt(3, setID);
                        
                        enumstmt.executeUpdate();

                        ResultSet enumIDRS = enumstmt.getGeneratedKeys();
                        enumIDRS.next();
                        int enumID = (int) enumIDRS.getLong(1);

                        for(String choice : question.getChoices()){
                            PreparedStatement choicestmt = this.connection.prepareStatement(
                                this.INSERT_CHOICE
                            );
                            
                            choicestmt.setString(1, choice);
                            choicestmt.setInt(2, 1);
                            choicestmt.setInt(3, enumID);

                            choicestmt.executeUpdate();
                        }
                    }
                    default -> {
                        PreparedStatement idnstmt = this.connection.prepareStatement(
                                this.INSERT_STD_QUESTION
                        );  

                        idnstmt.setInt(1,QuestionType.IDENTIFICATION.getCode());
                        idnstmt.setString(2,question.getDescription());
                        idnstmt.setInt(3, setID);

                        idnstmt.executeUpdate();

                        ResultSet idnIDRS = idnstmt.getGeneratedKeys();
                        idnIDRS.next();
                        int idnID = (int) idnIDRS.getLong(1);

                        for(int i = 0; i < question.getChoices().size(); i++){
                            PreparedStatement choicestmt = this.connection.prepareStatement(
                                    this.INSERT_CHOICE
                            );
                            
                            choicestmt.setString(1, question.getChoices().get(i));
                            choicestmt.setInt(2, question.getAnswerIndices().contains(i) ? 1 : 0);
                            choicestmt.setInt(3, idnID);

                            choicestmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to save StudySet", e);
        }

    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean exists(String title, String subject) {
        try {
            PreparedStatement existsStatement = this.connection.prepareStatement(
                this.EXISTS
            );

            existsStatement.setString(1, title);
            existsStatement.setString(2, subject);

            ResultSet existsResult = existsStatement.executeQuery();
            existsResult.next();

            return existsResult.getLong(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to check if study set exits.", e);
        }
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public int totalRows(){
        try {
            PreparedStatement totalStatement = this.connection.prepareStatement(this.TOTAL_ROWS);

            ResultSet totalSet = totalStatement.executeQuery();
            return (int)totalSet.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to get total number of rows", e);
        }
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public List<StudySet> list(int limit, int offset){
        try {
            List<StudySet> setList = new ArrayList<>();
            PreparedStatement setListStatement = this.connection.prepareStatement(
                this.SET_LIST
            );

            setListStatement.setInt(1, limit);
            setListStatement.setInt(2, offset);
            ResultSet setListResult = setListStatement.executeQuery();
            while(setListResult.next()){
                
                int setId = setListResult.getInt("id");
                String lastTakenOn = setListResult.getString("last_taken_on");

                List<Question> questionList = new ArrayList<>();
                questionList.addAll(this.listStandarQuestions(setId));
                questionList.addAll(this.listTrueOrFalseQuestions(setId));

                setList.add(new StudySet(
                    setId,
                    setListResult.getString("title"),
                    setListResult.getString("subject"),
                    setListResult.getString("imgpath"),
                    setListResult.getInt("total_takes"),
                    questionList,
                    Instant.parse(setListResult.getString("created_on")),
                    lastTakenOn == null ? null : Instant.parse(lastTakenOn)
                ));
            }

            return setList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to get list of sets", e );
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private List<Question> listStandarQuestions(int setId){
        try {
            List<Question> questionList = new ArrayList<>();
            PreparedStatement stdQuestionStatement = this.connection.prepareStatement(
                this.STD_QUESTION_LIST);
            stdQuestionStatement.setInt(1,setId);
            ResultSet stdQsSet = stdQuestionStatement.executeQuery();

            while(stdQsSet.next()){
                int q_id = setId;

                PreparedStatement choiceStatement = this.connection.prepareStatement(
                    this.CHOICES_LIST);
                choiceStatement.setInt(1, q_id);
                ResultSet choiceSet = choiceStatement.executeQuery();
                
                List<String> choices = new ArrayList<>();
                List<Integer> answers = new ArrayList<>();
                
                int index = 0;
                while(choiceSet.next()){

                    choices.add(choiceSet.getString("description"));
                    if(choiceSet.getInt("answer") == 1){
                        answers.add(index++);
                    }
                }
                
                questionList.add(new Question(
                    q_id,
                    stdQsSet.getString("description"),
                    stdQsSet.getInt("type") == QuestionType.IDENTIFICATION.getCode() ? 
                        QuestionType.IDENTIFICATION : QuestionType.ENUMERATION,
                    choices,
                    answers
                ));
            }

            return questionList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to get list of standard question list", e );
        }

    }

    @SuppressWarnings("CallToPrintStackTrace")
    private List<Question> listTrueOrFalseQuestions(int setId){
        try {
            List<Question> questionList = new ArrayList<>();
            PreparedStatement tofStatement = this.connection.prepareStatement(
                this.TOF_QUESTION_LIST);
            tofStatement.setInt(1, setId);
            ResultSet tofQsSet = tofStatement.executeQuery();

            while(tofQsSet.next()){
                Integer answer = tofQsSet.getInt("is_true");
                questionList.add(new Question(
                    tofQsSet.getInt("id"),
                    tofQsSet.getString("description"),
                    QuestionType.TRUE_OR_FALSE,
                    List.of("True", "False"),
                    List.of(answer)
                ));
            }

            return questionList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to get list of true or false question list", e );
        }
    }

    @Override
    public List<StudySet> listByTest(int offset, int limit, TestType type){
        switch(type){
            case TestType.TRUE_OR_FALSE: return this.listTrueOrFalseSets(offset, limit);
            default: throw new IllegalArgumentException("Test not supported.");
        }
    }

    private List<StudySet> listTrueOrFalseSets(int offset, int limit){
        try {
            List<StudySet> setList = new ArrayList<>();
            PreparedStatement setListStatement = this.connection.prepareStatement(
                this.SET_LIST
            );

            setListStatement.setInt(1, limit);
            setListStatement.setInt(2, offset);
            ResultSet setListResult = setListStatement.executeQuery();
            while(setListResult.next()){
                
                int setId = setListResult.getInt("id");
                String lastTakenOn = setListResult.getString("last_taken_on");

                List<Question> questionList = new ArrayList<>();
                questionList.addAll(this.listTrueOrFalseQuestions(setId));

                setList.add(new StudySet(
                    setId,
                    setListResult.getString("title"),
                    setListResult.getString("subject"),
                    setListResult.getString("imgpath"),
                    setListResult.getInt("total_takes"),
                    questionList,
                    Instant.parse(setListResult.getString("created_on")),
                    lastTakenOn == null ? null : Instant.parse(lastTakenOn)
                ));
            }

            return setList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to get list of sets", e );
        }
    }

    @Override
    public void delete(int studySetID){
        try {
            PreparedStatement deleteStatement = this.connection.prepareStatement(
                this.DELETE_SET
            );

            deleteStatement.setInt(1, studySetID);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete study set.", e);
        }
    }
}
