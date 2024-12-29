package com.veedo.tsk.utils;

import com.veedo.tsk.entity.Project;
import com.veedo.tsk.repository.ProjectRepository;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class Helpers {

    public String cleanToken(String token) {
        if (token == null) return null;
        return token.replace("Bearer ", "");
    }


    public String convertTimestampToString(Timestamp timestamp) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return df.format(timestamp);
        } catch (Exception e) {
            return null;
        }
    }

    public String convertToString(Date date) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isValidDate(String dateStr) {
        String pattern = "dd/MM/yyyy";
        if (dateStr == null) return false;
        if (dateStr.length() != pattern.length()) return false;

        DateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        try {
            df.parse(dateStr);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public LocalDate convertToDate(String dateStr) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = df.parse(dateStr);
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (Exception e) {
            return null;
        }
    }


    public String genProjectCode(int numbers) {
        return "PRO-TRC-00" + numbers;
    }

    public String genTaskCode(int numbers) {
        return "TSK-TRC-00" + numbers;
    }




}
