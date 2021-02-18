package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {
    public static void main(String[] args) throws Exception {
        Properties config = new Properties();
        try (InputStream rabbitProperties =
                     AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")
        ) {
            config.load(rabbitProperties);
        }
        Class.forName(config.getProperty("driver"));
        //Подклюение к базе
        try (Connection connection = DriverManager.getConnection(
                config.getProperty("url"),
                config.getProperty("postgres"),
                config.getProperty("password")
        )) {
            //Управление таймерами запуска задач.
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
            //Создание задачи.
            JobDetail job = newJob(Rabbit.class).build();
            //Создание расписания.
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(config.getProperty("rabbit.interval")))
                    .repeatForever();
            //Задача выполняется через триггер.
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            //Загрузка задачи и триггера в планировщик.
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        }

    }

    public static class Rabbit implements Job {
        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into rabbit (created_date) values (?)")){
                preparedStatement.setLong(1, System.currentTimeMillis());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
