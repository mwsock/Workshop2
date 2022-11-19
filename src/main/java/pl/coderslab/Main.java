package pl.coderslab;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        MainDaoCreate();
        MainDaoRead();
        MainDaoUpdate();
        MainDaoDelete();
        MainDaoFindAll();

    }

    private static void MainDaoCreate() {

        UserDao userDao = new UserDao();

        User user1 = new User();
        user1.setUserName("Kasia");
        user1.setEmail("randomEmail@email.com");
        user1.setPassword("tajneHasło20");
        userDao.create(user1);
        System.out.println(user1);

        User user2 = new User();
        user2.setUserName("Helcia");
        user2.setEmail("sweetKotki98@op.pl");
        user2.setPassword("dupa123");
        userDao.create(user2);
        System.out.println(user2);
    }
    private static void MainDaoRead() {
        UserDao userDao = new UserDao();
        System.out.println("User1: " + userDao.read("id",1));
        System.out.println("User2: " + userDao.read("id",3));
    }

    private static void MainDaoUpdate(){
        UserDao userDao = new UserDao();
        User user = new User();
        user.setUserName("Adaś");
        user.setEmail("leagueoflegends@gmail.com");
        user.setPassword("ryneczek2019");

        userDao.create(user);
        System.out.println(userDao.read("id",3));

        user.setEmail("counterStrikeRoxx@vp.pl");
        userDao.update(user);

        System.out.println(userDao.read("id",3));

    }

    private static void MainDaoDelete(){
        UserDao userDao = new UserDao();
        userDao.delete(1);
        System.out.println(userDao.read("id",1));
    }

    private static void MainDaoFindAll(){
        UserDao userDao = new UserDao();
        System.out.println(Arrays.toString(userDao.findAll()));
    }
}