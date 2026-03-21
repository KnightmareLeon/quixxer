package io.github.knightmareleon.shared.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.test.components.constants.TestType;
import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.exceptions.DataAccessException;
import io.github.knightmareleon.shared.exceptions.UniqueStudySetException;
import io.github.knightmareleon.shared.models.Choice;
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

    private final String SETS_WITH_TOF_ONLY_LIST = 
        "SELECT DISTINCT s_set.id, s_set.title, s_set.subject, s_set.imgpath, s_set.total_takes, " +
        " s_set.created_on, s_set.last_taken_on FROM " + 
        this.SET_TABLE_NAME + " s_set INNER JOIN tof_question ON s_set.id = tof_question.set_id"
        +  " LIMIT ? OFFSET ?";

    private final String SETS_WITH_ENUM_ONLY_LIST =
        "SELECT DISTINCT s_set.id, s_set.title, s_set.subject, s_set.imgpath, s_set.total_takes, " +
        "s_set.created_on, s_set.last_taken_on FROM " +
        this.SET_TABLE_NAME + " s_set INNER JOIN standard_question std_q ON s_set.id = std_q.set_id " + 
        " WHERE std_q.type = 1 " +
        "LIMIT ? OFFSET ?";
    
    private final String SETS_WITH_ONE_ANSWER_LIST =
        String.format("""
        SELECT DISTINCT s_set.id, s_set.title, s_set.subject, s_set.imgpath, s_set.total_takes, 
        s_set.created_on, s_set.last_taken_on 
        FROM %s s_set 
        JOIN %s std_q 
            ON s_set.id = std_q.set_id
        WHERE std_q.type = 0
        AND EXISTS (
            SELECT 1
            FROM %s c
            WHERE c.q_id = std_q.id
            GROUP BY c.q_id
            HAVING SUM(c.answer = 1) = 1
        ) LIMIT ? OFFSET ?;
        """, this.SET_TABLE_NAME, this.STD_QUESTION_TABLE_NAME, this.CHOICE_TABLE_NAME);
    
    private final String SETS_MULTIPLE_CHOICE_LIST =
        String.format("""
        SELECT DISTINCT s_set.id, s_set.title, s_set.subject, s_set.imgpath, s_set.total_takes, 
        s_set.created_on, s_set.last_taken_on 
        FROM %s s_set
        JOIN %s std_q 
            ON s_set.id = std_q.set_id
        WHERE std_q.type = 0
        AND EXISTS (
            SELECT 1
            FROM %s c
            WHERE c.q_id = std_q.id
            GROUP BY c.q_id
            HAVING COUNT(*) > 1
        ) LIMIT ? OFFSET ?;
        """, this.SET_TABLE_NAME, this.STD_QUESTION_TABLE_NAME, this.CHOICE_TABLE_NAME);

    private final String ENUM_QUESTION_LIST =
        "SELECT * FROM " + this.STD_QUESTION_TABLE_NAME + " WHERE set_id = ? AND type = 1";

    private final String ONE_ANSWER_QUESTION_LIST =
        String.format("""
        SELECT
            std_q.id AS que_id, 
            std_q.description AS q_desc,
            c.id AS c_id,
            c.description AS c_desc
        FROM %s std_q
        INNER JOIN %s c on std_q.id = c.q_id 
        WHERE type = 0 AND set_id = ?
        GROUP BY std_q.id
        HAVING COUNT(CASE WHEN answer = 1 THEN 1 END) = 1;
        """, this.STD_QUESTION_TABLE_NAME, this.CHOICE_TABLE_NAME);
    
    private final String MULTIPLE_CHOICE_QUESTIONS_LIST =
        String.format("""
        SELECT std_q.id, std_q.description FROM %s std_q
        INNER JOIN %s ON std_q.id = choice.q_id
        WHERE type = 0 AND std_q.set_id = ?
        GROUP BY std_q.id
        HAVING COUNT(choice.q_id) > 1
        """, this.STD_QUESTION_TABLE_NAME, this.CHOICE_TABLE_NAME);

    private final String DELETE_SET =
        "DELETE FROM " + this.SET_TABLE_NAME + 
        " WHERE id = ?";
    
    private final String UPDATE_SET_DETAILS =
        "UPDATE " + this.SET_TABLE_NAME +
        " SET title = ?, subject = ?" +
        " WHERE id = ?";
    
    private final String TOTAL_SETS_WITH_TOF_ONLY =
        "SELECT COUNT(DISTINCT s_set.id) AS total " +
        "FROM " + this.SET_TABLE_NAME + " s_set INNER JOIN " + this.TOF_QUESTION_TABLE_NAME +
        " ON s_set.id = tof_question.set_id";

    private final String TOTAL_SETS_WITH_ENUM_ONLY = 
        "SELECT COUNT(DISTINCT s_set.id) AS total FROM " +
        this.SET_TABLE_NAME + " s_set INNER JOIN " +  this.STD_QUESTION_TABLE_NAME + 
        " std_q ON s_set.id = std_q.set_id WHERE std_q.type = 1 ";
    
    private final String TOTAL_SETS_WITH_ONE_ANSWER_ONLY =
    String.format("""
        SELECT COUNT(id) FROM(
        SELECT DISTINCT s_set.id
        FROM %s s_set 
        JOIN %s std_q 
            ON s_set.id = std_q.set_id
        WHERE std_q.type = 0
        AND EXISTS (
            SELECT 1
            FROM %s c
            WHERE c.q_id = std_q.id
            GROUP BY c.q_id
            HAVING SUM(c.answer = 1) = 1
        )) AS total;
    """, this.SET_TABLE_NAME, this.STD_QUESTION_TABLE_NAME, this.CHOICE_TABLE_NAME);

    private final String TOTAL_SETS_WITH_MULTIPLE_CHOICE =
    String.format("""
        SELECT COUNT(id) FROM(
        SELECT DISTINCT s_set.id
        FROM %s s_set
        JOIN %s std_q 
            ON s_set.id = std_q.set_id
        WHERE std_q.type = 0
        AND EXISTS (
            SELECT 1
            FROM %s c
            WHERE c.q_id = std_q.id
            GROUP BY c.q_id
            HAVING COUNT(*) > 1
        )) AS total;
    """, this.SET_TABLE_NAME, this.STD_QUESTION_TABLE_NAME, this.CHOICE_TABLE_NAME);

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
            studySet.getDateLastTakeOn() == null ? 
                null : studySet.getDateLastTakeOn().toString()
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

                        int is_true = question.getChoices().get(0).isAnswer() ? 1 : 0;
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

                        for(Choice choice : question.getChoices()){
                            PreparedStatement choicestmt = this.connection.prepareStatement(
                                this.INSERT_CHOICE
                            );
                            
                            choicestmt.setString(1, choice.getDescription());
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
                            Choice choice = question.getChoices().get(i);
                            choicestmt.setString(1, choice.getDescription());
                            choicestmt.setInt(2, choice.isAnswer() ? 1 : 0);
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
    public void updateDetails(StudySet studySet){
        try {
            PreparedStatement stmt = this.connection.prepareStatement(
                this.UPDATE_SET_DETAILS
            );
            stmt.setString(1, studySet.getTitle());
            stmt.setString(2, studySet.getSubject());
            stmt.setInt(3, studySet.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            if(e.getMessage().contains("UNIQUE constraint failed")) 
                throw new UniqueStudySetException("Unique study set", e);
            throw new DataAccessException("Failed to update StudySet", e);
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
                questionList.addAll(this.listStandardQuestions(setId));
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
    private List<Question> listStandardQuestions(int setId){
        try {
            List<Question> questionList = new ArrayList<>();
            PreparedStatement stdQuestionStatement = this.connection.prepareStatement(
                this.STD_QUESTION_LIST);
            stdQuestionStatement.setInt(1,setId);
            ResultSet stdQsSet = stdQuestionStatement.executeQuery();

            while(stdQsSet.next()){
                int q_id = stdQsSet.getInt("id");

                PreparedStatement choiceStatement = this.connection.prepareStatement(
                    this.CHOICES_LIST);
                choiceStatement.setInt(1, q_id);
                ResultSet choiceSet = choiceStatement.executeQuery();
                
                List<Choice> choices = new ArrayList<>();

                while(choiceSet.next()){

                    choices.add(new Choice(
                        choiceSet.getString("description"),
                        choiceSet.getInt("answer") == 1
                    ));

                }
                
                questionList.add(new Question(
                    q_id,
                    stdQsSet.getString("description"),
                    stdQsSet.getInt("type") == QuestionType.IDENTIFICATION.getCode() ? 
                        QuestionType.IDENTIFICATION : QuestionType.ENUMERATION,
                    choices
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
                boolean answer = tofQsSet.getInt("is_true") == 1;
                questionList.add(new Question(
                    tofQsSet.getInt("id"),
                    tofQsSet.getString("description"),
                    QuestionType.TRUE_OR_FALSE,
                    List.of(
                        new Choice("True", answer),
                        new Choice("False", !answer)
                    )
                ));
            }

            return questionList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to get list of true or false question list", e );
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private List<Question> listEnumerationQuestions(int setId){
        try {
            List<Question> questionList = new ArrayList<>();
            PreparedStatement enumQuestionStatement = this.connection.prepareStatement(
                this.ENUM_QUESTION_LIST);
            enumQuestionStatement.setInt(1,setId);
            ResultSet enumQsSet = enumQuestionStatement.executeQuery();

            while(enumQsSet.next()){
                int q_id = enumQsSet.getInt("id");

                PreparedStatement choiceStatement = this.connection.prepareStatement(
                    this.CHOICES_LIST);
                choiceStatement.setInt(1, q_id);
                ResultSet choiceSet = choiceStatement.executeQuery();
                
                List<Choice> choices = new ArrayList<>();

                while(choiceSet.next()){

                    choices.add(new Choice(
                        choiceSet.getString("description"),
                        true
                ));
                }
                
                questionList.add(new Question(
                    q_id,
                    enumQsSet.getString("description"),
                    QuestionType.ENUMERATION,
                    choices
                ));
            }

            return questionList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to get list of standard question list", e );
        }

    }

    @SuppressWarnings("CallToPrintStackTrace")
    private List<Question> listOneAnswerQuestions(int setId){
        try {
            List<Question> questionList = new ArrayList<>();
            PreparedStatement oneAnswerQuestionStatement = this.connection.prepareStatement(
                this.ONE_ANSWER_QUESTION_LIST);
            oneAnswerQuestionStatement.setInt(1,setId);
            ResultSet oneAnswerSet = oneAnswerQuestionStatement.executeQuery();

            while(oneAnswerSet.next()){
                questionList.add(new Question(
                    oneAnswerSet.getInt("que_id"),
                    oneAnswerSet.getString("q_desc"),
                    QuestionType.IDENTIFICATION,
                    List.of(
                        new Choice(
                            oneAnswerSet.getString("c_desc"), 
                            true)
                    )
                ));
            }

            return questionList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to get list of standard question list", e );
        }

    }

    @SuppressWarnings("CallToPrintStackTrace")
    private List<Question> listMultipleChoiceQuestions(int setId){
        try {
            List<Question> questionList = new ArrayList<>();
            PreparedStatement mulQuestionStatement = this.connection.prepareStatement(
                this.MULTIPLE_CHOICE_QUESTIONS_LIST);
            mulQuestionStatement.setInt(1,setId);
            ResultSet mulSet = mulQuestionStatement.executeQuery();

            while(mulSet.next()){
                
                int q_id = mulSet.getInt("id");

                PreparedStatement choiceStatement = this.connection.prepareStatement(
                    this.CHOICES_LIST);
                choiceStatement.setInt(1, q_id);
                ResultSet choiceSet = choiceStatement.executeQuery();
                
                List<Choice> choices = new ArrayList<>();

                while(choiceSet.next()){

                    choices.add(
                        new Choice(
                            choiceSet.getString("description"),
                            choiceSet.getInt("answer") == 1
                    ));
                }

                questionList.add(new Question(
                    mulSet.getInt("id"),
                    mulSet.getString("description"),
                    QuestionType.IDENTIFICATION,
                    choices
                ));
            }

            return questionList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to get list of standard question list", e );
        }

    }

    @SuppressWarnings("CallToPrintStackTrace")
    private List<StudySet> listTrueOrFalseSets(int limit, int offset){
        try {
            List<StudySet> setList = new ArrayList<>();
            PreparedStatement setListStatement = this.connection.prepareStatement(
                this.SETS_WITH_TOF_ONLY_LIST
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

    @SuppressWarnings("CallToPrintStackTrace")
    private List<StudySet> listEnumerationSets(int limit, int offset){
        try {
            List<StudySet> setList = new ArrayList<>();
            PreparedStatement setListStatement = this.connection.prepareStatement(
                this.SETS_WITH_ENUM_ONLY_LIST
            );
            setListStatement.setInt(1, limit);
            setListStatement.setInt(2, offset);
            ResultSet setListResult = setListStatement.executeQuery();
            while(setListResult.next()){
                
                int setId = setListResult.getInt("id");
                String lastTakenOn = setListResult.getString("last_taken_on");

                List<Question> questionList = new ArrayList<>();
                questionList.addAll(this.listEnumerationQuestions(setId));

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
    private List<StudySet> listOneAnswerSets(int limit, int offset){
        try {
            List<StudySet> setList = new ArrayList<>();
            PreparedStatement setListStatement = this.connection.prepareStatement(
                this.SETS_WITH_ONE_ANSWER_LIST
            );
            setListStatement.setInt(1, limit);
            setListStatement.setInt(2, offset);
            ResultSet setListResult = setListStatement.executeQuery();
            while(setListResult.next()){
                
                int setId = setListResult.getInt("id");
                String lastTakenOn = setListResult.getString("last_taken_on");

                List<Question> questionList = new ArrayList<>();
                questionList.addAll(this.listOneAnswerQuestions(setId));

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
    private List<StudySet> listMulitpleChoiceSets(int limit, int offset){
        try {
            List<StudySet> setList = new ArrayList<>();
            PreparedStatement setListStatement = this.connection.prepareStatement(
                this.SETS_MULTIPLE_CHOICE_LIST
            );
            setListStatement.setInt(1, limit);
            setListStatement.setInt(2, offset);
            ResultSet setListResult = setListStatement.executeQuery();
            while(setListResult.next()){
                
                int setId = setListResult.getInt("id");
                String lastTakenOn = setListResult.getString("last_taken_on");

                List<Question> questionList = new ArrayList<>();
                questionList.addAll(this.listMultipleChoiceQuestions(setId));

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
    public List<StudySet> listByTest(int limit, int offset, TestType type){
        return switch(type){
            case TestType.MULTIPLE_CHOICE -> this.listMulitpleChoiceSets(limit, offset);
            case TestType.FLASHCARD -> this.listOneAnswerSets(limit, offset);
            case TestType.MATCHING_TYPE -> this.listOneAnswerSets(limit, offset);
            case TestType.ENUMERATION -> this.listEnumerationSets(limit, offset);
            case TestType.TRUE_OR_FALSE -> this.listTrueOrFalseSets(limit, offset);
            default -> throw new IllegalArgumentException("Test not supported.");
        };
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public int totalRowsByTest(TestType testType){
        try {
            PreparedStatement totalStatement = this.connection.prepareStatement(
                switch(testType){
                    case TestType.ENUMERATION -> this.TOTAL_SETS_WITH_ENUM_ONLY;
                    case TestType.FLASHCARD -> this.TOTAL_SETS_WITH_ONE_ANSWER_ONLY;
                    case TestType.MATCHING_TYPE -> this.TOTAL_SETS_WITH_ONE_ANSWER_ONLY;
                    case TestType.MULTIPLE_CHOICE -> this.TOTAL_SETS_WITH_MULTIPLE_CHOICE;
                    case TestType.TRUE_OR_FALSE -> this.TOTAL_SETS_WITH_TOF_ONLY;
                    default -> this.TOTAL_ROWS;
                }
            );

            ResultSet totalSet = totalStatement.executeQuery();
            return (int)totalSet.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to get total number of rows", e);
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
