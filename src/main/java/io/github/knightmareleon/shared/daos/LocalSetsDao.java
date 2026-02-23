package io.github.knightmareleon.shared.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                switch (question.questionType()) {
                    case "True or False" -> {
                        PreparedStatement tofstmt = this.connection.prepareStatement(
                                this.INSERT_TOF_QUESTION
                        );  
                        
                        int is_true = question.answerIndices().get(0);
                        tofstmt.setString(1, question.question());
                        tofstmt.setInt(2, is_true);
                        tofstmt.setInt(3, setID);

                        tofstmt.executeUpdate();
                    }
                    case "Enumeration" -> {
                        PreparedStatement enumstmt = this.connection.prepareStatement(
                                this.INSERT_STD_QUESTION
                        );  
                        
                        enumstmt.setInt(1,1);
                        enumstmt.setString(2,question.question());
                        enumstmt.setInt(3, setID);
                        
                        enumstmt.executeUpdate();

                        ResultSet enumIDRS = enumstmt.getGeneratedKeys();
                        enumIDRS.next();
                        int enumID = (int) enumIDRS.getLong(1);

                        for(String choice : question.choices()){
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

                        idnstmt.setInt(1,0);
                        idnstmt.setString(2,question.question());
                        idnstmt.setInt(3, setID);

                        idnstmt.executeUpdate();

                        ResultSet idnIDRS = idnstmt.getGeneratedKeys();
                        idnIDRS.next();
                        int idnID = (int) idnIDRS.getLong(1);

                        for(int i = 0; i < question.choices().size(); i++){
                            PreparedStatement choicestmt = this.connection.prepareStatement(
                                    this.INSERT_CHOICE
                            );
                            
                            choicestmt.setString(1, question.choices().get(i));
                            choicestmt.setInt(2, question.answerIndices().contains(i) ? 1 : 0);
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

            existsStatement.execute();
            ResultSet existsResult = existsStatement.getResultSet();
            existsResult.next();

            return existsResult.getLong(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to check if study set exits.", e);
        }
    }
    
}
