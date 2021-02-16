package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    public static void main(String[] args) {
        Properties config = new Properties();
        try (InputStream rabbitProperties =
                     AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")
        ) {
            config.load(rabbitProperties);
            //Подклюение к базе
            Class.forName(config.getProperty("driver"));
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
        } catch (SchedulerException | IOException | ClassNotFoundException
                | SQLException | InterruptedException se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {
        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            List<Long> store = (List<Long>) context.getJobDetail().getJobDataMap().get("store");
            store.add(System.currentTimeMillis());
        }
    }
}
